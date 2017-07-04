package com.example.user.airsharing;

import android.content.Intent;
import android.content.SharedPreferences;
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
 * Created by surin on 2017. 5. 28..
 */

public class InfoSetActivity extends AppCompatActivity{

    SharedPreferences sharedPreferences;  // 쿠키!
    SharedPreferences.Editor editor;       // 쿠키 에디터!
    String userid;

    //Activity 변수
    private EditText name,email,phone;
    private Button change_btn;
    private String memberID, name_str, email_str,phone_str;

    private ApiService apiService;
    String ip = "192.9.113.136";
    int port = 8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoset);

        // ip, port 연결, apiService 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildApiService(ip, port);
        apiService = ApplicationController.getInstance().getApiService();

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);   // 쿠키 설정
        userid = sharedPreferences.getString("userid", null);

        Intent intent = getIntent();
        String intent_name = intent.getStringExtra("name");
        String intent_email = intent.getStringExtra("email");
        String intent_phone = intent.getStringExtra("phone");

        //infoset_activity 매칭
        name = (EditText) findViewById(R.id.name_str);
        email = (EditText) findViewById(R.id.email_str);
        phone = (EditText) findViewById(R.id.phone_str);
        change_btn = (Button) findViewById(R.id.change_btn);

        //TODO 1 : 서버로부터 회원 정보를 요청하여 EditText 각 항목에 회원 정보 Set
        name.setText(intent_name);
        email.setText(intent_email);
        phone.setText(intent_phone);

        //TODO 2 : 변경하기 클릭 버튼 클릭 시 서버에 회원 데이터 전송 후 변경된 데이터로 Set
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 가져오기
                memberID = userid;
                name_str = name.getText().toString();
                email_str = email.getText().toString();
                phone_str = phone.getText().toString();

                Call<MemberData> thumbnailCall = apiService.updateInfo(memberID, name_str, email_str, phone_str);
                thumbnailCall.enqueue(new Callback<MemberData>() {
                    @Override
                    public void onResponse(Call<MemberData> call, Response<MemberData> response) {
                        if (response.isSuccessful()) {
                            //TODO 2 : 변경 성공을 알린다.
                            Toast.makeText(getBaseContext(),"정보 변경 성공",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                            startActivityForResult(intent,0);
                        }else{
                            int statusCode = response.code();
                            Log.i("MyTag","응답코드 : "+statusCode);
                            Toast.makeText(getBaseContext(),"개인 정보 변경 실패.",Toast.LENGTH_SHORT).show();
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
}
