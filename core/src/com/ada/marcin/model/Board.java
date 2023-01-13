package com.ada.marcin.model;

import com.ada.marcin.screen.ui.ShipView;
import com.badlogic.gdx.utils.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class will represent Shipboard or Target Board
 */
public class Board {

    public static final Logger logger = new Logger(Board.class.getName(),
            Logger.DEBUG);

    private Map<Coordinate, CellContent> board = new HashMap<Coordinate, CellContent>();
    //amount of columns and rows in a  board
    private final int columns;
    private final int rows;


    public Board(int columns, int rows) {

        this.rows = rows;
        this.columns = columns;
    }

    /**
     * Having at least two coordinate points, function is able to determine the direction of the ShipView
     * @param coordinates
     * @return
     */
    public static Direction checkDirection(List<Coordinate> coordinates) {
        int x1 = coordinates.get(0)
                .getX();
        int x2 = coordinates.get(1)
                .getX();
        if (x1 == x2) {
            return Direction.Vertical;
        }
        return Direction.Horizontal;
    }

    /**
     * Used by  Target board to save the  shots
     * @param coordinate
     * @param boatIdx unique identifier of the Ship
     */
    public void save(Coordinate coordinate, int boatIdx) {
        this.board.put(coordinate, new CellContent(boatIdx, true));
    }

    /**
     *
     * @param key  Coordinate on the Grid
     * @return  CellContent packs information about the ship which occupies that coordinate
     */
    public CellContent getValue(Coordinate key) {
        return this.board.get(key);
    }

    /**
     * Opponent  uses the function to check if there was a Ship on the  provided Coordinate
     * @param coordinate
     * @return  -1 if no ship is placed  on that Coordinate, Otherwise, unique identifier
     * is returned,which describes the ship
     */
    public int hit(Coordinate coordinate) {
        CellContent cellContent = this.getValue(coordinate);
        if (cellContent == null) {
            return -1;
        }
        cellContent.setDamaged(true);
        return cellContent.getBoatIdx();
    }

    /**
     *
     * @return  All  coordinates of the ships are returned (if Board represents Shipboard)
     *            or coordinates of all shots if board represents Target board
     */
    public Set<Coordinate> getKeys() {
        return this.board.keySet();
    }

    /**  Inner method
     *
     * @param coordinates  check if ShipView  can be placed on that Coordinates
     * @return   true if   placement is allowed , otherwise false
     */
    private boolean isPlacementAllowed(List<Coordinate> coordinates) {
        logger.debug("isPlacementAllowed()");
        Direction direction = Board.checkDirection(coordinates);
        //if there are more than two coordinates it is a ship  with length min 2

        if (direction == Direction.Horizontal) {
            int y = coordinates.get(0)
                    .getY();
            int x = coordinates.get(0)
                    .getX();
            int xMin = x;
            int xMax = x;
            for (Coordinate coordinate : coordinates) {
                int myX = coordinate.getX();
                xMin = Math.min(myX,
                        xMin);
                xMax = Math.max(myX,
                        xMax);
            }
            xMin = xMin - 1;
            xMax = xMax + 1;
            xMin = Math.max(xMin,
                    1);
            xMax = Math.min(xMax,
                    this.columns);

            int yMin = y - 1;
            int yMax = y + 1;
            yMin = Math.max(yMin,
                    1);
            yMax = Math.min(yMax,
                    this.rows);

            if (isCellOccupied(yMin,
                    yMax,
                    xMin,
                    xMax)) return false;
        } else {

            int y = coordinates.get(0)
                    .getY();
            int x = coordinates.get(0)
                    .getX();
            int yMin = y;
            int yMax = y;
            for (Coordinate coordinate : coordinates) {
                int myY = coordinate.getY();
                yMin = Math.min(myY,
                        yMin);
                yMax = Math.max(myY,
                        yMax);
            }
            yMin = yMin - 1;
            yMax = yMax + 1;
            yMin = Math.max(yMin,
                    1);
            yMax = Math.min(yMax,
                    this.rows);

            int xMin = x - 1;
            int xMax = x + 1;
            xMin = Math.max(xMin,
                    1);
            xMax = Math.min(xMax,
                    this.columns);

            if (isCellOccupied(yMin,
                    yMax,
                    xMin,
                    xMax)) return false;
        }
        return true;
    }

    private boolean isCellOccupied(int yMin,
                                   int yMax,
                                   int xMin,
                                   int xMax) {
        for (int yy = yMin; yy <= yMax; yy++) {
            logger.debug("\n");
            for (int xx = xMin; xx <= xMax; xx++) {
                if (board.containsKey(new Coordinate(xx, yy))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param shipView  ShipViews can not overlap, nor can be placed one next to the other.
     *                  If the rules are satisfied then
     * @return  true if placement was successful, false otherwise
     */
    public boolean placeShipOnTheGrid(ShipView shipView) {
        if (!isPlacementAllowed(shipView.getCoordinates())) {
            logger.debug("Placement not allowed");
            return false;
        }
        for (Coordinate coordinate : shipView.getCoordinates()) {

            board.put(coordinate,
                    new CellContent(shipView.getShipType(),
                            false));
        }
        return true;
    }

    public void removeShipFromTheGrid(ShipView shipView) {
        logger.debug("number of elements   before  deletion :" + board.size());
        for (Coordinate coordinate : shipView.getCoordinates()) {
            board.remove(coordinate);
        }
        logger.debug("number of elements   after deletion :" + board.size());
    }

    public void clear() {
        this.board.clear();
    }
}
