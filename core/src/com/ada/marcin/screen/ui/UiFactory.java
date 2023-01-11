package com.ada.marcin.screen.ui;

import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.assets.RegionNames;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.Coordinate;
import com.ada.marcin.model.Direction;
import com.ada.marcin.screen.menu.MenuScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;

import java.util.List;
import java.util.Map;

public class UiFactory {

    public static final Logger logger = new Logger(UiFactory.class.getName(),
            Logger.DEBUG);

    private Skin skin;
    private final AssetManager assetManager;
    private final TextureAtlas gamePlayAtlas;
    private TextureAtlas atlas;
    private TextureRegion backgroundRegion;
    private TextureRegion panelRegion;

    public UiFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.gamePlayAtlas = assetManager.get(AssetsDescriptor.GAME_PLAY);
        this.backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        this.skin = assetManager.get(AssetsDescriptor.UISKIN);
    }


   public  ShipView shipViewFactory(int myIndex,
                             int length,
                             String name,
                             TextureRegion unitViewDamagedTexture,
                             TextureRegion unitViewTexture,
                             TextureRegion regionReady,
                                    int cellSize) {
        return new ShipView(myIndex,
                length,
                name,
                Direction.Horizontal,
                unitViewDamagedTexture,
                unitViewTexture,
                regionReady,cellSize);


    }

    public Table getGrid(int sizeX,
                          int sizeY,
                         int cellsize) {
        Table table = new Table(skin);
        for (int i = 0; i <= sizeY; i++) {
            table.row();
            for (int j = 0; j <= sizeX; j++) {

                if (i == 0 && j != 0) {
                    Label label = new Label(Coordinate.columnLabel(j),
                            skin);
                    table.add(label);
                } else if (j == 0 && i != 0) {

                    String number = String.valueOf(i);
                    Label label = new Label(number,
                            skin,
                            "default");

                    table.add(label);
                } else {
                    final GridUnit unitActor = new GridUnit(getGridViewTexture(),
                            j,
                            i,cellsize);
                    unitActor.setTouchable(Touchable.enabled);

                    table.add(unitActor)
                            .space(2);
                }

            }
        }
        return table;

    }

    public Table createTableForBackground() {

        Table table = new Table();
        table.setBackground(new TextureRegionDrawable(backgroundRegion));
        return table;
    }

    public Table createContainerForButtons() {
        Table buttonTable = new Table();
        buttonTable.defaults()
                .pad(20);
        buttonTable.center();
        return buttonTable;
    }

    TextureRegion getGridViewTexture() {
        Pixmap pixmap = new Pixmap(1,
                1,
                Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.SKY);
        pixmap.fill();
        return new TextureRegion(new Texture(pixmap));
    }



    public  Table getButtons(SaveButton saveButton,TextButton buttonReset,TextButton buttonAuto) {
        Table table = new Table();




        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event,
                                float x,
                                float y) {
                logger.debug("Save Button clicked");
            }
        });
        table.add(saveButton).fill().pad(10);
        table.row();
        table.add  (buttonAuto).fill().pad(10);
        table.row();
        table.add  (buttonReset).fill().pad(10);



        return table;

    }

    public VerticalGroup ContainerWithShipView(Map<Integer,ShipView> shipViews) {
        VerticalGroup verticalGroup = new VerticalGroup();
        for (Map.Entry<Integer, ShipView> entry : shipViews.entrySet()) {
            //System.out.println(entry.getKey() + ":" + entry.getValue());
            verticalGroup.addActor(getContainer(entry.getValue()));
        }
        verticalGroup.space(10);
        verticalGroup.pad(10);
        verticalGroup.pack();
        return verticalGroup;

    }
    public  Container<ShipView> getContainer(ShipView shipView) {
        Container<ShipView> container = new Container<>();
        container.background(getContainerBackground());
        container.size(GameConfig.CELL_SIZE * shipView.getLength() + shipView.getLength() * 2);
        container.setActor(shipView);
        return container;
    }
    private TextureRegionDrawable getContainerBackground() {
        Pixmap bgPixmap = new Pixmap(1,
                1,
                Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.BROWN);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }


    private TextureRegionDrawable getPanelBackground() {
        TextureRegion backgroundRegion = skin.getRegion(RegionNames.PANEL);
        return new TextureRegionDrawable(backgroundRegion);
    }
    public Table getInfoPanel(List<HUD> huds) {

        Table infoPanel = new Table();
        infoPanel.row();
        Table tbl = new Table(skin);
        tbl.add(new Label("ID",
                        skin))
                .width(50);
        tbl.add(new Label("Name",
                        skin))
                .width(100);
        tbl.add(new Label("Len.",
                        skin))
                .width(50);
        tbl.add(new Label("Dmg",
                        skin))
                .width(50);
        tbl.add(new Label("Status",
                        skin))
                .width(200);
        tbl.add(new Label("Coordinates:",
                        skin))
                .width(200);
        tbl.row();
        tbl.pad(1);
        infoPanel.add(tbl);
        infoPanel.row();
        for (HUD hud : huds) {
            infoPanel.add(hud);
            infoPanel.row();
        }
        infoPanel.setBackground(getPanelBackground());
        return infoPanel;
    }

    public TextureRegion getTextureRegion(Color color){
        Pixmap pixmapShip = new Pixmap(1,
                1,
                Pixmap.Format.RGBA8888);
        pixmapShip.setColor(color);
        pixmapShip.fill();
        return new TextureRegion(new Texture(pixmapShip));

    }
}
