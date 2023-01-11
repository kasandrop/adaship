package com.ada.marcin.screen.ui;

import com.ada.marcin.config.GameConfig;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;


public class UnitActor extends Actor {
    protected int cellSize;

    public static final Logger logger = new Logger(UnitActor.class.getName(),
            Logger.DEBUG);

    protected TextureRegion regionCurrent;



    public UnitActor(int cellSize) {
        this.cellSize=cellSize;
        init();
    }

    private void init() {
        setSize(this.cellSize,this.cellSize);
        setOrigin(Align.center);
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


/*



public class SeaCell extends Actor {

    public static final Logger logger = new Logger(SeaCell.class.getName(), Logger.DEBUG);
    private int coordinateX;
    private int coordinateY;
    private TextureRegion regionNotDamaged;
    private TextureRegion regionDamaged;
    private TextureRegion regionCurrent;



    public SeaCell(TextureRegion regionNotDamaged,TextureRegion regionDamaged,int coordinateX,int coordinateY){
        this.coordinateY=coordinateY;
        this.coordinateX=coordinateX;
        this.regionNotDamaged=regionNotDamaged;
        this.regionDamaged=regionDamaged;
        this.regionCurrent =regionNotDamaged;
        init();

    }
    public SeaCell(TextureRegion regionNotDamaged,TextureRegion regionDamaged){
        this.coordinateY=coordinateY;
        this.coordinateX=coordinateX;
        this.regionNotDamaged=regionNotDamaged;
        this.regionDamaged=regionDamaged;
        this.regionCurrent =regionNotDamaged;
        init();

    }

private void init(){
    setSize(GameConfig.CELL_SIZE, GameConfig.CELL_SIZE);
    //this listener only  for testing purpose, in production  must be removed
    addListener(new InputListener() {

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            super.touchDragged(event, x, y, pointer);
        }

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            float yy = event.getTarget().getX();
            float xx = event.getTarget().getY();
            logger.debug("x===" + xx + "  " + "y===" + yy);
            return true;
        }
    });
}
    public void setCoordinates(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getCoordinateX() {
        return this.coordinateX;
    }

    public int getCoordinateY() {
        return this.coordinateY;
    }

    public void setFire() {
        this.regionCurrent = regionDamaged;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // logger.debug("draw   method()");
        if (this.regionCurrent == null) {
            return;
        }

        batch.draw(this.regionCurrent,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation()
        );
    }

}

 */
