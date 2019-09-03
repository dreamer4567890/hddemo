package com.example.demo.Bean;

public class Music {

    private String singer;
    private int imageId;

    public Music(String singer,int imageId){
        this.singer = singer;
        this.imageId = imageId;
    }

    public String getSinger(){
        return singer;
    }

    public int getImageId(){
        return imageId;
    }
}
