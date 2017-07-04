package com.example.user.airsharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;
import android.preference.SwitchPreference;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by surin on 2017. 5. 28..
 * Home Bottom Menu 중 '설정'에 해당하는 기능
 */

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener{

    Boolean switched;
    Boolean alarm;

    SharedPreferences sharedPreferences;  // 쿠키!
    SharedPreferences.Editor editor;       // 쿠키 에디터!
    private ApiService apiService;
    String ip = "192.9.113.136";
    int port = 8080;
    String userid;

    @Override
    protected void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        // ip, port 연결, apiService 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildApiService(ip, port);
        apiService = ApplicationController.getInstance().getApiService();

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);   // 쿠키 설정
        userid = sharedPreferences.getString("userid", null);

        //Image View 변경

        addPreferencesFromResource(R.xml.pref_settings);

        Preference infoSet = (Preference) findPreference("infoSet");
        Preference pwSet = (Preference) findPreference("pwSet");
        Preference logoutSet = (Preference) findPreference("logoutSet");
        Preference deleteSet = (Preference) findPreference("deleteSet");
        Preference monitorSet = (Preference) findPreference("monitorSet");
        Preference deviceSet = (Preference) findPreference("deviceSet");
        Preference pushSet = (Preference) findPreference("pushSet");
        Preference locationSet = (Preference) findPreference("locationSet");

        infoSet.setOnPreferenceClickListener(this);
        pwSet.setOnPreferenceClickListener(this);
        logoutSet.setOnPreferenceClickListener(this);
        deleteSet.setOnPreferenceClickListener(this);
        monitorSet.setOnPreferenceClickListener(this);
        deviceSet.setOnPreferenceClickListener(this);
        pushSet.setOnPreferenceClickListener(this);
        locationSet.setOnPreferenceClickListener(this);
    }
    @Override
    public boolean onPreferenceClick(Preference preference){

        if(preference.getKey().equals("infoSet")){   // 개인 정보 변경
            Call<List<MemberData>> member_info = apiService.getInfo(userid);
            System.out.println("개인 정보 데이터 요청!");

            member_info.enqueue(new Callback<List<MemberData>>() {
                @Override
                public void onResponse(Call<List<MemberData>> call, Response<List<MemberData>> response) {
                    if(response.isSuccessful()){
                        System.out.println("요청 성공");
                        System.out.println(response.body());
                        MemberData memberData = new MemberData();
                        memberData.setName(response.body().get(0).getName());
                        memberData.setEmail(response.body().get(0).getEmail());
                        memberData.setPhone(response.body().get(0).getPhone());

                        Intent intent = new Intent(SettingsActivity.this, InfoSetActivity.class);
                        intent.putExtra("name",memberData.getName());
                        intent.putExtra("email",memberData.getEmail());
                        intent.putExtra("phone",memberData.getPhone());
                        startActivity(intent);
                    }
                    else{
                        int statusCode = response.code();
                        Log.i("MyTag","응답코드 : " + statusCode);
                        Toast.makeText(getApplicationContext(),"오류 발생",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<MemberData>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "[서버]오류 발생", Toast.LENGTH_SHORT).show();
                    Log.i("Log", "서버 에러: " + t.getMessage());
                }
            });

        }
        else if(preference.getKey().equals("pwSet")){   // 비밀번호 변경
            Intent intent = new Intent(SettingsActivity.this,PwSetActivity.class);
            startActivityForResult(intent,0);

        }
        else if(preference.getKey().equals("logoutSet")){   // 로그아웃!
            //로그아웃
            //서비스 로그아웃 확인 알림 창 생성
            AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
            dialog.setTitle("로그아웃")
                    .setMessage("AirSharing App에서 로그아웃 하시겠습니까?")
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO : 이전 화면으로 돌아간다.
                        }
                    })
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("userid");   // 쿠키에 저장되어 있는 ID 값 삭제
                            editor.commit();
                            //TODO : 로그인 화면으로 돌아간다.
                            Toast.makeText(getApplicationContext(), "로그아웃 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                            startActivityForResult(intent,0);
                        }
                    });
            dialog.create();
            dialog.show();
        }
        else if(preference.getKey().equals("deleteSet")){    // 서비스 탈퇴
            //서비스 탈퇴 확인 알림 창 생성
            AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
            dialog.setTitle("서비스 탈퇴하기")
                    .setMessage("AirSharing App을 탈퇴 하시겠습니까?")
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO : 이전 화면으로 돌아간다.
                        }
                    })
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO 1: 서버에 서비스 탈퇴 요청
                            //TODO 2: 로그인 화면으로 돌아간다.

                            try {
                                System.out.println("탈퇴하기!");
                                Call<ResponseBody> withdrawal_data = apiService.withdrawal(userid);
                                System.out.println("요청 보냄!");
                                withdrawal_data.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "탈퇴 처리 완료", Toast.LENGTH_SHORT).show();
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.remove("userid");   // 쿠키에 저장되어 있는 ID 값 삭제
                                            editor.commit();
                                            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                            startActivityForResult(intent, 0);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "탈퇴 실패", Toast.LENGTH_SHORT).show();
                                        Log.i("Log", "서버 에러: " + t.getMessage());
                                    }
                                });
                            }catch(Exception e){
                                Toast.makeText(getApplicationContext(), "탈퇴 실패", Toast.LENGTH_SHORT).show();
                                System.out.println("에러: " + e);
                            }

                        }
                    });
            dialog.create();
            dialog.show();

        }
        else if(preference.getKey().equals("monitorSet")){   //모니터링 주기 설정
            //모니터링 주기 설정
            Intent intent = new Intent(SettingsActivity.this,MonitorSetActivity.class);
            startActivityForResult(intent,0);

        }
        else if(preference.getKey().equals("deviceSet")){   // 디바이스 정보 설정
                Intent intent = new Intent(SettingsActivity.this, DeviceSet3Activity.class);
                startActivity(intent);
        }
        else if(preference.getKey().equals("pushSet")){   // 알림 설정 (처음 실행시 False)
            //알림 설정 (처음 실행시 False)
            //TODO 1: ON 설정시 알림 설정 서버에 전송
            if(switched = ((SwitchPreference)preference).isChecked()){
                alarm = true;
                MemberData member = new MemberData();
                member.setId(userid);
                member.setAlarm(alarm);

                Call<MemberData> thumbnailCall = apiService.updateAlarm(member.isAlarm(), member.getId());
                thumbnailCall.enqueue(new Callback<MemberData>() {
                    @Override
                    public void onResponse(Call<MemberData> call, Response<MemberData> response) {
                        if(response.isSuccessful()){
                            //true 값을 서버로 전송. db 에 저장할거야
                            //Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                            //startActivityForResult(intent,0);
                        }else {
                            int statusCode = response.code();
                            Log.i("MyTag","응답코드 : "+statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<MemberData> call, Throwable t) {
                        Log.i("MyTag","서버 onFailure :  "+t.getMessage());
                    }
                });
            }else {
                System.out.println("false");
                alarm = false;
                MemberData member = new MemberData();
                member.setAlarm(alarm);

                Call<MemberData> thumbnailCall = apiService.updateAlarm(member.isAlarm(), member.getId());
                thumbnailCall.enqueue(new Callback<MemberData>() {
                    @Override
                    public void onResponse(Call<MemberData> call, Response<MemberData> response) {
                        if(response.isSuccessful()){
                            //false 값을 서버로 전송. db 에 저장할거야
                            //Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                            //startActivityForResult(intent,0);
                        }else {
                            int statusCode = response.code();
                            Log.i("MyTag","응답코드 : "+statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<MemberData> call, Throwable t) {
                        Log.i("MyTag","서버 onFailure :  "+t.getMessage());
                    }
                });
            }
            //TODO 2 :OFF 설정시 알림 설정 서버에 전송
        }
        else if(preference.getKey().equals("locationSet")){   // 자역 설정
            Intent intent = new Intent(SettingsActivity.this,LocationSetActivity.class);
            startActivityForResult(intent,0);



        }
        return false;
    }


}
