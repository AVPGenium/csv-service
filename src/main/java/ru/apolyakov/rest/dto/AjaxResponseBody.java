package ru.apolyakov.rest.dto;

public class AjaxResponseBody {
    private String msg;

    public AjaxResponseBody() {
    }

    public AjaxResponseBody(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
