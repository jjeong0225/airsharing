package com.example.user.airsharing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 2017-06-08.
 * 설정에서 디바이스 세팅 엑티비티
 */

public class DeviceSet3Activity extends AppCompatActivity{

    SharedPreferences sharedPreferences;  // 쿠키!
    SharedPreferences.Editor editor;       // 쿠키 에디터!
    String userid;

    private ApiService apiService;
    String ip = "192.9.113.136";
    int port = 8080;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    ListView listView;
    ImageView add, delete;
    EditText edittext;
    TextView ok;
    int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceset);

        // ip, port 연결, apiService 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildApiService(ip, port);
        apiService = ApplicationController.getInstance().getApiService();

        list = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list);

        listView = (ListView) findViewById(R.id.listview);
        add = (ImageView) findViewById(R.id.add);
        delete = (ImageView) findViewById(R.id.delete);
        edittext = (EditText) findViewById(R.id.edittext);
        ok = (TextView) findViewById(R.id.ok);

        listView.setAdapter(adapter);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count < 5) {
                    EditText device = (EditText) findViewById(R.id.edittext);
                    if (device.getText().length() != 0) {
                        list.add(device.getText().toString());
                        device.setText("");
                        adapter.notifyDataSetChanged();
                        count++;
                    } else {
                        Toast.makeText(getApplicationContext(), "더 이상 추가하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray sba = listView.getCheckedItemPositions();
                if (sba.size() != 0) {
                    for (int i = listView.getCount() - 1; i >= 0; i--) {
                        if (sba.get(i)) {
                            list.remove(i);
                        }
                    }
                    listView.clearChoices();
                    ;
                    adapter.notifyDataSetChanged();
                }
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceData deviceData = new DeviceData();
                deviceData.setUserid(userid);

                if (list.size() < 3) {
                    Toast.makeText(getApplicationContext(), "디바이스 정보를 3개 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (list.size() == 3) {
                        deviceData.setDevice_id1(list.get(0));
                        deviceData.setDevice_id2(list.get(1));
                        deviceData.setDevice_id3(list.get(2));
                        deviceData.setDevice_id4("0");
                        deviceData.setDevice_id5("0");
                    } else if (list.size() == 4) {
                        deviceData.setDevice_id1(list.get(0));
                        deviceData.setDevice_id2(list.get(1));
                        deviceData.setDevice_id3(list.get(2));
                        deviceData.setDevice_id4(list.get(3));
                        deviceData.setDevice_id5("0");
                    } else if (list.size() == 5) {
                        deviceData.setDevice_id1(list.get(0));
                        deviceData.setDevice_id2(list.get(1));
                        deviceData.setDevice_id3(list.get(2));
                        deviceData.setDevice_id4(list.get(3));
                        deviceData.setDevice_id5(list.get(4));
                    }

                    try {
                        Call<DeviceData> device_data = apiService.insert_device(deviceData.getUserid(), deviceData.getDevice_id1(), deviceData.getDevice_id2(), deviceData.getDevice_id3()
                                , deviceData.getDevice_id4(), deviceData.getDevice_id5());

                        device_data.enqueue(new Callback<DeviceData>() {
                            @Override
                            public void onResponse(Call<DeviceData> call, Response<DeviceData> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "디바이스 설정 성공", Toast.LENGTH_SHORT).show();
                                    Intent goLogin = new Intent(DeviceSet3Activity.this, SettingsActivity.class);   // 설정 화면으로 가기
                                    startActivity(goLogin);

                                } else {
                                    int statusCode = response.code();
                                    Log.i("MyTag", "응답코드 : " + statusCode);
                                    Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<DeviceData> call, Throwable t) {
                                Log.i("Log", "서버 에러: " + t.getMessage());
                            }
                        });
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(), "디바이스 설정 실패", Toast.LENGTH_SHORT).show();
                        System.out.println("에러: " + e);
                    }
                }
            }
        });

    }
}
