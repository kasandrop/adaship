package com.ada.marcin.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;


    private Board  board;

    private PlayerSetup  playerSetup;


    private List<Coordinate> shipTarget=new ArrayList<>();

    public Player(PlayerSetup playerSetup) {
        this.playerSetup = playerSetup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public PlayerSetup getPlayerSetup() {
        return playerSetup;
    }

    public void setPlayerSetup(PlayerSetup playerSetup) {
        this.playerSetup = playerSetup;
    }

    public List<Coordinate> getShipTarget() {
        return shipTarget;
    }

    public void setShipTarget(List<Coordinate> shipTarget) {
        this.shipTarget = shipTarget;
    }


    public void resetPlayer(){
        this.playerSetup=PlayerSetup.Awaits;
        this.name="";
        this.board=null;
        this.shipTarget.clear();
    }
}
