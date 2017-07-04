package com.example.user.airsharing;

/**
 * Created by surin on 2017. 6. 9..
 */

public class AirData {
    int num;
    String userid;
    String co;
    String co_result;
    String co2;
    String co2_result;
    String o2;
    String o2_result;
    String dust;
    String dust_result;
    String finedust;
    String finedust_result;
    String temp;
    String humidity;
    String latitude;
    String longitude;
    String date;

    public AirData(){
        num=0;
        userid="";
        co="";
        co_result="";
        co2="";
        co2_result="";
        o2="";
        o2_result="";
        dust="";
        dust_result="";
        finedust="";
        finedust_result="";
        temp="";
        humidity="";
        latitude="";
        longitude="";
        date="";
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getCo_result() {
        return co_result;
    }

    public void setCo_result(String co_result) {
        this.co_result = co_result;
    }

    public String getCo2() {
        return co2;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
    }

    public String getCo2_result() {
        return co2_result;
    }

    public void setCo2_result(String co2_result) {
        this.co2_result = co2_result;
    }

    public String getO2() {
        return o2;
    }

    public void setO2(String o2) {
        this.o2 = o2;
    }

    public String getO2_result() {
        return o2_result;
    }

    public void setO2_result(String o2_result) {
        this.o2_result = o2_result;
    }

    public String getDust() {
        return dust;
    }

    public void setDust(String dust) {
        this.dust = dust;
    }

    public String getDust_result() {
        return dust_result;
    }

    public void setDust_result(String dust_result) {
        this.dust_result = dust_result;
    }

    public String getFinedust() {
        return finedust;
    }

    public void setFinedust(String finedust) {
        this.finedust = finedust;
    }

    public String getFinedust_result() {
        return finedust_result;
    }

    public void setFinedust_result(String finedust_result) {
        this.finedust_result = finedust_result;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
