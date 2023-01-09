package com.ada.marcin.config;


import com.ada.marcin.model.Boat;
import com.ada.marcin.screen.loading.LoadingScreen;
import com.badlogic.gdx.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameConfig {
    private static GameConfig instance = null;
    public static final Logger logger = new Logger(GameConfig.class.getName(),
            Logger.DEBUG);

    private int boardWidth;
    private int boardHeight;
    private final List<Boat> boats = new ArrayList<>();


    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    public static GameConfig getInstance(int boardWidth,
                                         int boardHeight) {
        if (instance == null) {
            instance = new GameConfig(boardWidth,
                    boardHeight);
        }
        return instance;
    }

    private GameConfig() {
        this(GameConfig.sizeX,
                GameConfig.sizeY);
    }

    private GameConfig(int boardWidth,
                       int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        logger.debug("width:"+this.getBoardWidth()+" board height:"+this.getBoardHeight());
    }

    public void registerBoat(Boat boat) {
        this.boats.add(boat);
    }

    public int getCountOfBoats() {
        return this.boats.size();
    }

    public List<Boat> getBoats() {
        return Collections.unmodifiableList(this.boats);
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public static final int CELL_SIZE = 24;

    //amount of columns in a board
    public static final int sizeX = 10;
    //amount of rows in a board
    public static final int sizeY = 10;
    public static final float HUD_WIDTH = 1200f; // world units
    public static final float HUD_HEIGHT = 800f; // world units

    public static final int WINDOWS_WIDTH = 1200; // world units
    public static final int WINDOWS_HEIGHT = 800; // world units


}
