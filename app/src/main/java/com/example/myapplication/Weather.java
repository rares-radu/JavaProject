package com.example.myapplication;

public class Weather
{
    private final String name;
    private final String cloudiness;
    private final double temperature;
    private final double feelsLike;
    private final int    humidity;
    private final double windSpeed;

    public Weather( String name, String cloudiness, double temperature, double feelsLike, int humidity, double windSpeed )
    {
        this.name        = name;
        this.cloudiness  = cloudiness;
        this.temperature = temperature;
        this.feelsLike   = feelsLike;
        this.humidity    = humidity;
        this.windSpeed   = windSpeed;
    }

    public String getName()
    {
        return name;
    }

    public String getCloudiness()
    {
        return cloudiness;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public double getFeelsLike()
    {
        return feelsLike;
    }

    public int getHumidity()
    {
        return humidity;
    }

    public double getWindSpeed()
    {
        return windSpeed;
    }

    public static double kelvinToCelsius( double kelvin )
    {
        return kelvin - 273.15f;
    }
}
