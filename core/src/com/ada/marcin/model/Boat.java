package com.ada.marcin.model;

public class Boat {


    private final String name;
    private final int length;
    private int boatIdx;

    public Boat(int boatIdx,
                String name,
                int length) {
        this.name = name;
        this.length = length;
        this.boatIdx = boatIdx;
    }

    public int getBoatIdx() {
        return boatIdx;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }
}
