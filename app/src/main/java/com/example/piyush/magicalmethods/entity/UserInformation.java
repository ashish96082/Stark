package com.example.piyush.magicalmethods.entity;

/**
 * Created by SWE
 */

public class UserInformation {
    private String name;
    private String city;
    private String phoneNum;

    public UserInformation() {

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

    @Override
    public String toString() {
        return "city: " + this.city + ", name: " + name + ", phone: " + phoneNum;
    }
}
