package com.ada.marcin.screen.ui;

import com.ada.marcin.screen.menu.OptionsScreen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Logger;

public class SeaCell extends Actor {

   private int cordinateX;
    private int  cordinateY;
    public static final Logger logger = new Logger(SeaCell.class.getName(), Logger.DEBUG);
    public SeaCell(TextureRegion region,int y,int x) {
        this.region = region;
        this.cordinateX=x;
        this.cordinateY=y;
        setSize(32,32);
        addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
               int yy= ((SeaCell)event.getTarget()).cordinateY ;
                int xx= ((SeaCell)event.getTarget()).cordinateX ;
                logger.debug("x==="+xx +   "  "+"y==="+yy);
                return true;
            }
        });
    }

    private  TextureRegion region;

  //  public void setRegion(TextureRegion region) {
  //      this.region = region;
  //  }

    @Override
    public void draw(Batch batch, float parentAlpha) {
       // logger.debug("draw   method()");
        if (this.region == null) {
            return;
        }

        batch.draw(region,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation()
        );
    }

}
