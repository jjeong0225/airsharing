package com.example.user.airsharing;

import android.content.Intent;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Firebase 사용을 위한 devide token을 받아오는 클래스
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("========토큰 얻으려고 하는 중!========");
        Log.d(TAG, "Get token: " + refreshedToken);


        /*
        try{
            retrofit2.Call<Void> send_data = apiService.send_token(refreshedToken);

            send_data.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(retrofit2.Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        System.out.println("토큰 전송 성공");
                    }else{
                        System.out.println("토큰 전송 실패");
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    Log.i("Log", "서버 에러: " + t.getMessage());
                }
            });

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "토큰 저장 실패", Toast.LENGTH_SHORT).show();
            System.out.println("에러: " + e);
        }
*/
    }


}
