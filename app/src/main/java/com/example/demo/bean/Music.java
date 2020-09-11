package com.example.demo.bean;

public class Music {

    private String singer;
    private String url;
    private int pos;

    public Music(String singer, String url, int pos) {
        this.singer = singer;
        this.url = url;
        this.pos = pos;
    }

    public String getSinger(){
        return singer;
    }

    public String getUrl(){
        return url;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Music{" +
                "singer='" + singer + '\'' +
                ", url='" + url + '\'' +
                ", pos=" + pos +
                '}';
    }
}
