package com.example.user.airsharing;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by user on 2017-06-01.
 */

public interface ApiService {


    // FCM 토큰값 서버로 전송
    @FormUrlEncoded
    @POST("/mobile/monitoring/fcm/token")
    Call<Void> send_token(@Field("token") String token);


    // 네이버 아이디로 로그인
    @FormUrlEncoded
    @POST("/mobile/member/naver/login")
    Call<ResponseBody> login_naver(@Field("name") String name, @Field("email") String email,
                           @Field("gender") String gender, @Field("birthdate") String birthdate, @Field("naver_id") String naver_id);

    // 일반 로그인
    @FormUrlEncoded
    @POST("/mobile/member/login")
    Call<ResponseBody> login(@Field("id") String id, @Field("pw") String pw);


    // 회원 가입
    @FormUrlEncoded
    @POST("/mobile/member/registeration")   // 회원 가입
    Call<MemberData> signup(@Field("id") String id, @Field("pw") String pw, @Field("name") String name, @Field("email") String email,
                            @Field("phone") String phone, @Field("gender") String gender);

    // 아이디 찾기
    @FormUrlEncoded
    @POST("/mobile/member/request/id")    // 아이디 찾기
    Call<List<MemberData>> findid(@Field("name") String name, @Field("email") String email);


    // 비밀번호 찾기
    @FormUrlEncoded
    @POST("/mobile/member/request/passwd")   // 비밀번호 찾기
    Call<List<MemberData>> findpw(@Field("userid") String userid, @Field("email") String email);


    // 실내 IoT 작동 요청
    @POST("/mobile/request/measurement/inside/{userid}/{deviceid}")
    Call<Void> insidework(@Path("userid") String userid, @Path("deviceid") String deviceid);


    // 실내 IoT 측정 값 가져오기
    @POST("/mobile/request/inside/dbdata/{userid}")
    Call<List<AirData>> requestInsidedb(@Path("userid") String userid);


    //실외 IoT 작동 요청
    @POST("/mobile/request/measurement/outside/{userid}/{deviceid}")
    Call<Void> outsidework(@Path("userid") String userid, @Path("deviceid") String deviceid);


    // 실외 IoT 측정 값 가져오기
    @POST("/mobile/request/outside/dbdata/{userid}")
    Call<List<AirData>> requestOutsidedb(@Path("userid") String userid);


    //개인정보 가져오기
    @GET("/mobile/setting/info/{userid}")
    Call<List<MemberData>> getInfo(@Path("userid") String userid);


    @FormUrlEncoded
    //개인정보 변경
    @PUT("/mobile/setting/modification/{userid}")
    Call<MemberData> updateInfo(@Path("userid") String userid, @Field("name") String name, @Field("email") String email,
                                @Field("phone") String phone);


    @FormUrlEncoded
    //passwd 변경
    @PUT("/mobile/setting/modification/passwd/{userid}")
    Call<MemberData> updatePw(@Path("userid") String userid, @Field("passwd") String passwd);

    // 탈퇴
    @FormUrlEncoded
    @POST("/mobile/member/withdrawal")
    Call<ResponseBody> withdrawal(@Field("userid") String id);


    //알람 온오프 설정
    @PUT("/mobile/setting/alarm/{alarm}/{userid}")
    Call<MemberData> updateAlarm(@Path("alarm") Boolean alarm, @Path("userid") String userid);


    //지역 설정 변경=
    @PUT("/mobile/setting/location/{location}/{userid}")
    Call<MemberData> updateLocation(@Path("location") String location, @Path("userid") String userid);


    //모니터링 주기 설정
    @PUT("/mobile/setting/modification/cycle/{monitoring}/{userid}")
    Call<MemberData> updateMonitoring(@Path("userid") String userid, @Path("monitoring") Integer monitoring);


    //디바이스 정보 가져오기
    @FormUrlEncoded
    @POST("/mobile/member/device/info")
    Call<List<ReqDeviceData>> get_device(@Field("userid") String user_id);

    //디바이스 정보 조회 가져오기
    @POST("/mobile/request/deviceinfo/{userid}/{devicenum}")
    Call<List<ReqDeviceData>> requestDevice(@Path("userid") String userid,
                                            @Path("devicenum") String deviceid);

    //디바이스 정보 변경
    @FormUrlEncoded
    @POST("/mobile/member/device")
    Call<DeviceData> insert_device(@Field("userid") String user_id, @Field("device_id1") String device_id1, @Field("device_id2") String device_id2,
                                   @Field("device_id3") String device_id3, @Field("device_id4") String device_id4, @Field("device_id5") String device_id5);

    // 시/도별 미세먼지 데이터 가져오기
    @GET("/mobile/monitoring/area/dust")
    Call<ArrayList> get_dust();

    // 사용자 위치 기반 미세먼지 데이터 가져오기
    @GET("/mobile/monitoring/areabased/dust/{userid}")
    Call<List<AirData>> get_userArea(@Path("userid") String userid);

}
