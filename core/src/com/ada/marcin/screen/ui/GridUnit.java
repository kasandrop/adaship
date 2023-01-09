package com.ada.marcin.screen.ui;

import com.ada.marcin.model.Coordinate;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Logger;

public class GridUnit extends UnitActor {


    private Coordinate coordinate;

    public static final Logger logger = new Logger(GridUnit.class.getName(),
            Logger.DEBUG);

    public GridUnit(TextureRegion textureRegion,
                    int x,
                    int y) {
        this.regionCurrent = textureRegion;
        this.coordinate = new Coordinate(x,
                y);

    }

    public void changeColor() {
        this.regionCurrent = getUnitViewTexture();
    }

    TextureRegion getUnitViewTexture() {
        Pixmap pixmapShip = new Pixmap(1,
                1,
                Pixmap.Format.RGBA8888);
        pixmapShip.setColor(Color.DARK_GRAY);
        pixmapShip.fill();
        return new TextureRegion(new Texture(pixmapShip));
    }

    public int getGridX() {

        return coordinate.getX();
    }

    public int getGridY() {
        return coordinate.getY();
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }
}
