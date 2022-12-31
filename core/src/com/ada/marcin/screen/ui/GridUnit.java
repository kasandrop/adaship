package com.ada.marcin.screen.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Logger;

public class GridUnit  extends UnitActor {

    private int row;
    private int column;

    public static final Logger logger = new Logger(GridUnit.class.getName(), Logger.DEBUG);
    public GridUnit(TextureRegion textureRegion,int row,int column){
        this.regionCurrent=textureRegion;
        this.row=row;
        this.column=column;

    }

    public void chengecolor() {
        this.regionCurrent=getUnitViewTexture();
    }

    TextureRegion getUnitViewTexture() {
        Pixmap pixmapShip = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapShip.setColor(Color.DARK_GRAY);
        pixmapShip.fill();
        return new TextureRegion(new Texture(pixmapShip));
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    public String getPosition(){
       return this.getRow()+"-"+getColumn();
    }
}
