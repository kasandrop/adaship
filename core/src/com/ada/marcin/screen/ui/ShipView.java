package com.ada.marcin.screen.ui;

import com.ada.marcin.model.Coordinate;
import com.ada.marcin.model.Direction;
import com.ada.marcin.screen.menu.OptionsScreen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.ArrayList;
import java.util.List;

public class ShipView extends Container<HorizontalGroup> {
    private int length;

    //avoiding creation of these  objects just by caching one. GC is time-consuming;
    private static Vector2 vector2 = new Vector2();

    private String name;
    private boolean isOnTheBoard;

    private final ArrayList<Coordinate> coordinates = new ArrayList<>();

    private HorizontalGroup horizontalGroup;

    private Direction direction;
    private TextureRegion regionNotDamaged;
    private TextureRegion regionDamaged;

    private TextureRegion regionReady;

    private int boatIDX;
    public static final Logger logger = new Logger(OptionsScreen.class.getName(),
            Logger.DEBUG);

    //shipview when was extended directly from Horizontalgroup it did not rotate properly

    /**
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
                    TextureRegion regionReady) {
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
        //height of the group sum of the row heights

        init();
    }
    //private Direction direction;

    private void init() {
        for (int i = 0; i < this.length; i++) {
            UnitActor shipUnit = new ShipUnit(regionDamaged,
                    regionNotDamaged,
                    regionReady);
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

    public HorizontalGroup getHorizontalGroup() {
        return horizontalGroup;
    }


    /*
    when dropping a ship on the layout function it is going to be nicely align to the grid, thanks to that function
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

    /**
     * Set texture of the Ship  as green. Indication that a ship is   placed properly in the grid.
     */
    public void makeShipReady() {
        this.isOnTheBoard = true;
        SnapshotArray<Actor> children = this.horizontalGroup.getChildren();
        //libgdx's SnapshotArray can be modified during iteration
        for (Actor actor : this.horizontalGroup.getChildren()) {
            ShipUnit shipUnit = (ShipUnit) actor;
            shipUnit.showAsReady();
        }
    }

    /**
     * Resets texture of the Ship. Indication that a ship is not placed in the grid.
     */
    public void makeShipTrain() {
        this.isOnTheBoard = false;
        //  SnapshotArray<Actor>children= this.horizontalGroup.getChildren();
        //libgdx's SnapshotArray can be modified during iteration
        for (Actor actor : this.horizontalGroup.getChildren()) {
            ShipUnit shipUnit = (ShipUnit) actor;
            shipUnit.showAsTraining();
        }
        this.deleteCoordinates();
    }

    /**
     * @return List of  UnitActors (( ship is build of unitActors and also grid is built of unitActors  )
     */


    public SnapshotArray<Actor> getShipUnits() {
        return this.horizontalGroup.getChildren();

    }


//    public void setOriginOfTheUnitActor() {
//        for (Actor unitActor : this.horizontalGroup.getChildren()) {
//            unitActor.setOrigin(unitActor.getX() + (float) coco thank you
//            4040/ 2, (float) unitActor.getY() + (float) GameConfig.CELL_SIZE / 2);
//        }
//    }


    //i rotate horizontalGroup inside my container.
    @Override
    public void rotateBy(float amountInDegrees) {

        //together with rotation first child becomes the last one and vice versa
        //to avoid that i dont go over 90 or - 90 degrees in rotation
        //It is important feature when choosing a GridUnit from a Grid

        if (this.getRotation() == 90) {
            amountInDegrees = -90;
        }

        this.direction = (Direction.Horizontal == this.direction) ? Direction.Vertical : Direction.Horizontal;
        this.horizontalGroup.rotateBy(amountInDegrees);
    }
    //to get rotation i need to get rotation of horizontalGroup


    @Override
    public float getRotation() {
        return this.horizontalGroup.getRotation();
    }

    public int getLength() {
        return length;
    }

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

    public List<Coordinate> getCoordinates() {
        return this.coordinates;
    }

    private void deleteCoordinates() {
        this.coordinates.clear();
    }

    //for testing
    public String printPoints() {
        return this.coordinates.toString();
    }


}
