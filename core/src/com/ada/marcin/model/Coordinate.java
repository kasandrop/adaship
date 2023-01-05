package com.ada.marcin.model;

//Point in a cartesian coordinate system

import com.ada.marcin.common.WrongCoordinateException;

import java.util.Objects;

public class Coordinate {

    private final int x;
    private final int y;
    private final String data;

    private int hashCode;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        this.data = x + "-" + y;
        this.hashCode = Objects.hash(this.x, this.y);
    }

    //  coordinate   is "7-2"   where 7 is a row and 2 is a column
//    public Coordinate(String coordinate) throws WrongCoordinateException {
//        this.data = coordinate;
//        if (!this.data.contains("-")) throw new WrongCoordinateException("Wrong Coordinate format provided. Lack of \"-\"");
//        try {
//            this.x = Integer.parseInt(coordinate.split("-")[0]);
//
//        } catch (NumberFormatException e) {
//            throw new WrongCoordinateException("Wrong Coordinate provided. X is not a number ");
//        }
//        try {
//            this.y = Integer.parseInt(coordinate.split("-")[1]);
//
//        } catch (NumberFormatException e) {
//            throw new WrongCoordinateException("Wrong Coordinate provided. Y is not a number ");
//        }
//
//
//    }


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
        return "Coordinate :" + data;
    }


    public static String columnLabel(int column) {

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