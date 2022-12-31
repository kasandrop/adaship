package com.ada.marcin.screen.ui;

import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.Direction;
import com.ada.marcin.model.ShipType;
import com.ada.marcin.screen.menu.OptionsScreen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.ArrayList;

public class ShipView extends Container<HorizontalGroup> {
    private int length;

    //avoiding creation of these  objects just by caching one. GC is time-consuming;
    private static Vector2 vector2 = new Vector2();
    private boolean isOnTheBoard;

    private final ArrayList<String> points = new ArrayList<String>();

    private HorizontalGroup horizontalGroup;

    private Direction direction;
    private TextureRegion regionNotDamaged;
    private TextureRegion regionDamaged;

    private ShipType shipType;
    public static final Logger logger = new Logger(OptionsScreen.class.getName(), Logger.DEBUG);

    //shipview when was extended directly from Horizontalgroup it did not rotate properly
    public ShipView(ShipType shipType, int length, boolean isOnTheBoard, Direction direction, TextureRegion regionDamaged, TextureRegion regionNotDamaged) {
        super();
        this.horizontalGroup = new HorizontalGroup();
        this.horizontalGroup.space(2f);
        this.setTouchable(Touchable.childrenOnly);
        this.setTransform(true);
        this.shipType = shipType;
        this.length = length;
        this.isOnTheBoard = isOnTheBoard;
        this.direction = direction;
        this.regionNotDamaged = regionNotDamaged;
        this.regionDamaged = regionDamaged;
        //height of the group sum of the row heights


        init();
    }
    //private Direction direction;

    private void init() {
        for (int i = 0; i < this.length; i++) {
            UnitActor shipUnit = new ShipUnit(regionDamaged, regionNotDamaged);

            this.horizontalGroup.addActor(shipUnit);
        }


        this.horizontalGroup.setDebug(true);
        this.horizontalGroup.pack();

        this.setActor(this.horizontalGroup);
        this.setBounds(0, 0, this.horizontalGroup.getWidth(), this.horizontalGroup.getHeight());
        this.horizontalGroup.setOrigin(this.horizontalGroup.getWidth() / 2,
                this.horizontalGroup.getHeight() / 2);

    }

    public HorizontalGroup getHorizontalGroup() {
        return horizontalGroup;
    }



    /*
    when dropping a ship on the layout function it is going to be nicely align to the grid, thanks to that function
     */
    public void setPositionAlign(float x, float y) {
        if(direction==Direction.Horizontal){
            float positionY=y-(((float)this.length-1)/2)*this.horizontalGroup.getChild(0).getHeight();
            super.setPosition(x-2,positionY -4);
        }else{
            float positionX=x-(((float)this.length-1)/2)*this.horizontalGroup.getChild(0).getWidth();
            super.setPosition(positionX-4,y-2);
        }


    }

    /**
     *
     * @return List of  UnitActors (( ship is build of unitActors and also grid is built of unitActors  )
     *
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

        if(this.getRotation()==90){
            amountInDegrees=-90;
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

    public ShipType getShipType() {
        return shipType;
    }

    public Direction getDirection() {
        return direction;
    }

    //-------------------------------------model------------------------------------------------
    public void addPoints(String point){
        this.points.add(point);

    }
    public void deletePoints(){
        this.points.clear();
    }
    //for testing
    public String  printPoints(){
       return  this.points.toString();
    }


}
