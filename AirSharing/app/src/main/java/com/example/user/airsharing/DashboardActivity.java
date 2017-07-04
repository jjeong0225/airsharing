package com.example.user.airsharing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by user on 2017-06-03.
 */

public class DashboardActivity extends Activity{

    ApiService apiService;
    SharedPreferences sharedPreferences;  // 쿠키!
    SharedPreferences.Editor editor;       // 쿠키 에디터!
    String userid;

    String ip = "192.9.113.136";
    int port = 8080;
    Button btnO2, btnCO, btnCO2, btnDust;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        btnCO2 = (Button)findViewById(R.id.btnCO2);
        btnCO = (Button)findViewById(R.id.btnCO);
        btnO2 = (Button)findViewById(R.id.btnO2);
        btnDust = (Button)findViewById(R.id.btnDust);
        webView = (WebView)findViewById(R.id.webView);

        //ip, port 연결
        final ApplicationController application = ApplicationController.getInstance();
        application.buildApiService(ip, port);
        apiService = ApplicationController.getInstance().getApiService();

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);   // 쿠키 설정
        userid = sharedPreferences.getString("userid", null);

        btnCO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://airsharing.ddns.net:8080/mobile/chart/today/co/" + userid;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                //webView.loadUrl("http://airsharing.ddns.net:8080/mobile/chart/today/co/" + userid);
            }
        });
        btnCO2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://airsharing.ddns.net:8080/mobile/chart/today/co2/" + userid;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                //webView.loadUrl("http://airsharing.ddns.net:8080/mobile/chart/today/co2/" + userid);
            }
        });
        btnO2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://airsharing.ddns.net:8080/mobile/chart/today/o2/" + userid;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                //webView.loadUrl("http://airsharing.ddns.net:8080/mobile/chart/today/o2/" + userid);
            }
        });
        btnDust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://airsharing.ddns.net:8080/mobile/chart/today/dust/" + userid;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                //webView.loadUrl("http://airsharing.ddns.net:8080/mobile/chart/today/dust/" + userid);
            }
        });
    }
}
