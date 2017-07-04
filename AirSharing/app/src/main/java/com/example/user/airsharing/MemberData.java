package com.example.user.airsharing;

import android.os.Parcelable;

/**
 * Created by user on 2017-06-01.
 */

public class MemberData{
    private String name;   // 네이버에서 가져올 사용자 이름
    private String email;  // 네이버에서 가져올 사용자 이메일
    private String birthdate;  // 네이버에서 가져올 사용자 생년월일
    private String gender;   // 네이버에서 가져올 사용자 성별
    private String userid;   // 네이버에서 가져올 아이디
    private String pw;
    private String location;
    private Integer timer;
    private String phone;
    private boolean alarm;

    public MemberData(){
        name = "";
        email = "";
        gender = "";
        birthdate = "";
        userid = "";
        pw = "";
        location = "";
        phone = "";
        email = "";
        timer = 0;
        alarm = false;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getId() {
        return userid;
    }

    public void setId(String id) {
        this.userid = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTimer() {
        return timer;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }
}
