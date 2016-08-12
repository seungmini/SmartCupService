package com.example.lageder.touchuiexample;

/**
 * Created by Lageder on 2016-08-12.
 */
public class SOOL implements Comparable {
    private String name;
    private int value;

    public SOOL(String n, int v) {
        this.name = n;
        this.value = v;
    }

    public String getName() { return name; }
    public int getValue() { return value; }

    @Override
    public int compareTo(Object obj) {
        SOOL other = (SOOL)obj; // DownCasting
        if(value < other.value)
            return 1;
        else if(value > other.value)
            return -1;
        else
            return 0;
    }
}
