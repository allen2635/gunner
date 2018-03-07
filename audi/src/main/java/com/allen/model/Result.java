package com.allen.model;

public class Result {

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        if (code == 200) {
            setMsg("succeed");
        } else {
            setMsg("failure");
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
