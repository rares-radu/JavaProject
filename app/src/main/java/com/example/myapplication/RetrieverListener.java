package com.example.myapplication;

public interface RetrieverListener
{
    void onSuccess(Weather weather);
    void onFailure(Exception e);
}
