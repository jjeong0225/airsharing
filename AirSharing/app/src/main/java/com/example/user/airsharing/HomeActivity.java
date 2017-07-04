package com.example.user.airsharing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 2017-06-01.
 * 로그인 후 나타나는 첫 홈 화면!
 */

public class HomeActivity extends Activity{

    private ImageView home, dashboard, map, settings;
    private Button btn_inside, btn_outside;
    private ApiService apiService;
    private Handler mHandler;
    private ProgressDialog mProgressDialog;
    private Button btn_airResult, co, co2, o2, dust, microdust;
    private TextView realocation, temp, humi;
    private String userid, deviceid_inside, deviceid_outside;
    private static final int PERMISSIONS_REQUEST_LOCATION = 2;
    private LocationManager locationManager;
    private LocationListener locationListener;
    String ip = "192.9.113.136";
    int port = 8080;

    //쿠키
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //ip, port 연결
        final ApplicationController application = ApplicationController.getInstance();
        application.buildApiService(ip, port);
        apiService = ApplicationController.getInstance().getApiService();

        //쿠키 설정
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);   // 쿠키 설정
        userid = sharedPreferences.getString("userid", null);

        //위치 UI
        realocation = (TextView) findViewById(R.id.realocation);

        //Air Sensor + temp, humi Sensor Result
        btn_airResult = (Button) findViewById(R.id.home_result);
        temp = (TextView) findViewById(R.id.temp);
        humi = (TextView) findViewById(R.id.humi);
        co = (Button) findViewById(R.id.btn_co);
        co2 = (Button) findViewById(R.id.btn_co2);
        o2 = (Button) findViewById(R.id.btn_o2);
        dust = (Button) findViewById(R.id.btn_dust);
        microdust = (Button) findViewById(R.id.btn_microdust);

        //공기 측정 UI Component
        btn_inside = (Button) findViewById(R.id.btn_inside);
        btn_outside = (Button) findViewById(R.id.btn_outside);

        //Botton Nav Bar UI Component
        home = (ImageView) findViewById(R.id.home);
        dashboard = (ImageView) findViewById(R.id.dashboard);
        map = (ImageView) findViewById(R.id.map);
        settings = (ImageView) findViewById(R.id.settings);


        /*GPS 설정*/
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // TODO 위도, 경도로 하고 싶은 것
                Log.d("GPS 보고싶어요 : ",latitude+", "+longitude);

                Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.KOREA);
                List<Address> address;
                try{
                    if(geocoder != null){
                        //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                        //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                        address = geocoder.getFromLocation(latitude,longitude,1);

                        if(address != null && address.size()>0){
                            //주소 받아오기
                            String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                            realocation.setText(currentLocationAddress);
                        }
                    }

                }catch (IOException e){
                    Toast.makeText(getBaseContext(),"주소를 가져올 수 없습니다",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        /* 스마트 Airzone 실내 측정 요청 리스너 */
        btn_inside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //디바이스 정보 조회 요청
                Call<List<ReqDeviceData>> thumbnailCall2 = apiService.requestDevice(userid,"0");
                thumbnailCall2.enqueue(new Callback<List<ReqDeviceData>>() {
                    @Override
                    public void onResponse(Call<List<ReqDeviceData>> call, Response<List<ReqDeviceData>> response) {
                        if(response.isSuccessful()){
                            //TODO : 디바이스 (실내)정보 받아옴.
                            List<ReqDeviceData> device_temp = response.body();

                            List<String> strarr = new ArrayList<String>();

                            for(ReqDeviceData dd : device_temp){
                                strarr.add(dd.getDevice_id());
                            }

                            final String[] arr = strarr.toArray(new String[strarr.size()]);

                            AlertDialog.Builder dlg = new AlertDialog.Builder(HomeActivity.this);
                            dlg.setTitle("스마트 AirZone(실내) 디바이스 선택");
                            dlg.setSingleChoiceItems(arr, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deviceid_inside = arr[which];
                                    Log.d("Selected Device : ",deviceid_inside);
                                }
                            });
                            dlg.setPositiveButton("측정하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Call<Void> thumbnailCall = apiService.insidework(userid,deviceid_inside);
                                    thumbnailCall.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if(response.isSuccessful()){
                                                // TODO : 스마트 Airzone (실내) 측정 요청 성공
                                            }else{
                                                int statusCode = response.code();
                                                Log.i("MyTag","응답코드 : "+statusCode);
                                                Toast.makeText(getBaseContext(),"실내 측정 요청 실패.",Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Log.i("MyTag","서버 onFailure :  "+t.getMessage());
                                        }

                                    });

                                    //프로그래스바 요청
                                    callProgressBarInside();
                                }
                            });
                            dlg.setNegativeButton("닫기",null);
                            dlg.show();

                        }else{
                            int statusCode = response.code();
                            Log.i("MyTag","응답코드 : "+statusCode);
                            Toast.makeText(getBaseContext(),"실내 디바이스 요청 실패.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ReqDeviceData>> call, Throwable t) {
                        Log.i("MyTag","서버 onFailure :  "+t.getMessage());
                    }
                });
            }
        });

        /* 스마트 Airzone 실외 측정 요청 리스너 */
        btn_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //디바이스 정보 조회 요청
                Call<List<ReqDeviceData>> thumbnailCall2 = apiService.requestDevice(userid,"1");
                thumbnailCall2.enqueue(new Callback<List<ReqDeviceData>>() {
                    @Override
                    public void onResponse(Call<List<ReqDeviceData>> call, Response<List<ReqDeviceData>> response) {
                        if(response.isSuccessful()){
                            //TODO : 디바이스 (실외)정보 받아옴.
                            List<ReqDeviceData> device_temp = response.body();

                            List<String> strarr = new ArrayList<String>();

                            for(ReqDeviceData dd : device_temp){
                                strarr.add(dd.getDevice_id());
                            }

                            final String[] arr = strarr.toArray(new String[strarr.size()]);

                            AlertDialog.Builder dlg = new AlertDialog.Builder(HomeActivity.this);
                            dlg.setTitle("스마트 AirZone(실외) 디바이스 선택");
                            dlg.setSingleChoiceItems(arr, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 선택한 Device_id 저장
                                    deviceid_outside = arr[which];
                                    Log.d("Selected Device: ",deviceid_outside);
                                }
                            });
                            dlg.setPositiveButton("측정하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //실내 측정하기
                                    Call<Void> thumnailCall = apiService.outsidework(userid,deviceid_outside);
                                    thumnailCall.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if(response.isSuccessful()){
                                                // TODO : 스마트 Airzone (실내) 측정 요청 성공
                                            }else{
                                                int statusCode = response.code();
                                                Log.i("MyTag","응답코드 : "+statusCode);
                                                Toast.makeText(getBaseContext(),"실외 측정 요청 실패.",Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Log.i("MyTag","서버 onFailure :  "+t.getMessage());
                                        }
                                    });

                                    //프로그래스바 요청
                                    callProgressBarOutside();
                                }
                            });
                            dlg.setNegativeButton("닫기",null);
                            dlg.show();

                        }else{
                            int statusCode = response.code();
                            Log.i("MyTag","응답코드 : "+statusCode);
                            Toast.makeText(getBaseContext(),"실외 디바이스 요청 실패.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ReqDeviceData>> call, Throwable t) {
                        Log.i("MyTag","서버 onFailure :  "+t.getMessage());
                    }
                });
            }
        });


        /* 내비게이션 버튼 클릭 리스너 선언*/
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(goHome);
            }
        });


        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goDashBoard = new Intent(HomeActivity.this, DashboardActivity.class);
                startActivityForResult(goDashBoard, 0);
            }
        });


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goMap = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(goMap);
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goSettings = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivityForResult(goSettings, 0);
            }
        });

    }


    private void callProgressBarOutside(){
        mHandler = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = ProgressDialog.show(HomeActivity.this,"AirSharing","스마트 AirZone 측정 중 입니다");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if(mProgressDialog != null && mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                                //TODO : 측정 완료 후 DB에서 실외 측정 값 가져옴.
                                Call <List<AirData>> thumbnailCall2 = apiService.requestOutsidedb(userid);
                                thumbnailCall2.enqueue(new Callback<List<AirData>>() {
                                    @Override
                                    public void onResponse(Call<List<AirData>> call, Response<List<AirData>> response) {
                                        if(response.isSuccessful()){
                                            AirData airData = new AirData();
                                            airData.setCo(response.body().get(0).getCo());
                                            airData.setCo_result(response.body().get(0).getCo_result());
                                            airData.setCo2(response.body().get(0).getCo2());
                                            airData.setCo2_result(response.body().get(0).getCo2_result());
                                            airData.setO2(response.body().get(0).getO2());
                                            airData.setO2_result(response.body().get(0).getO2_result());
                                            airData.setDust(response.body().get(0).getDust());
                                            airData.setDust_result(response.body().get(0).getDust_result());
                                            airData.setLongitude(response.body().get(0).getLongitude());
                                            airData.setLatitude(response.body().get(0).getLatitude());
                                            airData.setTemp(response.body().get(0).getTemp());
                                            airData.setHumidity(response.body().get(0).getHumidity());

                                            //View Set
                                            //공기 판별 결과 set
                                            if(airData.getDust_result().equals("bad")){
                                                btn_airResult.setText("나쁨");
                                                btn_airResult.setBackgroundResource(R.drawable.bad);
                                            }else if(airData.getDust_result().equals("normal")){
                                                btn_airResult.setText("보통");
                                                btn_airResult.setBackgroundResource(R.drawable.normal);
                                            }else if(airData.getDust_result().equals("good")){
                                                btn_airResult.setText("좋음");
                                                btn_airResult.setBackgroundResource(R.drawable.good);
                                            }

                                            //온습도 set
                                            temp.setText("온도: "+airData.getTemp()+" ℃");
                                            humi.setText("습도: "+airData.getHumidity()+" %");

                                            //공기 데이터 set
                                            co.setText(airData.getCo()+" ppm");
                                            co2.setText(airData.getCo2()+" ppm");
                                            o2.setText(airData.getO2()+" %");
                                            dust.setText(airData.getDust()+" µg/m3");

                                            ToastMessage();
                                        }else{
                                            int statusCode = response.code();
                                            Log.i("MyTag","응답코드 : "+statusCode);
                                            Toast.makeText(getBaseContext(),"실외 측정 DB 요청 실패.",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<AirData>> call, Throwable t) {
                                        Log.i("MyTag","서버 onFailure :  "+t.getMessage());
                                    }
                                });

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },11000);
            }
        });
    }

    private void callProgressBarInside(){
        mHandler = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = ProgressDialog.show(HomeActivity.this,"AirSharing","스마트 AirZone 측정 중 입니다");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if(mProgressDialog != null && mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();

                                //TODO : 측정 완료 후 DB에서 값을 가져옴.
                                Call <List<AirData>> thumbnailCall2 = apiService.requestInsidedb(userid);
                                thumbnailCall2.enqueue(new Callback <List<AirData>>() {

                                    @Override
                                    public void onResponse(Call<List<AirData>> call, Response<List<AirData>> response) {
                                        if (response.isSuccessful()) {
                                            AirData airData = new AirData();
                                            airData.setCo(response.body().get(0).getCo());
                                            airData.setCo_result(response.body().get(0).getCo_result());
                                            airData.setCo2(response.body().get(0).getCo2());
                                            airData.setCo2_result(response.body().get(0).getCo2_result());
                                            airData.setO2(response.body().get(0).getO2());
                                            airData.setO2_result(response.body().get(0).getO2_result());
                                            airData.setDust(response.body().get(0).getDust());
                                            airData.setDust_result(response.body().get(0).getDust_result());
                                            airData.setTemp(response.body().get(0).getTemp());
                                            airData.setHumidity(response.body().get(0).getHumidity());

                                            //View Set
                                            //공기 판별 결과 set
                                            if(airData.getDust_result().equals("bad")){
                                                btn_airResult.setText("나쁨");
                                                btn_airResult.setBackgroundResource(R.drawable.bad);
                                            }else if(airData.getDust_result().equals("normal")){
                                                btn_airResult.setText("보통");
                                                btn_airResult.setBackgroundResource(R.drawable.normal);
                                            }else if(airData.getDust_result().equals("good")){
                                                btn_airResult.setText("좋음");
                                                btn_airResult.setBackgroundResource(R.drawable.good);
                                            }

                                            //온습도 set
                                            temp.setText("온도: "+airData.getTemp()+" ℃");
                                            humi.setText("습도: "+airData.getHumidity()+" %");

                                            //공기 데이터 set
                                            co.setText(airData.getCo()+" ppm");
                                            co2.setText(airData.getCo2()+" ppm");
                                            o2.setText(airData.getO2()+" %");
                                            dust.setText(airData.getDust()+" µg/m3");

                                            ToastMessage();

                                        }else{
                                            int statusCode = response.code();
                                            Log.i("MyTag","응답코드 : "+statusCode);
                                            Toast.makeText(getBaseContext(),"실내 측정 DB 요청 실패.",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<AirData>> call, Throwable t) {
                                        Log.i("MyTag","서버 onFailure :  "+t.getMessage());
                                    }
                                });

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },10000);
            }
        });
    }

    void ToastMessage(){
        //10초 후 ToastMessage
        Toast.makeText(getBaseContext(),"측정이 완료되었습니다",Toast.LENGTH_SHORT).show();
    }

    /*
     *   사용자의 위치를 수신하는 GetLocation
     */
    private Location getMyLocation(){
        Location currentLocation = null;

        //Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 사용자 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
                Log.d("Main", "longitude=" + lng + ", latitude=" + lat);
            }
        }

        return  currentLocation;
    }

    //사용자 권한 요청 처리
    boolean canReadLocation = false;
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_LOCATION){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // success!
                Location userLocation = getMyLocation();
                if( userLocation != null ) {
                    // todo : 사용자의 현재 위치 구하기
                    double latitude = userLocation.getLatitude();
                    double longitude = userLocation.getLongitude();
                }
                canReadLocation = true;
            } else {
                // Permission was denied or request was cancelled
                canReadLocation = false;
            }
        }
    }


}
