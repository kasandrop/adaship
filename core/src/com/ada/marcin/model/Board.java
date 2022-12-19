package com.ada.marcin.model;

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

    private Map<String,Boolean> board=new HashMap<String, Boolean>();
    private List<Ship> ships=new ArrayList<Ship>() ;

    private Ship patrolBoat;
    private Ship submarine;
    private Ship destroyer;
    private Ship battleship;
    private Ship carrier;

    public Board() {
        patrolBoat = new Ship("Patrol Boat", 2, Direction.West_East);
        submarine = new Ship("Submarine", 3, Direction.West_East);
        destroyer = new Ship("Destroyer", 3, Direction.West_East);
        battleship = new Ship("Battleship", 4, Direction.West_East);
        carrier = new Ship("Carrier", 5, Direction.West_East);
        ships.add(patrolBoat);
        ships.add(submarine);
        ships.add(destroyer);
        ships.add(battleship);
        ships.add(carrier);
        init();

    }

    private void init() {
        for (Ship ship :ships) {
            List<String> keys = ship.getKeys();
            for (String key : keys) {
                board.putIfAbsent(key, true);
            }
        }

    }
}
