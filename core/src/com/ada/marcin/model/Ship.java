package com.ada.marcin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ship {

    private final String name;
    private final int length;
    private ArrayList<String> points = new ArrayList<String>();
    /*
    keys: A-1 A-2 AA-80
     */
    //  Map<String, Boolean> points = new HashMap<String, Boolean>();
    private int x;
    private int y;
    private Direction direction;
    private boolean isOnTheBoard ;


    public List<String> getKeys() {
        return Collections.unmodifiableList(points);
    }

    public Ship(String name, int length, Direction direction) {
        this.name = name;
        this.length = length;
        this.direction = direction;

    }


    public int getLength() {
        return this.length;
    }

    public void rotate() {
        this.direction = (this.direction == Direction.North_South) ? Direction.West_East : Direction.North_South;
        initPoints();
    }


    public void setPosition(int x, int y) {

        this.x = x;
        this.y = y;
        initPoints();
        ;
    }

    public void isOnTheBoard(boolean added){
        this.isOnTheBoard =added;
    }

    private void initPoints() {
        points.clear();
        if (this.direction == Direction.West_East) {
            for (int i = 0; i < this.length; i++) {
                int result = this.y + i;
                String key = this.x + "_" + result;
                points.add(key);
            }

            return;
        }
        for (int i = 0; i < this.length; i++) {
            int result = this.x + i;
            String key = result + "_" + this.y;
            points.add(key);
        }
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public String getShip() {
        return this.name;
    }


}
