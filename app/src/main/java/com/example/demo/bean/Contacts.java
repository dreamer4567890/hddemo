package com.example.demo.bean;

public class Contacts {

    private String name;
    private String phone;

    public Contacts(String name,String phone){
        this.name = name;
        this.phone = phone;
    }

    public String getName(){
        return name;
    }

    public String getPhone(){
        return phone;
    }
}
