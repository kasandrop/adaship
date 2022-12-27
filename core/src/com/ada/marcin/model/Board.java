package com.ada.marcin.model;

import com.ada.marcin.common.ShipByTypeNotFoundException;

import java.util.ArrayList;
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
    private Map<String, CellContent> board = new HashMap<String, CellContent>();
    private final List<Ship> ships = new ArrayList<Ship>();

    private Ship patrolBoat;
    private Ship submarine;
    private Ship destroyer;
    private Ship battleship;
    private Ship carrier;


    public Board() {
        patrolBoat = new Ship(ShipType.PatrolBoat, 2, Direction.Horizontal);
        submarine = new Ship(ShipType.Submarine, 3, Direction.Horizontal);
        destroyer = new Ship(ShipType.Destroyer, 3, Direction.Horizontal);
        battleship = new Ship(ShipType.Battleship, 4, Direction.Horizontal);
        carrier = new Ship(ShipType.Carrier, 5, Direction.Horizontal);
        ships.add(patrolBoat);
        ships.add(submarine);
        ships.add(destroyer);
        ships.add(battleship);
        ships.add(carrier);
        init();

    }
    public  List<Ship> getAllShips(){
        return this.ships;
    }

    Ship getShip(ShipType shiptype)   {
        for (Ship ship : this.ships) {
            if (ship.getShip() == shiptype) {
                return ship;
            }
        }
       return null;
    }

    private void init() {
        for (Ship ship : ships) {
            List<String> keys = ship.getKeys();
            int index = 0;
            for (String key : keys) {
                CellContent cellContent = new CellContent(ship.getShip(), index, false);
                board.putIfAbsent(key, cellContent);
                index++;
            }
        }

    }

    CellContent checkTorpedoShot(String target)  {
        CellContent cellContent = board.get(target);
        if (cellContent == null) {
            this.lastDamagedShip=null;
            return null;
        }
        cellContent.setDamaged(true);
        this.lastDamagedShip=this.getShip(cellContent.getShipType());
        if(this.lastDamagedShip==null)  return null ;
        this.lastDamagedShip.damageShip(cellContent.getIndexOfTheShip());
        return cellContent;

    }

    boolean isShipSunk()  {
        if(this.lastDamagedShip!=null)  return this.lastDamagedShip.isShipSunk() ;
        return false;
    }
}
