package com.ada.marcin.model;

public class Boat {


    private final String name;
    private final int length;

    public Boat(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }
}
