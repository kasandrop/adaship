package com.ada.marcin.screen.menu;

import com.ada.marcin.AdashipGame;
import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.*;
import com.ada.marcin.screen.ui.*;
import com.ada.marcin.util.GdxUtils;
import com.ada.marcin.util.debug.DebugCameraController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class SetUpPlayerScreen extends ScreenAdapter {


    public static final Logger logger = new Logger(SetUpPlayerScreen.class.getName(),
            Logger.DEBUG);

    private final AdashipGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;

    private Board board;
    private SaveButton saveButton;
    private TextButton buttonAuto;

    private TextButton resetButton;

    //focus camera functionality and   camera movements.
    // just for debugging purposes
    private DebugCameraController debugCameraController;
    private static Vector2 vector2 = new Vector2();
    private Map<Integer, ShipView> shipViews = new HashMap<>();
    private List<HUD> huds = new ArrayList<>();
    private Skin skin;
    private UiFactory uiFactory;
    private Player player;

    public SetUpPlayerScreen(AdashipGame game,Player player) {
        this.game = game;
        this.player=player;
        this.assetManager = game.getAssetManager();
        this.uiFactory = new UiFactory(this.assetManager);
        this.skin = assetManager.get(AssetsDescriptor.UISKIN);
    }

    @Override
    public void show() {

        board = new Board(GameConfig.getInstance()
                .getBoardWidth(),
                GameConfig.getInstance()
                        .getBoardHeight());

        viewport = new FitViewport(GameConfig.HUD_WIDTH,
                GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport,
                game.getBatch());
        this.resetButton = new TextButton("Reset  Ships",
                skin,
                "default");
        this.resetButton.setRound(true);
        this.resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onResetClicked();
            }


        });

        this.buttonAuto = new TextButton("Auto Place",
                skin,
                "default");
        buttonAuto.setRound(true);

        buttonAuto.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onAutoClicked();
            }
        });



        this.saveButton = new SaveButton("Save your shipboard",
                skin,
                "default",
                GameConfig.getInstance()
                        .getCountOfBoats());
        this.saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });
        Gdx.input.setInputProcessor(stage);
        initShipsViews();
        initUi();
        //logger.debug("origin x,y="+shipView.getWidth()/2+"  "+shipView.getHeight()/2);
    }

    //2nd column   of the main table
    private void initShipsViews() {
        int myIndex = 0;
        List<Boat> myBoats = GameConfig.getInstance()
                .getBoats();
        for (Boat boat : myBoats) {
            final ShipView shipView = uiFactory.shipViewFactory(myIndex,
                    boat.getLength(),
                    boat.getName(),
                    getUnitViewDamagedTexture(),
                    getUnitViewTexture(),
                    getUnitViewReadyTexture(),
                    GameConfig.CELL_SIZE);
            this.shipViews.put(myIndex,
                    shipView);
            HUD hud = new HUD(skin,
                    myIndex + "",
                    shipView.getName(),
                    shipView.getLength() + "",
                    shipView.getDamage() + "",
                    shipView.getShipStatus()
                            .toString());
            shipView.addObserver(hud);
            shipView.addObserver(this.saveButton);
            this.huds.add(hud);
            myIndex++;
            shipView.addListener(new InputListener() {
                                     @Override
                                     public boolean touchDown(InputEvent event,
                                                              float x,
                                                              float y,
                                                              int pointer,
                                                              int button) {
                                         //logger.debug("touchDown event  on ViewShip:" + shipView.getShipType());
                                         shipView.setZIndex(1000000);
                                         board.removeShipFromTheGrid(shipView);
                                         //back to brown colour
                                         shipView.trainShip();
                                         if (button == Input.Buttons.RIGHT) {
                                             shipView.rotateBy(90);
                                             return false;
                                         }
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
                                     public void touchUp(InputEvent event,
                                                         float x,
                                                         float y,
                                                         int pointer,
                                                         int button) {
                                         logger.debug("touchUp event");
                                         SnapshotArray<Actor> actors = shipView.getShipUnits();
                                         List<GridUnit> gridUnits = new ArrayList<>();
                                         Stage myStage = event.getStage();
                                         for (Actor actor : actors) {
                                             vector2.setZero();
                                             if (!(actor instanceof ShipUnit)) continue;
                                             //Scene2d all points are local that is relative to actor's parent
                                             //changing to stage coordinates
                                             vector2 = actor.localToStageCoordinates(vector2.set(actor.getOriginX(),
                                                     actor.getOriginY()));

                                             //shipView is going to be hit that is why i made it untouchable
                                             shipView.setTouchable(Touchable.disabled);

                                             Actor newActor = myStage.hit(vector2.x,
                                                     vector2.y,
                                                     true);
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
                                         //logger.debug("Rotation:" + shipView.getRotation());
                                         vector2 = firstGridUnit.localToStageCoordinates(vector2.setZero());
                                         event.getStage()
                                                 .addActor(shipView);
                                         shipView.setPositionAlign(vector2.x,
                                                 vector2.y);
                                         shipView.setZIndex(1000);
                                         logger.debug(shipView.printPoints());
                                         boolean answer = board.placeShipOnTheGrid(shipView);
                                         // logger.debug("Is placement allowed?:"+answer);
                                         if (answer) {
                                             shipView.deployShip();
                                         } else {
                                             shipView.deleteCoordinates();
                                         }
                                     }

                                     @Override
                                     public void touchDragged(InputEvent event,
                                                              float x,
                                                              float y,
                                                              int pointer) {
                                         shipView.moveBy(x - shipView.getWidth() / 2,
                                                 y - shipView.getHeight() / 2);
                                     }
                                 }
            );
        }
    }

    private void initUi() {

        Table table = new Table();
        //   table.setDebug(true);

        Table tbl = uiFactory.getGrid(GameConfig.getInstance()
                        .getBoardWidth(),
                GameConfig.getInstance()
                        .getBoardHeight(),
                GameConfig.CELL_SIZE);
        tbl.setName("GridTable");
        table.add(tbl);
        table.add(uiFactory.ContainerWithShipView(this.shipViews));
        table.row();
        table.add(uiFactory.getInfoPanel(this.huds));
        table.add(uiFactory.getButtons(Arrays.asList(this.saveButton,
                this.resetButton,
                this.buttonAuto)));
        table.background(getWindowBackground());
        table.center();
        table.setFillParent(true);
        table.pack();
        table.setDebug(true);
        stage.addActor(table);
        // create debug camera controller
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition((float) GameConfig.WINDOWS_WIDTH / 2,
                (float) GameConfig.WINDOWS_HEIGHT / 2);
    }

    private TextureRegionDrawable getWindowBackground() {
        Pixmap bgPixmap = new Pixmap(1,
                1,
                Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.rgb565(0.09f,
                0.094f,
                0.106f));
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }

    @Override
    public void resize(int width,
                       int height) {
        viewport.update(width,
                height,
                true);
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();
        stage.act();
        stage.draw();
        //to zoom out in
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo((OrthographicCamera) stage.getCamera());
    }

    TextureRegion getUnitViewTexture() {
        return uiFactory.getTextureRegion(Color.DARK_GRAY);
    }

    TextureRegion getUnitViewReadyTexture() {
        return uiFactory.getTextureRegion(Color.GREEN);
    }

    TextureRegion getUnitViewDamagedTexture() {
        return uiFactory.getTextureRegion(Color.RED);
    }

    private void onResetClicked() {
        this.shipViews.clear();
        this.huds.clear();
        show();
    }

    private void onSaveClicked(){
        this.player.setPlayerSetup(PlayerSetup.Ready);
        this.player.setBoard(this.board);
        this.game.setScreen(new MenuScreen(this.game));

    }


    //to place a ship Randomly   on the board  not easy in my case
    //1. get coordinates of GridTable , and width and height
    //2. randomly create a Vector2 which position is going to be somewhere inside that table
    //3. Actor actor=stage.hit(vector2) will get a gridUnit
    //4. NOW OBTAINING A POSITION OF THAT FIRST CELL AND RANDOMLY CHOOSING VERT/hORI POSITION
    //5 REPEAT FOR AS LONG AS N-LENGTH sHIPvIEW STAYS INSIDE A GRID ON FREE CELLS
    public void onAutoClicked() {
        for (Map.Entry<Integer, ShipView> entry : this.shipViews.entrySet()) {
            int key = entry.getKey();
            ShipView shipView = entry.getValue();
            if (shipView.getShipStatus() == ShipStatus.training) {
                autoPosition(shipView);
            }
        }
    }

    private void autoPosition(ShipView shipView) {
        boolean answer = false;
        this.stage.addActor(shipView);

        shipView.randomizeDirection();
        do {
            ShipView autoShipView = placeShipViewRandomly(shipView);
            answer = board.placeShipOnTheGrid(autoShipView);

        } while (answer == false);
        shipView.deployShip();
        //logger.debug("coordinates:" + shipView.printPoints());

    }

    private ShipView placeShipViewRandomly(ShipView shipView) {
        int xxRandom;
        int yyRandom;
        boolean isGridUnit = false;
        Actor randomActor;
        Table grid = this.stage.getRoot()
                .findActor("GridTable");
        float width = grid.getWidth();
        float height = grid.getHeight();
        vector2 = grid.localToStageCoordinates(vector2.setZero());
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
                if (shipView.getDirection() == Direction.Vertical) {
                    if (((GridUnit) randomActor).getGridY() - shipView.getLength() >= 0) {

                        isGridUnit = true;
                    }
                    //it is Horizontal
                } else {

                    if (((GridUnit) randomActor).getGridX() + shipView.getLength() <= 1 + GameConfig.getInstance()
                            .getBoardWidth()) {
                        isGridUnit = true;
                    }
                }


            }

        } while (isGridUnit == false);
        Coordinate coordinate = ((GridUnit) randomActor).getCoordinate();
        logger.debug("checking..length:" + shipView.getLength() + "coordinate :x" + ((GridUnit) randomActor).getGridX() + " y:" + ((GridUnit) randomActor).getGridY());
        shipView.addCoordinatesAuto(coordinate);
        vector2 = ((GridUnit) randomActor).localToStageCoordinates(vector2.setZero());
        shipView.setPositionAlign(vector2.x,
                vector2.y);
        return shipView;

    }
}
