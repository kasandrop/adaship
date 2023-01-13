package com.ada.marcin.screen.ui;

import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.assets.RegionNames;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.Coordinate;
import com.ada.marcin.model.Direction;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class UiFactory {

    public static final Logger logger = new Logger(UiFactory.class.getName(),
            Logger.DEBUG);

    private Skin skin;
    private final AssetManager assetManager;
    private final TextureAtlas gamePlayAtlas;
    private TextureAtlas atlas;
    private TextureRegion backgroundRegion;
    private TextureRegion panelRegion;
    private static Vector2 vector2 = new Vector2();

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
                         int cellsize,
                         Touchable touchable) {
        Table table = new Table(skin);
        for (int i = 0; i <= sizeY; i++) {
            table.row();
            for (int j = 0; j <= sizeX; j++) {


                if (i == 0 && j != 0) {
                    Label label = new Label(Coordinate.columnLabel(j),
                            skin,"small");
                    table.add(label);
                } else if (j == 0 && i != 0) {

                    String number = String.valueOf(i);
                    Label label = new Label(number,
                            skin,
                            "small");
                    table.add(label);
                } else {
                    final GridUnit gridUnit = new GridUnit(getGridViewTexture(),
                            j,
                            i,cellsize);
                    gridUnit.setTouchable(touchable);

                    gridUnit.setName(new Coordinate(j,i).toString());
                    table.add(gridUnit)
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
        Table buttonTable = new Table(skin);
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


    public  Table getButtons(List<TextButton> buttons) {
        Table table = new Table();
        for(TextButton textButton :buttons){
            table.add(textButton).fill().pad(10);
            table.row();
        }

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

    public TextureRegion getTextureRegion(Color color) {
        Pixmap pixmapShip = new Pixmap(1,
                1,
                Pixmap.Format.RGBA8888);
        pixmapShip.setColor(color);
        pixmapShip.fill();
        return new TextureRegion(new Texture(pixmapShip));

    }

    /**
     * @param nameOfTable the name of the   table,which contains the GridUnit
     * @param stage current stage
     * @return randomly returns a cell from the table
     */
    public GridUnit getGridUnit(String nameOfTable, Stage stage) {
        int xxRandom;
        int yyRandom;
        boolean isGridUnit = false;
        Actor randomActor;
        Table targetBoard = stage.getRoot()
                .findActor(nameOfTable);
        float width = targetBoard.getWidth();
        float height = targetBoard.getHeight();
        vector2 = targetBoard.localToStageCoordinates(vector2.setZero());
        do {
            int xTemp = (int) vector2.x + 4 + GameConfig.CELL_SIZE;
            xxRandom = ThreadLocalRandom.current()
                    .nextInt(xTemp,
                            xTemp + 1 + (int) width);
            yyRandom = ThreadLocalRandom.current()
                    .nextInt((int) vector2.y + 4,
                            (int) vector2.y + (int) height - 2 - GameConfig.CELL_SIZE);
            randomActor = stage.hit(xxRandom,
                    yyRandom,
                    true);

            if (randomActor instanceof GridUnit) {
                isGridUnit = true;
            }

        } while (isGridUnit == false);

        return (GridUnit) randomActor;
    }

}
