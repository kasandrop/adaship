package com.ada.marcin.screen.ui;

import com.ada.marcin.config.GameConfig;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;

public class ShipUnit  extends  UnitActor {


    public static final Logger logger = new Logger(ShipUnit.class.getName(), Logger.DEBUG);

    public ShipUnit(TextureRegion regionDamaged, TextureRegion regionNotDamaged) {
        this.regionNotDamaged = regionNotDamaged;
        this.regionDamaged = regionDamaged;
        this.regionCurrent = regionNotDamaged;
    }

    public void setFire() {
        if(this.regionDamaged==null || this.regionNotDamaged==null){
            logger.error("TextureRegion is not set");
            return;
        }
        this.regionCurrent = regionDamaged;
    }


}
