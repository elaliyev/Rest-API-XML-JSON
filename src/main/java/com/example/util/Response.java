package com.example.util;

public class Response {

    private boolean success;
    private int statusCode;
    private String data;

    public Response() {
    }

    public Response(boolean success, int statusCode, String data) {
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
