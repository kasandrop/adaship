package com.ada.marcin.model;

import com.ada.marcin.config.GameConfig;

public class Player {

    public boolean isAi;
    private String name;
    private Board shipBoard;
    private Board targetBoard;
    private PlayerSetup playerSetup;
    private int numberOfUnitsToSink;
    private int[] shipLength;


    public Player(PlayerSetup playerSetup) {
        this.playerSetup = playerSetup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(Board shipBoard) {
        this.shipBoard = shipBoard;
    }

    /**
     *
     * @param shipLength array of int elements, index of the array represents the id of the ship (boatIdx)
     *                   and element is a length of the ship.
     */
    public void setShipLength(int[] shipLength) {
        this.shipLength = shipLength;
        for (int i = 0; i < GameConfig.getInstance().getBoats().size(); i++) {
            this.numberOfUnitsToSink += this.shipLength[i];
        }
    }

    /**
     *
     * @param boatIdx   unique identifier
     * @return true if the  player managed to sink  the opponent ship
     */
    public boolean isShipSunk(int boatIdx) {
        if (this.shipLength[boatIdx] == 0) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return  true when player successfully sunk all opponent's ships
     */
    public boolean isPlayerAWinner() {
        return this.numberOfUnitsToSink == 0;
    }

    /**
     * @param coordinate
     * @return -1 if missed or if hit a boatIdx  of the ship
     */
    public int torpedoOpponent(Coordinate coordinate) {
        return shipBoard.hit(coordinate);
    }

    /**
     *
     * @param coordinate
     * @param boatIdx thanks to it I keep track of  me enemy fleet.
     *               Subtracting one unit when player torpedoed opponent successfully
     */
    public void saveMyTorpedoResult(Coordinate coordinate, int boatIdx) {
        if (boatIdx != -1) {
            this.shipLength[boatIdx]--;
            this.numberOfUnitsToSink--;
        }
        this.targetBoard.save(coordinate, boatIdx);
    }

    public PlayerSetup getPlayerSetup() {
        return playerSetup;
    }

    public void setPlayerSetup(PlayerSetup playerSetup) {
        this.playerSetup = playerSetup;
    }


    public void resetPlayer() {
        this.playerSetup = PlayerSetup.Awaits;

       if( null!=this.shipBoard){
           this.shipBoard.clear();
       }
       if(null!=this.targetBoard){
           this.targetBoard.clear();
       }

    }

    public Board getTargetBoard() {
        return targetBoard;
    }

    public void setTargetBoard(Board board) {
        this.targetBoard = board;
    }

    public void setAi(boolean isAi) {

        this.isAi = isAi;
    }
}
