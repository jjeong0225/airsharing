package com.example.user.airsharing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteCallbackList;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 2017-06-01.
 */

public class FindPasswdActivity extends Activity{
    Button ok;
    EditText id, email;
    ApiService apiService;
    String ip = "192.9.113.136";
    int port = 8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);

        // ip, port 연결, apiService 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildApiService(ip, port);
        apiService = ApplicationController.getInstance().getApiService();

        ok = (Button) findViewById(R.id.ok);
        id = (EditText) findViewById(R.id.id);
        email = (EditText) findViewById(R.id.email);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String data1 = id.getText().toString();
                    String data2 = email.getText().toString();
                    Call<List<MemberData>> findpw_data = apiService.findpw(data1, data2);

                    findpw_data.enqueue(new Callback<List<MemberData>>() {
                        @Override
                        public void onResponse(Call<List<MemberData>> call, Response<List<MemberData>> response) {
                            List<MemberData> datas = response.body();
                            if(response.isSuccessful()){
                                if(datas != null){
                                    AlertDialog.Builder alert = new AlertDialog.Builder(FindPasswdActivity.this);
                                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            Intent goLogin = new Intent(FindPasswdActivity.this, LoginActivity.class);
                                            startActivity(goLogin);
                                        }
                                    });
                                    for(MemberData data : datas){
                                        System.out.println("사용자 비밀번호: " + data.getPw());
                                        alert.setMessage("당신의 비밀번호는 " + data.getPw() + "입니다.");
                                        alert.show();
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "일치하는 계정이 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<MemberData>> call, Throwable t) {
                            Log.i("Log", "서버 에러: " + t.getMessage());
                        }
                    });
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "모든 정보를 다 입력하셨나요?", Toast.LENGTH_SHORT).show();
                    System.out.println("에러: " + e);
                }
            }
        });
    }
}
