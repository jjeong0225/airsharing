package com.example.user.airsharing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by surin on 2017. 5. 28..
 */

public class LocationSetActivity extends AppCompatActivity{

    SharedPreferences sharedPreferences;  // 쿠키!
    SharedPreferences.Editor editor;       // 쿠키 에디터!
    private ApiService apiService;

    private WebView webView;
    private Handler handler;
    private Button btn_location;
    private String location;
    String ip = "192.9.113.136";
    int port = 8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationset);

        btn_location = (Button) findViewById(R.id.btn_location);

        // ip, port 연결, apiService 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildApiService(ip, port);
        apiService = ApplicationController.getInstance().getApiService();

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);   // 설정한 쿠키 가져오기

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();

        //TODO 1 : 버튼 클릭시, 선택된 지역 정보를 서버에 전송한다.
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = sharedPreferences.getString("userid", null);   // 쿠키 값 가져오기
                System.out.println("쿠키 아이디: " + id);
                MemberData member = new MemberData();
                member.setId(id);
                member.setLocation(location);

                Call<MemberData> thumbnailCall = apiService.updateLocation(member.getLocation(), member.getId());
                thumbnailCall.enqueue(new Callback<MemberData>() {
                    @Override
                    public void onResponse(Call<MemberData> call, Response<MemberData> response) {
                        if(response.isSuccessful()){
                            //TODO 2 : 변경 성공을 알린 뒤, 지역설정 화면으로 이동한다.
                            //result 값을 서버로 전송. db 에 저장할거야
                            Toast.makeText(getApplicationContext(), "당신의 지역이 " + '"' + location + '"' + "으로 설정되었습니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LocationSetActivity.this, SettingsActivity.class);
                            startActivityForResult(intent,0);
                        }else {
                            int statusCode = response.code();
                            Log.i("MyTag","응답코드 : "+statusCode);
                            Toast.makeText(getBaseContext(),"지역 설정 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<MemberData> call, Throwable t) {
                        Log.i("MyTag","서버 onFailure :  "+t.getMessage());
                    }
                });
            }
        });
    }

    public void init_webView(){
        // WebView 설정
        webView = (WebView) findViewById(R.id.webView);
        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());
        // webview url load
        webView.loadUrl("http://codeman77.ivyro.net/getAddress.php");
    }

    private class AndroidBridge{
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    location = String.format("(%s) %s %s", arg1, arg2, arg3);
                    //result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                }
            });
        }
    }

}
