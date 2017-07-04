package com.example.user.airsharing;

import android.*;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ApiService apiService;
    String ip = "192.9.113.136";
    int port = 8080;

    SharedPreferences sharedPreferences;  // 쿠키!
    SharedPreferences.Editor editor;       // 쿠키 에디터!
    String userid;

    // Add a marker and move the camera
    LatLng seoul = new LatLng(37.566535, 126.977969);
    LatLng busan = new LatLng(35.1795543, 129.0756416);
    LatLng daegu = new LatLng(35.8714354, 128.601445);
    LatLng incheon = new LatLng(37.4562557, 126.7052062);
    LatLng gwangju = new LatLng(35.1595454, 126.8526011);
    LatLng daejeon = new LatLng(36.3504119, 127.3845475);
    LatLng ulsan = new LatLng(35.5383773, 129.3113596);
    LatLng gyeonggi = new LatLng(37.413799, 127.518299);
    LatLng gangwon = new LatLng(37.8228, 128.155499);
    LatLng chungbuk = new LatLng(36.8, 127.700000);
    LatLng chungnam = new LatLng(36.5184, 126.799999);
    LatLng jeonbuk = new LatLng(35.717500, 127.153000);
    LatLng jeonnam = new LatLng(34.8679, 126.990999);
    LatLng gyeongbuk = new LatLng(36.4919, 128.888900);
    LatLng gyeongnam = new LatLng(35.4606, 128.213200);
    LatLng jeju = new LatLng(33.4996213, 126.5311884);
    LatLng sejong = new LatLng(36.4799395, 127.2622516);


    Button btn1;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // ip, port 연결, apiService 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildApiService(ip, port);
        apiService = ApplicationController.getInstance().getApiService();

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);   // 쿠키 설정
        userid = sharedPreferences.getString("userid", null);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                try{
                    Call<ArrayList> dustdata = apiService.get_dust();

                    dustdata.enqueue(new Callback<ArrayList>() {
                        @Override
                        public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                            if(response.isSuccessful()){

                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(seoul).title("서울"+ ": " + response.body().get(0) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(busan).title("부산"+ ": " + response.body().get(1) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(daegu).title("대구"+ ": " + response.body().get(2) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(incheon).title("인천"+ ": " + response.body().get(3) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(gwangju).title("광주"+ ": " + response.body().get(4) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(daejeon).title("대전"+ ": " + response.body().get(5) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(ulsan).title("울산"+ ": " + response.body().get(6) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(gyeonggi).title("경기"+ ": " + response.body().get(7) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(gangwon).title("강원"+ ": " + response.body().get(8) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(chungbuk).title("충북"+ ": " + response.body().get(9) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(chungnam).title("충남"+ ": " + response.body().get(10) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(jeonbuk).title("전북"+ ": " + response.body().get(11) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(jeonnam).title("전남"+ ": " + response.body().get(12) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(gyeongbuk).title("경북"+ ": " + response.body().get(13) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(gyeongnam).title("경남"+ ": " + response.body().get(14) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(jeju).title("제주"+ ": " + response.body().get(15) + "pm10"));
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(sejong).title("세종"+ ": " + response.body().get(16) + "pm10"));

                                Toast.makeText(getApplicationContext(), "데이터 로드 성공", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                int statusCode = response.code();
                                Log.i("오류","응답 코드 : " + statusCode);
                                Toast.makeText(getBaseContext(),"데이터 로드 실패",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "[서버 에러]데이터 로드 실패", Toast.LENGTH_SHORT).show();
                            Log.i("Log", "서버 에러: " + t.getMessage());
                        }
                    });

                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "데이터 로드 실패", Toast.LENGTH_SHORT).show();
                    System.out.println("에러: " + e);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mMap.clear();
                try{
                    Call<List<AirData>> data = apiService.get_userArea(userid);


                    data.enqueue(new Callback<List<AirData>>() {
                        @Override
                        public void onResponse(Call<List<AirData>> call, Response<List<AirData>> response) {
                            if(response.isSuccessful()){
                                for(int i = 0; i < response.body().size(); i++){
                                    AirData airData = new AirData();
                                    airData.setDust(response.body().get(i).getDust());
                                    airData.setLatitude(response.body().get(i).getLatitude());
                                    airData.setLongitude(response.body().get(i).getLongitude());
                                    System.out.println(response.body().get(i).getLongitude());
                                    LatLng latLng  = new LatLng(Float.parseFloat(response.body().get(i).getLatitude()), Float.parseFloat(response.body().get(i).getLongitude()));
                                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2)).position(latLng).title(airData.getDust()+ "pm10"));
                                }

                                Toast.makeText(getApplicationContext(), "데이터 로드 성공", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                int statusCode = response.code();
                                Log.i("오류","응답 코드 : " + statusCode);
                                Toast.makeText(getBaseContext(),"데이터 로드 실패",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<AirData>> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "[서버 에러]데이터 로드 실패", Toast.LENGTH_SHORT).show();
                            Log.i("Log", "서버 에러: " + t.getMessage());
                        }
                    });

                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "데이터 로드 실패", Toast.LENGTH_SHORT).show();
                    System.out.println("에러: " + e);
                }
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(6.0f);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(chungbuk));

    }
}
