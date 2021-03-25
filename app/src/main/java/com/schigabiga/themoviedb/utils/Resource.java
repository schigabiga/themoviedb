package com.schigabiga.themoviedb.utils;

public class Resource<T>{
    private Status status;
    private T data;
    private Object o;

    public Resource(Status status, T data, Object o) {
        this.status = status;
        this.data = data;
        this.o = o;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}