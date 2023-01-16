package com.ada.marcin.screen.menu;

import com.ada.marcin.AdashipGame;
import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.*;
import com.ada.marcin.screen.ui.*;
import com.ada.marcin.util.GdxUtils;
import com.ada.marcin.util.debug.DebugCameraController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.*;


public class SetUpPlayerScreen extends ScreenAdapter {
    public static final Logger logger = new Logger(SetUpPlayerScreen.class.getName(),
                                                   Logger.DEBUG);
    private static Vector2 vector2 = new Vector2();
    private final AdashipGame game;
    private final AssetManager assetManager;
    private final TextButton aiButton;
    private Viewport viewport;
    private Stage stage;
    private Board board;
    private SaveButton saveButton;
    private TextButton buttonAuto;
    private TextButton resetButton;
    //focus camera functionality and   camera movements.
    // just for debugging purposes
    private DebugCameraController debugCameraController;
    private Map<Integer, ShipView> shipViews = new HashMap<>();
    private List<HUD> huds = new ArrayList<>();
    private Skin skin;
    private UiFactory uiFactory;
    private Player player;

    public SetUpPlayerScreen(AdashipGame game, Player player) {
        this.game         = game;
        this.player       = player;
        this.assetManager = game.getAssetManager();
        this.uiFactory    = new UiFactory(this.assetManager);
        this.skin         = assetManager.get(AssetsDescriptor.UISKIN);
        this.aiButton     = new TextButton("AI Plays", skin, "toggle");
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

        this.aiButton.setRound(true);
        this.aiButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (aiButton.isChecked()) {
                    player.setAi(true);
                  onAIClick();
                }else{
                    logger.debug("unchecked:" + actor.toString());
                    player.setAi(false);
                }

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

        initHUDs();
        initShipsViews();
        initUi();
        //logger.debug("origin x,y="+shipView.getWidth()/2+"  "+shipView.getHeight()/2);
    }

    private void onAIClick() {

        // onResetClicked();

        this.huds.clear();
        this.board.clear();
        onAutoClicked();
    }

    private void initHUDs() {

        List<Boat> myBoats = GameConfig.getInstance()
                .getBoats();
        for (Boat boat : myBoats) {
            HUD hud = new HUD(skin,
                              boat.getBoatIdx(),
                              boat.getName(),
                              boat.getLength(),
                              0,
                              ShipStatus.Training.toString());

            this.huds.add(hud);
        }
    }

    //2nd column   of the main table
    private void initShipsViews() {
        List<Boat> myBoats = GameConfig.getInstance()
                .getBoats();
        for (Boat boat : myBoats) {
            final ShipView shipView = uiFactory.shipViewFactory(boat.getBoatIdx(),
                    boat.getLength(),
                    boat.getName(),
                    getUnitViewDamagedTexture(),
                    getUnitViewTexture(),
                    getUnitViewReadyTexture(),
                    GameConfig.CELL_SIZE);
            this.shipViews.put(boat.getBoatIdx(),
                    shipView);
            HUD hud = this.huds.get(boat.getBoatIdx());
            shipView.addObserver(hud);
            shipView.addObserver(this.saveButton);
            shipView.addListener(new MyInputListener(shipView,board));
        }
    }

    private void initUi() {

        Table table = new Table();
        //   table.setDebug(true);

        Table tbl = uiFactory.getGrid(GameConfig.getInstance()
                                                .getBoardWidth(),
                                      GameConfig.getInstance()
                                                .getBoardHeight(),
                                      GameConfig.CELL_SIZE, Touchable.enabled);
        tbl.setName("GridTable");
        table.add(tbl);
        table.add(uiFactory.ContainerWithShipView(this.shipViews));
        table.row();
        table.add(uiFactory.getInfoPanel(this.huds));
        table.add(uiFactory.getButtons(Arrays.asList(this.saveButton,
                this.resetButton,
                this.buttonAuto,
                this.aiButton)));
        table.background(getWindowBackground());
        table.center();
        table.setFillParent(true);
        table.pack();
        // table.setDebug(true);
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

    private void onSaveClicked() {
        this.player.setPlayerSetup(PlayerSetup.Ready);
        this.player.setShipBoard(this.board);
        this.player.setTargetBoard(new Board(GameConfig.getInstance().getBoardWidth(), GameConfig.getInstance().getBoardHeight()));
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
            ShipView shipView = entry.getValue();
            if (shipView.getShipStatus() == ShipStatus.Training) {
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

    }

    private ShipView placeShipViewRandomly(ShipView shipView) {
        GridUnit gridUnit;
        boolean  isGridUnit = false;
        do {
            gridUnit = this.uiFactory.getGridUnitRandomly("GridTable", this.stage);
            if (shipView.getDirection() == Direction.Vertical) {
                if (gridUnit.getGridY() - shipView.getLength() >= 0) {

                    isGridUnit = true;
                }
                //it is Horizontal
            } else {
                if (gridUnit.getGridX() + shipView.getLength() <= 1 + GameConfig.getInstance().getBoardWidth()) {
                    isGridUnit = true;
                }
            }
        } while (!isGridUnit);
        Coordinate coordinate = gridUnit.getCoordinate();
        shipView.addCoordinatesAuto(coordinate);
        vector2 = gridUnit.localToStageCoordinates(vector2.setZero());
        shipView.setPositionAlign(vector2.x,
                                  vector2.y);
        return shipView;

    }


}
