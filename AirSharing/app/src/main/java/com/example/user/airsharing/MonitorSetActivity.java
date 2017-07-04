package com.example.user.airsharing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by surin on 2017. 5. 28..
 */

public class MonitorSetActivity extends AppCompatActivity{

    //REST 변수
    private ApiService apiService;
    SharedPreferences sharedPreferences;  // 쿠키!
    SharedPreferences.Editor editor;       // 쿠키 에디터!

    private Spinner spinner;
    private Button btn_cycle;
    String result, memberID, original;
    Integer i, monitoring;
    String ip = "192.9.113.136";
    int port = 8080;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitorset);
        setTitle("모니터링 주기 설정");

        // ip, port 연결, apiService 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildApiService(ip, port);
        apiService = ApplicationController.getInstance().getApiService();

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);   // 쿠키 설정
        userid = sharedPreferences.getString("userid", null);

        spinner = (Spinner)findViewById(R.id.spinner);
        btn_cycle = (Button)findViewById(R.id.btn_cycle);


        final String[] cycle_time = {"10분", "20분", "30분", "1시간", "2시간", "3시간", "4시간" };

        for (i=0; i<cycle_time.length; i++){
            original = cycle_time[i];

        }

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cycle_time);
        spinner.setAdapter(adapter);

        btn_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼 눌렀을 때의 액션
                //결과를 DB에 저장해야 한다
                //result는 spinner에서 선택된 항목을 받는다
                result = String.valueOf(spinner.getSelectedItem());

                //선택된 항목에 따라 숫자로 처리
                if (result.equals("10분")){
                    monitoring = Integer.parseInt(result.replace("10분", "10"));
                }else if (result.equals("20분")){
                    monitoring = Integer.parseInt(result.replace("20분", "20"));
                }else if (result.equals("30분")){
                    monitoring = Integer.parseInt(result.replace("30분", "30"));
                }else if(result.equals("1시간")){
                    monitoring = Integer.parseInt(result.replace("1시간", "60"));
                }else if(result.equals("2시간")){
                    monitoring = Integer.parseInt(result.replace("2시간", "120"));
                }else if(result.equals("3시간")){
                    monitoring = Integer.parseInt(result.replace("3시간", "180"));
                }else if(result.equals("4시간")){
                    monitoring = Integer.parseInt(result.replace("4시간", "240"));
                }else {
                    System.out.println("error");
                }


                Call<MemberData> thumbnailCall = apiService.updateMonitoring(userid, monitoring);
                thumbnailCall.enqueue(new Callback<MemberData>() {
                    @Override
                    public void onResponse(Call<MemberData> call, Response<MemberData> response) {
                        if(response.isSuccessful()){
                            //TODO 2 : 변경 성공을 알린 뒤, 지역설정 화면으로 이동한다.
                            //result 값을 서버로 전송. db 에 저장할거야
                            Toast.makeText(getApplicationContext(), result + "으로 설정되었습니다" , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MonitorSetActivity.this, SettingsActivity.class);
                            startActivityForResult(intent,0);
                        }else {
                            int statusCode = response.code();
                            Log.i("MyTag","응답코드 : "+statusCode);
                            Toast.makeText(getBaseContext(),"타이머 변경 실패.",Toast.LENGTH_SHORT).show();
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
