package com.ada.marcin.model;

public class CellContent {
    private final int boatIdx;
    private boolean isDamaged;

    public CellContent(int boatIdx,
                       boolean isDamaged) {
        this.boatIdx = boatIdx;
        this.isDamaged = isDamaged;
    }

    public int getBoatIdx() {
        return boatIdx;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }
}
