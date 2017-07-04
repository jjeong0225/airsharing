package com.example.user.airsharing;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.nhn.android.naverlogin.data.OAuthErrorCode;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.util.HashMap;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;

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

    private TextView ApiResultText;

    private String name = "";   // 네이버에서 가져올 사용자 이름
    private String email = "";  // 네이버에서 가져올 사용자 이메일
    private String birthdate = "";  // 네이버에서 가져올 사용자 생년월일
    private String gender = "";   // 네이버에서 가져올 사용자 성별
    private String naver_id = "";   // 네이버에서 가져올 사용자 아이디


    private ApiService apiService;

    private OAuthLoginButton naverlogin;
    private Button normal_login;
    private Button signup;
    private EditText id;
    private EditText passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // ip, port 연결, apiService 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildApiService("localhost.", 1000);
        apiService = ApplicationController.getInstance().getApiService();

        OAuthLoginDefine.DEVELOPER_VERSION = true;

        context = getApplicationContext();

        // 네이버로 로그인 버튼 설정
        naverlogin = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        naverlogin.setOAuthLoginHandler(mOAuthLoginHandler);
        naverlogin.setBgResourceId(R.drawable.naverbutton);   // 네이버가 제공하는 버튼 이미지로 설정


        OAuthLoginInstance = OAuthLogin.getInstance();   // 네이버 아이디로 로그인 인스턴스 얻기
        OAuthLoginInstance.init(context, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

        naverlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OAuthLoginInstance.startOauthLoginActivity(LoginActivity.this, OAuthLoginHandler);  // 네이버로 로그인 클릭 시 handler 호출
            }
        });


    }


    // 로그인이 종료되었을 때 실행되는 OAuthLoginHandler 클래스
    private OAuthLoginHandler OAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {

            if (success) {  // OAuth2.0 인증이 성공해 접근 토큰과 갱신 토큰을 정상적으로 발급받았을 때
                System.out.println("성공");
                accessToken = OAuthLoginInstance.getAccessToken(context);  // 로그인 결과로 얻은 접근 토큰 반환
                System.out.println("토큰 받아옴");
                String refreshToken = OAuthLoginInstance.getRefreshToken(context);  // 로그인 결과로 얻은 갱신 토큰 반환
                long expireTime = OAuthLoginInstance.getExpiresAt(context);   // 접근 토큰의 만료 시간 반환
                System.out.println("만료 시간 받아옴");
                tokenType = OAuthLoginInstance.getTokenType(context);
                if(accessToken == null){
                    Toast.makeText(getApplicationContext(), "accessToken 널값", Toast.LENGTH_SHORT).show();
                }
                else{

                    Log.d("Log", "accessToken" + accessToken);
                    Log.d("Log", "refreshToekn" + refreshToken);
                    Log.d("Log", "expire" + expireTime);
                    Log.d("Log", "tokenType" + tokenType);
                    Log.d("Log", "상태" + OAuthLoginInstance.getState(context).toString());


                    new RequestApiTask().execute();
                }

            } else {  // 실패했을 시
                String errorCode = OAuthLoginInstance.getLastErrorCode(context).getCode();  // 에러 코드
                String errorDesc = OAuthLoginInstance.getLastErrorDesc(context);   // 실패 이유
                Toast.makeText(context, "errorCode: " + errorCode + ", errorDexc: " + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };



    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            ApiResultText.setText((String) "");
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = OAuthLoginInstance.getAccessToken(context);
            System.out.println("받아온것:" + OAuthLoginInstance.requestApi(context, at, url));
            return OAuthLoginInstance.requestApi(context, at, url);
        }

        @Override
        protected void onPostExecute(String s) {

            System.out.println("이름: " + name);
            System.out.println("이메일: " + email);
            System.out.println("이름: " + name);

            if (email == null) {
                Toast.makeText(LoginActivity.this, "[로그인 실패]다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
            ApiResultText.setText((String) s);
        }

        private void Passingversiondata(String data){
            String array[] = new String[5];

            try{
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserFactory.newPullParser();
                InputStream input = new ByteArrayInputStream(data.getBytes("UTF-8"));
                parser.setInput(input, "UTF-8");

                int parserEvent = parser.getEventType();
                String tag;
                boolean state = false;
                int count = 0;

                while(parserEvent != XmlPullParser.END_DOCUMENT){
                    switch(parserEvent){
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();
                            if(tag.compareTo("xml") == 0){
                                state = false;
                            }
                            else if(tag.compareTo("data") == 0){
                                state = false;
                            }
                            else if(tag.compareTo("result") == 0){
                                state = false;
                            }
                            else if(tag.compareTo("resultcode") == 0){
                                state = false;
                            }
                            else if(tag.compareTo("message") == 0){
                                state = false;
                            }
                            else if(tag.compareTo("response") == 0){
                                state = false;
                            }
                            else{
                                state = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            tag = parser.getName();
                            if(state){
                                if(parser.getText() == null){
                                    array[count] = "";
                                }
                                else{
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
                }
            }catch(Exception e){
                Log.e("LOG", "Error in Network Call", e);
            }

            email = array[0];
            gender = array[5];
            naver_id = array[6];
            name = array[7];
            birthdate = array[8];


            MemberData memberData = new MemberData();
            memberData.setName(name);
            memberData.setEmail(email);
            memberData.setBirthdate(birthdate);
            memberData.setGender(gender);
            memberData.setNaver_id(naver_id);

            Log.d("로그인 정보", memberData.getName() + ":" + memberData.getEmail() + ":" + memberData.getGender());

            Call<MemberData> login_data = apiService.login(name, email, gender, birthdate, naver_id);
            login_data.enqueue(new Callback<MemberData>() {
                @Override
                public void onResponse(Call<MemberData> call, Response<MemberData> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                        Intent goHome = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(goHome);
                    }
                    else{
                        int code = response.code();
                        Log.i("Log", "로그인 실패 코드: "+code);
                    }
                }

                @Override
                public void onFailure(Call<MemberData> call, Throwable t) {
                    Log.i("Log", "서버 에러: " + t.getMessage());
                }
            });

        }

    }

}
