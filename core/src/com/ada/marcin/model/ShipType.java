package com.ada.marcin.model;

public enum ShipType {
    PatrolBoat(2),
    Submarine(3),
    Destroyer(3),
    Battleship (4),
    Carrier(5);

    private final int length;
    private ShipType(int lengthNumber){
        this.length=lengthNumber;
    }

    private int getLength(){
        return this.length;
    }
}
