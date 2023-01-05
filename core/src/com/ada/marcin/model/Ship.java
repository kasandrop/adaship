package com.ada.marcin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ship {

    private final int length;
    private int shipType;
    private ArrayList<String> points = new ArrayList<String>();
    private ArrayList<Boolean> damagedElements = new ArrayList<Boolean>();

    //is whole ship sunk? if it is true then yes, and it is needed for the strategy
    private boolean isSunk;
    /*
    keys: A-1 A-2 AA-80
     */
    //  Map<String, Boolean> points = new HashMap<String, Boolean>();

    //position of the first element on the board
    private int x;
    private int y;
    private Direction direction;

    //if ship was added to the board
    private boolean isOnTheBoard;


    public Ship(int name, int length, Direction direction) {
        this.shipType = name;
        this.length = length;
        this.direction = direction;
        for (int i = 0; i < length; i++) {
            damagedElements.add(0, false);

        }

    }

    public List<String> getKeys() {
        return Collections.unmodifiableList(points);
    }

    public int getLength() {
        return this.length;
    }

    public void rotate() {
        this.direction = (this.direction == Direction.Vertical) ? Direction.Horizontal : Direction.Vertical;
        initPoints();
    }


    public void setPosition(int x, int y) {

        this.x = x;
        this.y = y;
        initPoints();
        ;
    }

    public void damageShip(int element) {
        this.damagedElements.set(element, true);
        int allDamaged = 0;
        for (int i = 0; i < this.length; i++) {
            if (this.damagedElements.get(i)) {
                allDamaged++;
            }

        }
        if (allDamaged == this.length) {
            this.isSunk = true;
        }
    }

    public void isOnTheBoard(boolean added) {
        this.isOnTheBoard = added;
    }

    private void initPoints() {
        points.clear();
        if (this.direction == Direction.Horizontal) {
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

    public int getShip() {
        return this.shipType;
    }

    public boolean isShipSunk() {
        return this.isSunk;
    }


}
