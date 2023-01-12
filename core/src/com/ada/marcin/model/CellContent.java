package com.ada.marcin.model;

public class CellContent {
    private final int boatIdx;
    private final int indexOfTheShip;
    private boolean isDamaged;

    public CellContent(int boatIdx,
                       int indexOfTheShip,
                       boolean isDamaged) {
        this.boatIdx = boatIdx;
        this.indexOfTheShip = indexOfTheShip;
        this.isDamaged = isDamaged;
    }

    public int getBoatIdx() {
        return boatIdx;
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
