package com.ada.marcin.screen.ui;

import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.Direction;
import com.ada.marcin.model.ShipType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Align;

public class ShipView extends Container<HorizontalGroup> {
    private int length;
    private boolean isOnTheBoard;

    private HorizontalGroup horizontalGroup;

    private Direction direction;
    private TextureRegion regionNotDamaged;
    private TextureRegion regionDamaged;

    private ShipType shipType;



    //shipview when was extended directly from Horizontalgroup it did not rotate properly
    public ShipView(ShipType shipType,int length, boolean isOnTheBoard, Direction direction, TextureRegion regionDamaged, TextureRegion regionNotDamaged) {
        super();
        this.horizontalGroup=new HorizontalGroup();
        this.horizontalGroup.space(2f);
        this.setTransform(true);
        this.shipType=shipType;
        this.length = length;
        this.isOnTheBoard = isOnTheBoard;
        this.direction = direction;
        this.regionNotDamaged=regionNotDamaged;
        this.regionDamaged=regionDamaged;
        //height of the group sum of the row heights
        this.horizontalGroup.wrap(false);
        init();
    }
    //private Direction direction;

    private void init() {
        for (int i = 0; i < this.length; i++) {
            UnitActor unitActor = new UnitActor(regionDamaged, regionNotDamaged);

            this.horizontalGroup.addActor(unitActor);
        }

        this.horizontalGroup.setDebug(true);
        this.horizontalGroup.pack();

        this.setActor(this.horizontalGroup);
        this.horizontalGroup.setOrigin(this.horizontalGroup.getWidth()/2,
                this.horizontalGroup.getHeight()/2);

    }

    @Override
    public void rotateBy(float amountInDegrees) {
        this.horizontalGroup.rotateBy(amountInDegrees);
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

    public void changeDirection() {
        this.direction = (this.direction==Direction.Horizontal)?Direction.Vertical:Direction.Horizontal;
    }
}
