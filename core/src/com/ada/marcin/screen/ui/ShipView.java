package com.ada.marcin.screen.ui;

import com.ada.marcin.model.Coordinate;
import com.ada.marcin.model.Direction;
import com.ada.marcin.model.ShipEvent;
import com.ada.marcin.model.ShipStatus;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ShipView extends Container<HorizontalGroup> {
    public static final Logger logger = new Logger(ShipView.class.getName(),
            Logger.DEBUG);
    //avoiding creation of these  objects just by caching one. GC is time-consuming;
    private static final Vector2 vector2 = new Vector2();
    private final ArrayList<Coordinate> coordinates = new ArrayList<>();
    private int length;
    private List<Observer> observers = new ArrayList<>();
    private String name;
    private HorizontalGroup horizontalGroup;
    private Direction direction;
    private TextureRegion regionNotDamaged;
    private TextureRegion regionDamaged;
    private TextureRegion regionReady;
    private int boatIDX;
    private int damage;
    private int cellSize;
    private ShipStatus shipStatus;

    /**
     * ShipView when was extended directly from HorizontalGroup it did not rotate properly
     *
     * @param boatIDX          type of the ship
     * @param length           length of the ship
     * @param name             if the ship was placed successfully on the grid
     * @param direction        Horizontal or Vertical
     * @param regionDamaged    how to display damaged Unit of the ship
     * @param regionNotDamaged how to display ship
     * @param regionReady      how to display ship after it was placed successfully on the grid
     */
    public ShipView(int boatIDX,
                    int length,
                    String name,
                    Direction direction,
                    TextureRegion regionDamaged,
                    TextureRegion regionNotDamaged,
                    TextureRegion regionReady,
                    int cellSize) {
        super();
        this.horizontalGroup = new HorizontalGroup();
        this.horizontalGroup.space(2f);
        this.setTouchable(Touchable.childrenOnly);
        this.setTransform(true);
        this.boatIDX = boatIDX;
        this.length = length;
        this.name = name;
        this.direction = direction;
        this.regionNotDamaged = regionNotDamaged;
        this.regionDamaged = regionDamaged;
        this.regionReady = regionReady;
        this.damage = 0;
        this.shipStatus = ShipStatus.Training;
        this.cellSize = cellSize;
        //height of the group sum of the row heights
        init();
    }


    private void init() {
        for (int i = 0; i < this.length; i++) {
            UnitActor shipUnit = new ShipUnit(regionDamaged,
                    regionNotDamaged,
                    regionReady,
                    cellSize);
            this.horizontalGroup.addActor(shipUnit);
        }
        this.horizontalGroup.setDebug(true);
        this.horizontalGroup.pack();
        this.setActor(this.horizontalGroup);
        this.setBounds(0,
                0,
                this.horizontalGroup.getWidth(),
                this.horizontalGroup.getHeight());
        this.horizontalGroup.setOrigin(this.horizontalGroup.getWidth() / 2,
                this.horizontalGroup.getHeight() / 2);
    }

    public ShipStatus getShipStatus() {
        return this.shipStatus;
    }


    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }



    /*
     * when dropping a ship on the layout , it is going to be nicely align to the grid, thanks to that function
     * @param x current x coordinate
     * @param y  current y coordinate
     */
    public void setPositionAlign(float x,
                                 float y) {
        if (direction == Direction.Horizontal) {
            float positionY = y - (((float) this.length - 1) / 2) * (2 + this.horizontalGroup.getChild(0)
                    .getHeight());
            super.setPosition(x - 1,
                    positionY - 1);
        } else {
            float positionX = x - (((float) this.length - 1) / 2) * (2 + this.horizontalGroup.getChild(0)
                    .getWidth());
            super.setPosition(positionX - 1,
                    y - 1);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Set texture of the Ship  as green. Indication that a ship is   placed properly in the grid.
     */
    public void deployShip() {
        this.shipStatus = ShipStatus.Deployed;
        this.notifyObservers(ShipEvent.Deployment,
                this.getCoordinatesAsString());
        //libgdx's SnapshotArray can be modified during iteration
        for (Actor actor : this.horizontalGroup.getChildren()) {
            ShipUnit shipUnit = (ShipUnit) actor;
            shipUnit.showAsDeployed();
        }
    }

    /**
     * Resets texture of the Ship. Indication that a ship is not placed in the grid.
     */
    public void trainShip() {
        //  logger.debug("trainShip");
        if (this.coordinates.size() > 0) {
            this.deleteCoordinates();
            this.notifyObservers(ShipEvent.Training,
                    this.getCoordinatesAsString());
        }

        this.shipStatus = ShipStatus.Training;

        //libgdx's SnapshotArray can be modified during iteration
        for (Actor actor : this.horizontalGroup.getChildren()) {
            ShipUnit shipUnit = (ShipUnit) actor;
            shipUnit.showAsTraining();
        }
    }


    private void notifyObservers(ShipEvent shipEvent,
                                 String data) {
        for (Observer observer : this.observers) {
            observer.notify(shipEvent,
                    data);
        }
    }

    /**
     * @return UnitActors (( ship is build of unitActors and also grid is built of unitActors  )
     */
    public SnapshotArray<Actor> getShipUnits() {
        return this.horizontalGroup.getChildren();
    }


    //i rotate horizontalGroup inside my container.
    @Override
    public void rotateBy(float amountInDegrees) {
        if (this.getRotation() == 90) {
            amountInDegrees = -90;
        }

        this.direction = (Direction.Horizontal == this.direction) ? Direction.Vertical : Direction.Horizontal;
        this.horizontalGroup.rotateBy(amountInDegrees);
    }

    /**
     * @return //The rotation of the visual is the rotation   of  the horizontalGroup
     */
    @Override
    public float getRotation() {
        return this.horizontalGroup.getRotation();
    }

    public int getLength() {
        return length;
    }

    /**
     * @return unique id of the visual
     */
    public int getShipType() {
        return boatIDX;
    }

    public Direction getDirection() {
        return direction;
    }

    //-------------------------------------model------------------------------------------------
    public void addCoordinate(Coordinate coordinate) {
        this.coordinates.add(coordinate);
    }

    /**
     * Randomly chooses direction of the shipView
     */
    public void randomizeDirection() {
        Direction randomDirection;
        //randomNumber  is goint o be 1 or 2
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 3);
        if (randomNumber == 1) {
            randomDirection = Direction.Horizontal;
        } else {
            randomDirection = Direction.Vertical;
        }
        if (this.getDirection() != randomDirection) {
            this.rotateBy(90);
        }
    }

    /**
     * Function    calculates Coordinates for all ShipUnits.
     *
     * @param coordinate ShipView Coordinate. Indicates the unit position on the grid.
     */
    public void addCoordinatesAuto(Coordinate coordinate) {
        //logger.debug();
        deleteCoordinates();
        for (int i = 0; i < this.length; i++) {
            Coordinate newCoordinate;
            if (this.direction == Direction.Horizontal) {

                newCoordinate = new Coordinate(coordinate.getX() + i, coordinate.getY());
            } else {
                newCoordinate = new Coordinate(coordinate.getX(), coordinate.getY() - i);
            }
            this.coordinates.add(newCoordinate);
        }
    }

    public List<Coordinate> getCoordinates() {
        return Collections.unmodifiableList(this.coordinates);
    }

    public void deleteCoordinates() {
        this.coordinates.clear();
    }

    /**
     * @return Print friendly format of all Coordinates
     */
    public String getCoordinatesAsString() {
        String temp = "";
        for (Coordinate coordinate : this.coordinates) {
            temp = temp + " " + coordinate;
        }
        return temp;
    }
}
