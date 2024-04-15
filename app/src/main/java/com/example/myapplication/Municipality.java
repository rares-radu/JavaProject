package com.example.myapplication;

import java.util.HashMap;

public class Municipality
{
    private final String                                            name;
    private final HashMap< Short /*year*/, Integer /*population*/ > populationData;

    public Municipality( String name, HashMap< Short, Integer > populationData )
    {
        this.name           = name;
        this.populationData = populationData;
    }

    public String getName()
    {
        return name;
    }

    public HashMap< Short, Integer > getPopulationData()
    {
        return populationData;
    }
}
