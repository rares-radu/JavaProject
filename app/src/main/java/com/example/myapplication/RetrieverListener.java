package com.example.myapplication;

public interface RetrieverListener< T >
{
    void onSuccess( T value );

    void onFailure( Exception e );
}
