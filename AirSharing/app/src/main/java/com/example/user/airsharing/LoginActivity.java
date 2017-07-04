package com.example.user.airsharing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nhn.android.naverlogin.OAuthLogin.mOAuthLoginHandler;

public class LoginActivity extends AppCompatActivity {

    // 네이버 아이디로 로그인하기 위한 클라이언트 정보
    private static String OAUTH_CLIENT_ID = "ptodDHI0yi_4FjKZrSOx";
    private static String OAUTH_CLIENT_SECRET = "aDfvJ6MMFS";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";
    private static OAuthLogin OAuthLoginInstance;
    private static Context context;

    String accessToken = "";
    String tokenType;

    private String ApiResultText = "";

    private String name = "";   // 네이버에서 가져올 사용자 이름
    private String email = "";  // 네이버에서 가져올 사용자 이메일
    private String birthdate = "";  // 네이버에서 가져올 사용자 생년월일
    private String gender = "";   // 네이버에서 가져올 사용자 성별
    private String naver_id = "";   // 네이버에서 가져올 사용자 아이디


    private ApiService apiService;
    SharedPreferences sharedPreferences;  // 쿠키!
    SharedPreferences.Editor editor;       // 쿠키 에디터!


    private OAuthLoginButton naverlogin;
    Button normal_login, signup;
    EditText id, passwd;
    TextView findid, findpw;
    String userid;
    String ip = "192.9.113.136";
    int port = 8080;

    Intent goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ip, port 연결, apiService 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildApiService(ip, port);
        apiService = ApplicationController.getInstance().getApiService();

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        String loginCheck = sharedPreferences.getString("userid", null);


        System.out.println("로그인 기록?: " + loginCheck);
        if (loginCheck != null) {
            goHome = new Intent(getApplicationContext(), HomeActivity.class);   // 쿠키 존재한다면 자동 로그인!!!!
            finish();
            startActivity(goHome);
        }

        OAuthLoginDefine.DEVELOPER_VERSION = true;

        context = getApplicationContext();


        // 네이버로 로그인 버튼 설정
        naverlogin = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        naverlogin.setOAuthLoginHandler(mOAuthLoginHandler);
        naverlogin.setBgResourceId(R.drawable.naverbutton);   // 네이버가 제공하는 버튼 이미지로 설정


        //회원 가입 버튼 설정
        signup = (Button) findViewById(R.id.signup);
        normal_login = (Button) findViewById(R.id.normal_login);
        id = (EditText) findViewById(R.id.id);
        passwd = (EditText) findViewById(R.id.passwd);
        findid = (TextView) findViewById(R.id.findid);
        findpw = (TextView) findViewById(R.id.findpw);


        OAuthLoginInstance = OAuthLogin.getInstance();
        OAuthLoginInstance.init(context, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);


        naverlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  // 네이버로 로그인
                OAuthLoginInstance.startOauthLoginActivity(LoginActivity.this, OAuthLoginHandler);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {   // 회원가입
                Intent goSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(goSignup);
            }
        });

        findid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goFindid = new Intent(LoginActivity.this, FindIdActivity.class);
                startActivity(goFindid);
            }
        });

        findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goFindpw = new Intent(LoginActivity.this, FindPasswdActivity.class);
                startActivity(goFindpw);
            }
        });

        normal_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {   // 일반 로그인

                try {
                    userid = id.getText().toString();

                    Call<ResponseBody> login_data = apiService.login(userid, passwd.getText().toString());

                    login_data.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                System.out.println(response.body().toString());
                                sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.putString("userid", userid);
                                editor.commit();

                                Toast.makeText(getApplicationContext(), "로그인 성공 " + sharedPreferences.getString("userid", null), Toast.LENGTH_SHORT).show();
                                Intent goHome = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivityForResult(goHome, 0);
                            } else {
                                Toast.makeText(getApplicationContext(), "로그인 실패. 회원가입 하셨나요?", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "[서버 에러]로그인 실패", Toast.LENGTH_SHORT).show();
                            Log.i("Log", "서버 에러: " + t.getMessage());
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    System.out.println("에러: " + e);
                }
            }
        });
    }

    private OAuthLoginHandler OAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {   // OAuth2.0 인증이 성공해 접근 토큰과 갱신 토큰을 정상적으로 발급받았을 때
                // OAuth2.0 인증이 성공해 접근 토큰과 갱신 토큰을 정상적으로 발급받았을 때
                System.out.println("성공");
                accessToken = OAuthLoginInstance.getAccessToken(context);  // 로그인 결과로 얻은 접근 토큰 반환
                System.out.println("토큰 받아옴");
                String refreshToken = OAuthLoginInstance.getRefreshToken(context);  // 로그인 결과로 얻은 갱신 토큰 반환
                long expireTime = OAuthLoginInstance.getExpiresAt(context);   // 접근 토큰의 만료 시간 반환
                System.out.println("만료 시간 받아옴");
                tokenType = OAuthLoginInstance.getTokenType(context);
                if (accessToken == null) {  // 접근 토큰이 null 이라면
                    Toast.makeText(getApplicationContext(), "accessToken 널값", Toast.LENGTH_SHORT).show();
                } else {  // 접근 토큰이 null이 아니라면
                    Log.d("Log", "accessToken" + accessToken);
                    Log.d("Log", "refreshToekn" + refreshToken);
                    Log.d("Log", "expire" + expireTime);
                    Log.d("Log", "tokenType" + tokenType);
                    Log.d("Log", "상태" + OAuthLoginInstance.getState(context).toString());

                    new RequestApiTask().execute();
                }
            } else {  // OAuth2.0 인증이 실패했다면
                String errorCode = OAuthLoginInstance.getLastErrorCode(context).getCode();  // 에러 코드
                String errorDesc = OAuthLoginInstance.getLastErrorDesc(context);   // 실패 이유
                Toast.makeText(context, "errorCode: " + errorCode + ", errorDexc: " + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = OAuthLoginInstance.getAccessToken(context);
            pasingversiondata(OAuthLoginInstance.requestApi(context, at, url));

            return "0";
        }

        @Override
        protected void onPostExecute(String s) {
            if (email == null) {
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        }


        private void pasingversiondata(String data) {
            String array[] = new String[10];

            try {
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserFactory.newPullParser();
                InputStream input = new ByteArrayInputStream(data.getBytes("UTF-8"));
                System.out.println("들어온 데이터: " + data);
                parser.setInput(input, "UTF-8");
                parser.next();

                int parserEvent = parser.getEventType();
                String tag;
                boolean state = false;
                int count = 0;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();   // tag값 저장
                            System.out.println("start tag:" + tag);
                            if (tag.compareTo("xml") == 0) {
                                state = false;
                            } else if (tag.compareTo("data") == 0) {
                                state = false;
                            } else if (tag.compareTo("result") == 0) {
                                state = false;
                            } else if (tag.compareTo("resultcode") == 0) {
                                state = false;
                            } else if (tag.compareTo("message") == 0) {
                                state = false;
                            } else if (tag.compareTo("response") == 0) {
                                state = false;
                            } else {
                                state = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (state) {
                                if (parser.getText() == null) {
                                    array[count] = "";
                                } else {
                                    array[count] = parser.getText().trim();
                                }
                                count++;
                            }
                            state = false;
                            break;
                        case XmlPullParser.END_TAG:
                            tag = parser.getName();
                            state = false;
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                Log.e("LOG", "Error in Network Call", e);
            }

            name = array[0];
            gender = array[4];
            naver_id = array[5];
            email = array[6];
            birthdate = array[7];

            MemberData memberData = new MemberData();
            memberData.setName(name);
            memberData.setEmail(email);
            memberData.setBirthdate(birthdate);
            memberData.setGender(gender);
            memberData.setId(naver_id);

            Call<ResponseBody> login_data = apiService.login_naver(name, email, gender, birthdate, naver_id);
            System.out.println("rest api signup!");
            login_data.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("userid", naver_id);
                        editor.commit();

                        System.out.println("리스펀스 바디는? " +response.body().toString());
                            Intent goHome = new Intent(getApplicationContext(), HomeActivity.class);   // 홈 화면으로
                            startActivity(goHome);
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

                    } else {
                        int code = response.code();
                        Log.i("Log", "로그인 실패 코드: " + code);
                        Toast.makeText(getApplicationContext(), "로그인을 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("Log", "서버 에러: " + t.getMessage());
                }
            });
        }

    }

    ;
}
