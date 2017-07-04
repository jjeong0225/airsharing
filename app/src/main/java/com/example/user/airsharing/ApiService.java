package com.example.user.airsharing;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by user on 2017-05-21.
 */

public interface ApiService {

    @POST("/mobile/member/login")
    Call<MemberData> login(@Field("name") String name, @Field("email") String email,
                           @Field("gender") String gender, @Field("birthdate") String birthdate, @Field("naver_id") String naver_id);


}
