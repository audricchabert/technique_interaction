package com.example.clement.themaze;

import android.graphics.drawable.Drawable;

/**
 * Created by Jean-Philippe Kha on 12/11/2015.
 */
public class Fruits {

    private int value;
    private String name;

    public Fruits(int value, String name){
        this.value=value;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
