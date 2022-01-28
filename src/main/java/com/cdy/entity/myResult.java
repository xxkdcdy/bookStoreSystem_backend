package com.cdy.entity;

public class myResult {
    //响应码400 和 200
    private int code;

    public myResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
