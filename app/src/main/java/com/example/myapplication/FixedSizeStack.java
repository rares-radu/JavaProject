package com.example.myapplication;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;

public class FixedSizeStack
{
    private final int                  capacity;
    private final ArrayDeque< String > stack;

    public FixedSizeStack( int capacity )
    {
        this.capacity = capacity;
        this.stack    = new ArrayDeque<>( capacity );
    }

    public void push( String query )
    {
        if ( stack.size() == capacity )
        {
            stack.removeFirst();
        }
        stack.addLast( query );
    }

    public String pop()
    {
        return stack.removeLast();
    }

    public void clear()
    {
        stack.clear();
    }

    public boolean isEmpty()
    {
        return stack.isEmpty();
    }

    public int size()
    {
        return stack.size();
    }

    @NotNull
    @Override
    public String toString()
    {
        return stack.toString();
    }
}
