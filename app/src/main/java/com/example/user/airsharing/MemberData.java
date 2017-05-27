package com.example.user.airsharing;

/**
 * Created by user on 2017-05-21.
 * 회원 계정 DO 클래스
 */


public class MemberData {

    private String name;   // 네이버에서 가져올 사용자 이름
    private String email;  // 네이버에서 가져올 사용자 이메일
    private String birthdate;  // 네이버에서 가져올 사용자 생년월일
    private String gender;   // 네이버에서 가져올 사용자 성별
    private String naver_id;   // 네이버에서 가져올 네이버 아이디

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

    public String getNaver_id() {
        return naver_id;
    }

    public void setNaver_id(String naver_id) {
        this.naver_id = naver_id;
    }


}
