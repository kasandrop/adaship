package com.ada.marcin.screen.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;

public class UnitActor extends Actor {

    public static final Logger logger = new Logger(UnitActor.class.getName(),

            Logger.DEBUG);
    protected int cellSize;
    protected TextureRegion regionCurrent;

    public UnitActor(int cellSize) {
        this.cellSize = cellSize;
        init();
    }

    private void init() {
        setSize(this.cellSize, this.cellSize);
        setOrigin(Align.center);
    }

    public void setRegionCurrent(TextureRegion regionCurrent) {
        this.regionCurrent = regionCurrent;
    }

    @Override
    public void draw(Batch batch,
                     float parentAlpha) {
        if (this.regionCurrent == null) {
            logger.error("TextureRegion is not set in UnitActor");
            return;
        }

        batch.draw(this.regionCurrent,
                getX(),
                getY(),
                getOriginX(),
                getOriginY(),
                getWidth(),
                getHeight(),
                getScaleX(),
                getScaleY(),
                getRotation()
        );
    }
}
