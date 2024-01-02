package com.example.jdbcdemo.utils;

import org.springframework.http.HttpStatusCode;

public class ResponseHandler<T> {
    private String message;

    private HttpStatusCode statusCode;

    private T body;

    public ResponseHandler() {
        message = null;
        statusCode = null;
        body = null;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
