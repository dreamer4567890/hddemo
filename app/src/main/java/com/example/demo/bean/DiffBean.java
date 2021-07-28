package com.example.demo.bean;

import static com.example.demo.adapter.DiffAdapter.VIEW_TYPE_EMPTY;

public class DiffBean {

    private int id;
    private String name;
    private String value;
    private String title;
    private int type;//判断布局类型

    public DiffBean(int id, String name, String value, String title, int type) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.title = title;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEmpty() {
        return type == VIEW_TYPE_EMPTY;
    }
}
