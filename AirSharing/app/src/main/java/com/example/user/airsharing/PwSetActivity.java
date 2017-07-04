package com.example.user.airsharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by surin on 2017. 5. 29..
 */

public class PwSetActivity extends AppCompatActivity {

    //activity 변수
    private EditText pw1, pw2;
    private Button pw_btn;
    String ip = "192.9.113.136";

    //REST 변수
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwset);

        //pwset_activity mapping
        pw1 = (EditText) findViewById(R.id.pw1);
        pw2 = (EditText) findViewById(R.id.pw2);
        pw_btn = (Button) findViewById(R.id.pw_btn);

        // ip, port 연결, apiService 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildApiService(ip, 8080);
        System.out.println("연결된 IP" + ip);
        apiService = ApplicationController.getInstance().getApiService();

        //TODO 1 : 변경하기 클릭시, 변경한 비밀번호를 서버에 전송한다.
        pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd1 = pw1.getText().toString();
                String passwd2 = pw2.getText().toString();

                System.out.println("작동1");
                if(passwd1.equals(passwd2)){
                    //비밀번호 일치
                    //서버 전송
                    MemberData member = new MemberData();
                    member.setPw(passwd1);
                    member.setId("22466035");
                    System.out.println("작동2");

                    Call<MemberData> thumbnailCall = apiService.updatePw(member.getId().toString(), member.getPw().toString());
                    thumbnailCall.enqueue(new Callback<MemberData>() {

                        @Override
                        public void onResponse(Call<MemberData> call, Response<MemberData> response) {
                            System.out.println("작동3");
                            if(response.isSuccessful()){
                                //TODO 2 : 변경 성공을 알린 뒤, 로그인 화면으로 이동한다.
                                Toast.makeText(getBaseContext(),"비밀번호 변경 성공.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PwSetActivity.this,SettingsActivity.class);
                                startActivityForResult(intent,0);
                            }else {
                                System.out.println("작동4");
                                int statusCode = response.code();
                                Log.i("MyTag","응답코드 : "+statusCode);
                                Toast.makeText(getBaseContext(),"비밀번호 변경 실패.",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MemberData> call, Throwable t) {
                            Log.i("MyTag","서버 onFailure :  "+t.getMessage());
                        }
                    });

                }else{
                    //비밀번호 불일치
                    //변경 실패 메시지!
                    Toast.makeText(getBaseContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
