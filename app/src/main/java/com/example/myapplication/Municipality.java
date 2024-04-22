package com.example.myapplication;

import java.util.HashMap;

public class Municipality
{
    private final String                                            name;
    private final HashMap< Short /*year*/, Integer /*population*/ > populationData;
    private final HashMap< Short /*year*/, Float /*percentage*/ >   employmentData;
    private final HashMap< Short /*year*/, Float /*percentage*/ >   workplaceData;

    public Municipality( String name, HashMap< Short, Integer > populationData, HashMap< Short, Float > employmentData, HashMap< Short, Float > workplaceData )
    {
        this.name           = name;
        this.populationData = populationData;
        this.employmentData = employmentData;
        this.workplaceData  = workplaceData;
    }

    public String getName()
    {
        return name;
    }

    public HashMap< Short, Integer > getPopulationData()
    {
        return populationData;
    }

    public HashMap< Short, Float > getEmploymentData()
    {
        return employmentData;
    }

    public HashMap< Short, Float > getWorkplaceData()
    {
        return workplaceData;
    }
}
