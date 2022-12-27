package com.ada.marcin.model;

public class CellContent {
    private final ShipType shipType;
    private final int indexOfTheShip;
    private boolean isDamaged;

    public CellContent(ShipType shipType, int indexOfTheShip, boolean isDamaged) {
        this.shipType = shipType;
        this.indexOfTheShip = indexOfTheShip;
        this.isDamaged = isDamaged;
    }

    public ShipType getShipType() {
        return shipType;
    }


    public int getIndexOfTheShip() {
        return indexOfTheShip;
    }


    public boolean isDamaged() {
        return isDamaged;
    }

    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }
}
