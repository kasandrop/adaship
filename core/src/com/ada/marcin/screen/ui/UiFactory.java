package com.ada.marcin.screen.ui;

import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.assets.RegionNames;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
