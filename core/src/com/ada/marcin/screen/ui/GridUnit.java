package com.ada.marcin.screen.ui;

import com.ada.marcin.model.Coordinate;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Logger;

public class GridUnit extends UnitActor {


    public static final Logger logger = new Logger(GridUnit.class.getName(),
            Logger.DEBUG);
    private final Coordinate coordinate;
    private TextureRegion spareTexture;

    public GridUnit(TextureRegion textureRegion,
                    int x,
                    int y,
                    int cellSize) {
        super(cellSize);
        this.regionCurrent = textureRegion;
        this.coordinate = new Coordinate(x,
                y);
    }


    @Override
    public void setRegionCurrent(TextureRegion regionCurrent) {
        super.setRegionCurrent(regionCurrent);
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
