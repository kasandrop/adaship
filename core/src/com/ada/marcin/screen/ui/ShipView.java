package com.ada.marcin.screen.ui;

import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.Direction;
import com.ada.marcin.model.ShipType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Align;

public class ShipView extends HorizontalGroup {
    private int length;
    private boolean isOnTheBoard;

    private Direction direction;
    private TextureRegion regionNotDamaged;
    private TextureRegion regionDamaged;

    private ShipType shipType;

    public ShipView(ShipType shipType,int length, boolean isOnTheBoard, Direction direction, TextureRegion regionNotDamaged, TextureRegion regionDamaged) {
        super();
        this.shipType=shipType;
        this.length = length;
        this.isOnTheBoard = isOnTheBoard;
        this.direction = direction;
        this.regionNotDamaged=regionNotDamaged;
        this.regionDamaged=regionDamaged;
        //height of the group sum of the row heights
        this.wrap(false);
        init();
    }
    //private Direction direction;

    private void init() {
        for (int i = 0; i < this.length; i++) {
            UnitActor unitActor = new UnitActor(regionDamaged, regionNotDamaged);

            this.addActor(unitActor);
        }

        this.setDebug(true);
        this.pack();
    //   this.setOrigin(this.getMaxWidth()/2,this.getMaxHeight()/2);

    }

    public int getLength() {
        return length;
    }

    public ShipType getShipType() {
        return shipType;
    }
}
