package com.ada.marcin.model;

import com.ada.marcin.screen.ui.ShipView;
import com.badlogic.gdx.utils.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
First screen with game set up, arranging the ships
Then this screen
3. will have to come up with the idea of persisting position of the ships between the screen
 */
public class Board {
    //if shot torpedo was unsuccessful   then this variable is null;
    private Ship lastDamagedShip;

    // /
    private Map<Coordinate, CellContent> board = new HashMap<Coordinate, CellContent>();
//    private final List<Ship> ships = new ArrayList<Ship>();

    public static final Logger logger = new Logger(Board.class.getName(),
            Logger.DEBUG);

    //amount of columns and rows in a  board
    private int columns;
    private int rows;


    public Board(int columns,int rows) {

        this.rows = rows;
        this.columns = columns;
logger.debug("rows:"+this.rows+" columns:"+this.columns);
    }

    //   coordinates  look like that  "10-2" where 10 is a row and 2 is a column
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

            if (isCellOcupied(yMin,
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

            if (isCellOcupied(yMin,
                    yMax,
                    xMin,
                    xMax)) return false;
        }
        return true;

    }

    private boolean isCellOcupied(int yMin,
                                  int yMax,
                                  int xMin,
                                  int xMax) {
        for (int yy = yMin; yy <= yMax; yy++) {
            logger.debug("\n");
            for (int xx = xMin; xx <= xMax; xx++) {
                if (board.containsKey(new Coordinate(xx, yy))) {
                    return true;
                }
                //logger.debug(xx+"-"+yy);
            }
        }
        return false;
    }


    //   CellContent checkTorpedoShot(String target)  {
//        CellContent cellContent = board.get(target);
//        if (cellContent == null) {
//            this.lastDamagedShip=null;
//            return null;
//        }
//        cellContent.setDamaged(true);
//        this.lastDamagedShip=this.getShip(cellContent.getShipType());
//        if(this.lastDamagedShip==null)  return null ;
//        this.lastDamagedShip.damageShip(cellContent.getIndexOfTheShip());
//        return cellContent;

    //   }

    boolean isShipSunk() {
        if (this.lastDamagedShip != null) return this.lastDamagedShip.isShipSunk();
        return false;
    }

    public boolean placeShipOnTheGrid(ShipView shipView) {
        if (!isPlacementAllowed(shipView.getCoordinates())) {
            logger.debug("Placement not allowed");
            return false;
        }
        int index = shipView.getLength() - 1;
        for (Coordinate coordinate : shipView.getCoordinates()) {

            board.put(coordinate,
                    new CellContent(shipView.getShipType(),
                            index,
                            false));
            index--;

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
}
