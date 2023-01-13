package com.ada.marcin.screen.ui;

import com.ada.marcin.common.RegionTextureNotSetException;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Logger;

/**
 * Visual Representation of the ship. ShipView consists of ShipUnits
 */
public class ShipUnit extends UnitActor {
    public static final Logger logger = new Logger(ShipUnit.class.getName(),
            Logger.DEBUG);
    protected TextureRegion regionNotDamaged;
    protected TextureRegion regionDamaged;
    //when placed correctly inside a grid
    protected TextureRegion regionReady;

    public ShipUnit(TextureRegion regionDamaged,
                    TextureRegion regionNotDamaged,
                    TextureRegion regionReady,
                    int cellSize) {
        super(cellSize);
        this.regionNotDamaged = regionNotDamaged;
        this.regionDamaged = regionDamaged;
        this.regionCurrent = regionNotDamaged;
        this.regionReady = regionReady;
    }

    public void setFire() {
        if (this.regionDamaged == null && this.regionNotDamaged == null) {
            throw new RegionTextureNotSetException("TextureRegion is not set.");
        }
        this.regionCurrent = regionDamaged;
    }

    public void showAsDeployed() {
        if (this.regionReady == null) {
            throw new RegionTextureNotSetException("TextureRegion is not set.");
        }
        this.regionCurrent = regionReady;
    }


    public void showAsTraining() {
        if (this.regionCurrent == this.regionNotDamaged) {
            return;
        }
        if (this.regionNotDamaged == null) {
            throw new RegionTextureNotSetException("TextureRegion is not set.");
        }
        this.regionCurrent = regionNotDamaged;
    }
}
