package com.ada.marcin.screen.menu;

import com.ada.marcin.AdashipGame;
import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.Board;
import com.ada.marcin.model.Direction;
import com.ada.marcin.model.ShipType;
import com.ada.marcin.screen.ui.GridUnit;
import com.ada.marcin.screen.ui.ShipUnit;
import com.ada.marcin.screen.ui.ShipView;
import com.ada.marcin.screen.ui.UnitActor;
import com.ada.marcin.util.GdxUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OptionsScreen extends ScreenAdapter {


    public static final Logger logger = new Logger(OptionsScreen.class.getName(), Logger.DEBUG);

    private final AdashipGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;

    private Board board;


    private static Vector2 vector2 = new Vector2();


    private Map<ShipType, ShipView> shipViews = new HashMap<ShipType, ShipView>();


    private UnitActor unitActor;
    private TextureRegion region;

    private Skin skin;


    public OptionsScreen(AdashipGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();

        this.skin = assetManager.get(AssetsDescriptor.UISKIN);

    }

    TextureRegion getGridViewTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.SKY);
        pixmap.fill();
        return new TextureRegion(new Texture(pixmap));
    }

    TextureRegion getUnitViewTexture() {
        Pixmap pixmapShip = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapShip.setColor(Color.DARK_GRAY);
        pixmapShip.fill();
        return new TextureRegion(new Texture(pixmapShip));
    }

    TextureRegion getUnitViewReadyTexture(){
        Pixmap pixmapShip = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapShip.setColor(Color.GREEN);
        pixmapShip.fill();
        return new TextureRegion(new Texture(pixmapShip));
    }

    TextureRegion getUnitViewDamagedTexture() {
        Pixmap pixmapShip = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapShip.setColor(Color.RED);
        pixmapShip.fill();
        return new TextureRegion(new Texture(pixmapShip));
    }

    @Override
    public void show() {
        board=new Board(GameConfig.sizeX,GameConfig.sizeY);
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);
        initShipsViews();
        initUi();
        //logger.debug("origin x,y="+shipView.getWidth()/2+"  "+shipView.getHeight()/2);
    }


    private void initUi() {

        Table table = new Table();
        //   table.setDebug(true);
        table.add(getGrid(10, 10));
        table.add(ContainerWithShipView());
        table.background(getWindowBackground());

        table.center();
        table.setFillParent(true);
        table.pack();
        table.setDebug(true);

        stage.addActor(table);
        for (Map.Entry<ShipType, ShipView> entry : this.shipViews.entrySet()) {
            //System.out.println(entry.getKey() + ":" + entry.getValue());
            final ShipView shipView = entry.getValue();


            shipView.addListener(new InputListener() {
                                     @Override
                                     public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        logger.debug("touchDown event  button:"+button);
                                         logger.debug("touchDown event  on ViewShip:"+shipView.getShipType().toString());
                                         shipView.setZIndex(1000000);

                                         if (button == Input.Buttons.RIGHT) {
                                             //  shipView.localToParentCoordinates(new Vector2(x,y));
                                             //  shipView.setOrigin(shipView.getWidth() / 2, shipView.getHeight() / 2);
                                             shipView.rotateBy(90);
                                             //logger.debug("Pointer:" + pointer + " button:" + button);
                                             //  logger.debug("1origin x,y=" +shipView.getWidth() / 2 + "  " +shipView.getHeight() / 2);
                                             return false;
                                         }

                                         board.removeShipFromTheGrid(shipView);
                                         shipView.makeShipTrain();
//  returning true means touchUp or drag is going to be handled
                                         return true;

                                     }


                                     //Ship is built of shipUnits   When user drops on the grid a ship event is raised :touchUp. That event is raised
                                     // on the ViewShip.  I calculate here a  middle point of each unitActor.  Carrier for example consists of 5 unitActors
                                     // and will have 5 middle points (These are  originX and originY built-in variable of the actor class)
                                     //Because all actors coordinates are relative to  its parent .  I translate them to be relative to the stage (root group)
                                     //Next, I am finding actors which my ship overlaps (To be precise the deepest actor in the stage hierarchy which contains
                                     // the middle point  (originX,originY)
                                     // These found actors   are  cells of the table which my ship overlaps.
                                     // I use them for 1) aligning a ship
                                     //              2) remembering a position on the board
                                     @Override
                                     public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                        logger.debug("touchUp event");
                                         SnapshotArray<Actor> actors = shipView.getShipUnits();
                                         ArrayList<GridUnit> gridUnits = new ArrayList<>();
                                         Stage myStage = event.getStage();
                                         for (Actor actor : actors) {
                                             vector2.setZero();
                                             if (!(actor instanceof ShipUnit)) continue;

                                             //Scene2d all points are local that is relative to actor's parent
                                             //changing to stage coordinates
                                             vector2 = actor.localToStageCoordinates(vector2.set(actor.getOriginX(), actor.getOriginY()));

                                             //shipView is going to be hit that is why i made it untouchable
                                             shipView.setTouchable(Touchable.disabled);

                                             Actor newActor = myStage.hit(vector2.x, vector2.y, true);
                                             shipView.setTouchable(Touchable.childrenOnly);
                                             if (newActor instanceof GridUnit) {
                                                 gridUnits.add((GridUnit) newActor);
                                             }


                                         }


                                         //the amount of cells must be equal to the length of the shipView
                                         //if it is not it means the shipView   placed on the grid only partially

                                         if (gridUnits.size() != shipView.getLength()) {
                                             return;
                                         }
                                         for (GridUnit gridUnit : gridUnits) {

                                             if (gridUnit == null) {
                                                 // logger.debug("actor is null");
                                                 return;
                                             }

                                             //  logger.debug(gridUnit.toString());
                                             //for testing purposes i mark grid which contains the ship
                                             //  ((GridUnit) gridUnit).chengecolor();

                                             shipView.addCoordinate(gridUnit.getCoordinate());

                                         }
                                         //aligning  a ship  (converting stage position of the ship to a local one.
                                         //Actor position is  relative to its parent.

                                         GridUnit firstGridUnit;

                                         //logger.debug("Direction:"+shipView.getDirection().toString()+"POSITION:"+shipView.getHorizontalGroup().getX()+" y:"+shipView.getHorizontalGroup().getY());
                                         //together with rotation first child becomes the last one and vice versa
//                                         if (shipView.getRotation()%180.0f>1){
//                                                firstGridUnit=gridUnits.get(shipView.getLength()-1);
//                                         }else{
                                         firstGridUnit = gridUnits.get(0);
//                                         }
                                         logger.debug("Rotation:" + shipView.getRotation());


                                         vector2 = firstGridUnit.localToStageCoordinates(vector2.setZero());
                                         event.getStage().addActor(shipView);

                                         shipView.setPositionAlign(vector2.x, vector2.y);

                                         shipView.setZIndex(1000);
                                         logger.debug(shipView.printPoints());
                                         logger.debug("_______________");
                                         if(board.placeShipOnTheGrid(shipView)){
                                              shipView.makeShipReady();
                                         }

                                         //logger.debug("Coordinates:"+board.isPlacementAllowed(shipView.getPoints())+"");

                                     }

                                     @Override
                                     public void touchDragged(InputEvent event, float x, float y, int pointer) {
                                         //  logger.debug(vector2.set(x,y).toString());
                                         shipView.moveBy(x - shipView.getWidth() / 2, y - shipView.getHeight() / 2);
                                         shipView.localToParentCoordinates(new Vector2(x, y));

                                         // logger.debug("x:::" + shipView.getX());
                                         //  logger.debug("y:::" + shipView.getY());
                                     }
                                 }


            );
        }

    }

    //2nd column   of the main table
    private VerticalGroup ContainerWithShipView() {
        VerticalGroup verticalGroup = new VerticalGroup();
        for (Map.Entry<ShipType, ShipView> entry : this.shipViews.entrySet()) {
            //System.out.println(entry.getKey() + ":" + entry.getValue());
            verticalGroup.addActor(getContainer(entry.getValue()));
        }
        verticalGroup.space(10);
        verticalGroup.pad(10);
        verticalGroup.pack();
        return verticalGroup;

    }

    private void initShipsViews() {
        this.shipViews.put(ShipType.PatrolBoat, shipViewFactory(ShipType.PatrolBoat, 2, getUnitViewDamagedTexture(), getUnitViewTexture(),getUnitViewReadyTexture()));
        this.shipViews.put(ShipType.Submarine, shipViewFactory(ShipType.Submarine, 3, getUnitViewDamagedTexture(), getUnitViewTexture(),getUnitViewReadyTexture()));
        this.shipViews.put(ShipType.Destroyer, shipViewFactory(ShipType.Destroyer, 3, getUnitViewDamagedTexture(), getUnitViewTexture(),getUnitViewReadyTexture()));
        this.shipViews.put(ShipType.Battleship, shipViewFactory(ShipType.Battleship, 4, getUnitViewDamagedTexture(), getUnitViewTexture(),getUnitViewReadyTexture()));
        this.shipViews.put(ShipType.Carrier, shipViewFactory(ShipType.Carrier, 5, getUnitViewDamagedTexture(), getUnitViewTexture(),getUnitViewReadyTexture()));


    }

    //  the brown placeholder   where ships are placed
    private Container<ShipView> getContainer(ShipView shipView) {
        Container<ShipView> container = new Container<>();
        container.background(getContainerBackground());
        container.size(GameConfig.CELL_SIZE * shipView.getLength() + shipView.getLength() * 2);
        container.setActor(shipView);
        return container;
    }

    private Table getGrid(int sizeX, int sizeY) {
        Table table = new Table(skin);
        for (int i = 0; i <= sizeX; i++) {
            table.row();
            for (int j = 0; j <= sizeY; j++) {

                if (i == 0 && j != 0) {
                    Label label = new Label("A", skin);
                    table.add(label).pad(1);
                } else if (j == 0 && i != 0) {

                    String number = String.valueOf(i);
                    Label label = new Label(number, skin, "default");

                    table.add(label).pad(1);
                } else {
                    GridUnit unitActor = new GridUnit(getGridViewTexture(), j, i);
                    unitActor.setTouchable(Touchable.enabled);
                    unitActor.addListener(new InputListener() {


                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            logger.debug(event.toString() + " x:" + x + "y: " + y);
                            return true;
                        }

                        @Override
                        public void touchDragged(InputEvent event, float x, float y, int pointer) {
                            //        logger.debug(event.toString() + " " + x);
                            super.touchDragged(event, x, y, pointer);
                        }

                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            //     logger.debug(event.toString() + "x: " + x + "y: " + y);

                        }

                        @Override
                        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                            //   logger.debug(event.toString() + " " + x);
                            super.exit(event, x, y, pointer, toActor);

                        }
                    });
                    table.add(unitActor).pad(1);
                }

            }
        }
        return table;

    }

    ShipView shipViewFactory(ShipType shipType, int length, TextureRegion unitViewDamagedTexture,
                             TextureRegion unitViewTexture,TextureRegion regionReady) {
        return new ShipView(shipType, length, false, Direction.Horizontal, unitViewDamagedTexture,
                unitViewTexture,regionReady);


    }


    private TextureRegionDrawable getWindowBackground() {
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.GRAY);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }

    private TextureRegionDrawable getContainerBackground() {
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.BROWN);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }

    private void back() {
        game.setScreen(new MenuScreen(game));
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();
        stage.act();
        stage.draw();
    }
}
