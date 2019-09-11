package com.example.demo.bean;

public class Music {

    private String singer;
    private String url;

    public Music(String singer,String url){
        this.singer = singer;
        this.url = url;
    }

    public String getSinger(){
        return singer;
    }

    public String getUrl(){
        return url;
    }
}
