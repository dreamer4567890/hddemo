package com.example.demo.bean;

public class Movie {

    private String name;
    private String url;

    public Movie(String name,String url){
        this.name = name;
        this.url = url;
    }

    public String getName(){
        return name;
    }

    public String getUrl(){
        return url;
    }
}
