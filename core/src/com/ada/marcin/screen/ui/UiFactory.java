package com.ada.marcin.screen.ui;

import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.assets.RegionNames;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.Direction;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UiFactory {
    private final AssetManager assetManager;
    private final TextureAtlas gamePlayAtlas;
    private TextureAtlas atlas;
    private TextureRegion backgroundRegion;
    private TextureRegion panelRegion;

    public UiFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.gamePlayAtlas = assetManager.get(AssetsDescriptor.GAME_PLAY);
        this.backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
    }

//    public Table createShip(Direction direction, int length) {
//        Pixmap pixmap = new Pixmap(GameConfig.CELL_SIZE, GameConfig.CELL_SIZE, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.BROWN);
//        pixmap.fill();
//        TextureRegion region = new TextureRegion(new Texture(pixmap));
//        Table table = new Table();
//        table.pad(1);
//
//        for (int i = 0; i < length; i++) {
//            ShipUnit unitActor = new ShipUnit(region);
//            table.add(unitActor);
//            if (Direction.Vertical == direction) {
//                table.row();
//            }
//
//
//        }
//        return table;
//    }

//    public ShipView createShipView(){
//
//    }
//

    public Table createTableForBackground() {

        Table table = new Table();
        table.setBackground(new TextureRegionDrawable(backgroundRegion));
        return table;
    }

    public Table createContainerForButtons() {
        Table buttonTable = new Table();
        buttonTable.defaults().pad(20);
        buttonTable.center();
        return buttonTable;
    }


    //quite button
}
