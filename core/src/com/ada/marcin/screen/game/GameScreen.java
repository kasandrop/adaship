package com.ada.marcin.screen.game;


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
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameScreen extends ScreenAdapter {

    public static final Logger logger = new Logger(GameScreen.class.getName(),
            Logger.DEBUG);

    private final AdashipGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;

    private Player player;


    private TextButton nextButton;

    //focus camera functionality and   camera movements.
    // just for debugging purposes
    private DebugCameraController debugCameraController;
    private static Vector2 vector2 = new Vector2();
    private Map<Integer, ShipView> shipViews = new HashMap<>();
    private List<HUD> huds = new ArrayList<>();
    private Skin skin;
    private UiFactory uiFactory;

    public GameScreen(AdashipGame game, Player player) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        this.uiFactory = new UiFactory(this.assetManager);
        this.skin = assetManager.get(AssetsDescriptor.UISKIN);
        this.player = player;
    }

    @Override
    public void show() {

        viewport = new FitViewport(GameConfig.HUD_WIDTH,
                GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport,
                game.getBatch());


        this.nextButton = new TextButton("Next Player",
                skin,
                "default");
        this.nextButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        onNextButtonClick();
                    }
                }
        );
        Gdx.input.setInputProcessor(stage);
        // initShipsViews();
        initUi();
        updatePlayer();

    }

    //2nd column   of the main table
    private void onNextButtonClick() {
        Player player1 = GameConfig.getInstance().getPlayer1();
        Player player2 = GameConfig.getInstance().getPlayer2();
        this.player = this.player == player1 ? player2 : player1;
        this.updatePlayer();
    }

    private void updatePlayer() {
        Table shipboard = this.stage.getRoot().findActor("Shipboard");
//SnapshotArray is libgdx inner data type .SnapshotArray    allows to change  its  element during iteration.
        // SnapshotArray<Actor>gridUnits=   shipboard.getChildren();
        for (Actor actor : shipboard.getChildren()) {
            if (actor instanceof GridUnit) {
                ((GridUnit) actor).setRegionCurrent(getGridUnitTexture()) ;
            }


        }
        Set<Coordinate> keys = this.player.getBoard().getCoordinates();
        for (Coordinate key : keys) {
            String name = key.toString();
            GridUnit gridUnit = this.stage.getRoot().findActor(name);
            gridUnit.setRegionCurrent(getUnitViewReadyTexture());
        }
    }

    private void initUi() {
        Table table = new Table();
        Table shipboard = uiFactory.getGrid(GameConfig.getInstance()
                        .getBoardWidth(),
                GameConfig.getInstance()
                        .getBoardHeight(),
                GameConfig.SMALL_CELL_SIZE);
        shipboard.setName("Shipboard");
        shipboard.row();
        shipboard.add(new Label("Ship Board", this.skin)).colspan(shipboard.getColumns());

        Table targetBoard = uiFactory.getGrid(GameConfig.getInstance()
                        .getBoardWidth(),
                GameConfig.getInstance()
                        .getBoardHeight(),
                GameConfig.SMALL_CELL_SIZE);
        targetBoard.setName("TargetBoard");
        targetBoard.row();
        targetBoard.add(new Label("Target Board", this.skin)).colspan(shipboard.getColumns());
        targetBoard.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);


            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);


            }
        });

        SplitPane splitAbout = new SplitPane(new ScrollPane(shipboard,skin,"default"), new ScrollPane(targetBoard,skin,"default"), false, skin,"default-horizontal");
        splitAbout.setSplitAmount(0.4f);


        table.row();
        table.add(splitAbout);

        table.row();


        table.add(uiFactory.getInfoPanel(this.huds));
        table.add(uiFactory.getButtons(Arrays.asList(this.nextButton)));
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

    TextureRegion getGridUnitTexture() {
        return uiFactory.getTextureRegion(Color.SKY);
    }
}

