package com.example.myapplication;

public class Weather
{
    private final String name;
    private final String weather;
    private final double temperature;
    private final double feelsLike;
    private final short  humidity;
    private final double windSpeed;

    public Weather( String name, String weather, double temperature, double feelsLike, short humidity, double windSpeed )
    {
        this.name        = name;
        this.weather     = weather;
        this.temperature = temperature;
        this.feelsLike   = feelsLike;
        this.humidity    = humidity;
        this.windSpeed   = windSpeed;
    }

    public static double kelvinToCelsius( double kelvin )
    {
        return kelvin - 273.15f;
    }

    public String getName()
    {
        return name;
    }

    public String getweather()
    {
        return weather;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public double getFeelsLike()
    {
        return feelsLike;
    }

    public short getHumidity()
    {
        return humidity;
    }

    public double getWindSpeed()
    {
        return windSpeed;
    }
}
