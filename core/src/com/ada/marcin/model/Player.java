package com.ada.marcin.model;

import com.ada.marcin.config.GameConfig;
import com.badlogic.gdx.utils.Array;

public class Player {

    private String name;


    private Board shipBoard;
    private Board targetBoard;

    private PlayerSetup  playerSetup;

    private   int numberOfUnitsToSink;

    private int[] shipLength;

    public  boolean isAi;



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

    public void setShipLength(int[] shipLength) {
        this.shipLength = shipLength;
        for(int i=0;i< GameConfig.getInstance().getBoats().size();i++){
            this.numberOfUnitsToSink+=this.shipLength[i];
        }
    }
    public boolean  isShipSunk(int boatIdx){
        if (this.shipLength[boatIdx]==0){
            return true;
        }
        return false;
    }
    public boolean isPlayerAWinner(){
        if (this.numberOfUnitsToSink==0){
            return true;
        }
        return false;
    }

    /**
     *
     * @param coordinate
     * @return  -1 if missed or if hit a boatIdx
     */
    public int torpedoOpponent(Coordinate coordinate){
      return shipBoard.hit(coordinate);

    }

    public void saveMyTorpedoResult(Coordinate coordinate,int boatIdx){
        if(boatIdx!=-1){
            this.shipLength[boatIdx]--;
            this.numberOfUnitsToSink--;
        }
        this.targetBoard.save(coordinate,boatIdx);
    }

    public void setShipBoard(Board shipBoard) {
        this.shipBoard = shipBoard;
    }

    public PlayerSetup getPlayerSetup() {
        return playerSetup;
    }

    public void setPlayerSetup(PlayerSetup playerSetup) {
        this.playerSetup = playerSetup;
    }


    public void resetPlayer() {
        this.playerSetup = PlayerSetup.Awaits;
        this.name = "";
        this.shipBoard.clear();
        this.targetBoard.clear();
    }

    public void setTargetBoard(Board board) {
        this.targetBoard = board;
    }

    public Board getTargetBoard() {
        return targetBoard;
    }

    public void setAi(boolean isAi) {
        this.isAi=isAi;
    }


}
