package com.ada.marcin.model;

//Point in a cartesian coordinate system
import java.util.Objects;

public class Coordinate {

    private final int x;
    private final int y;
    private final String data;

    private int hashCode;

    public Coordinate(int x,
                      int y) {
        this.x = x;
        this.y = y;
        this.data = x + "-" + y;
        this.hashCode = Objects.hash(this.x,
                this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public String getData() {
        return data;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return this.getY() + Coordinate.columnLabel(this.getX());
    }


    public static String columnLabel(int column) {
        column--;

        //convert col to label; valid range 0 to 702 (ZZ), col's only relates to A..ZZ
        char[] reference = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        String label = "";

        if (column >= 0 && column <= 80) { //check valid range
            int x = column / 26; //check range; 0 if A..Z
            if (x == 0) {
                label = reference[column] + "";

            } else {
                column = column - (x * 26);
                label = reference[(x - 1)] + "";
                label += reference[column];
            }

        }
        return label;
    }
}