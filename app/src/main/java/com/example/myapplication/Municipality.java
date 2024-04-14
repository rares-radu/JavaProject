package com.example.myapplication;

public class Municipality
{
    private final short year;
    private final int   population;

    public Municipality( short year, int population )
    {
        this.year = year;
        this.population = population;
    }

    public short getYear()
    {
        return year;
    }

    public int getPopulation()
    {
        return population;
    }
}
