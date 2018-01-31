package com.example.piyush.magicalmethods;

/**
 * Created by Piyush on 29-Jan-18.
 */

public class UserInformation {
    private String name;
    private String city;
    private String phoneNum;

    public UserInformation(){

    }

    public UserInformation(String city, String name, String phoneNum) {
        this.city = city;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
