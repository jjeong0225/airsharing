package com.example.user.airsharing;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 2017-05-24.
 */

public class ApplicationController extends Application {

    private static ApplicationController instance;
    private ApiService apiService;
    private String url;

    public static ApplicationController getInstance(){
        if(instance == null){
            instance = new ApplicationController();
            return instance;
        }
        else{
            return instance;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;  // 어플 최초 실행 시 인스턴스 생성
    }

    public ApiService getApiService() {return apiService;}

    public void buildApiService(String ip, int port){
        synchronized (ApplicationController.class){   // 스레드 동기화
            if(apiService == null){
                url = String.format("http://%s:%d", ip, port);
                System.out.println("http://"+ip+":"+port);

                Gson gson = new GsonBuilder().setLenient().create();

                GsonConverterFactory factory = GsonConverterFactory.create(gson);
                // 서버에서 json 형식으로 데이터를 보내고 이를 파싱해서 받아오기 위해 설정

                // Retrofit 설정
                Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(factory).build();
                apiService = retrofit.create(ApiService.class);
            }
        }
    }
}
