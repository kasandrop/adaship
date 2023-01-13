package com.ada.marcin.screen.game;


import com.ada.marcin.AdashipGame;
import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.*;
import com.ada.marcin.screen.ui.GridUnit;
import com.ada.marcin.screen.ui.HUD;
import com.ada.marcin.screen.ui.ShipView;
import com.ada.marcin.screen.ui.UiFactory;
import com.ada.marcin.util.GdxUtils;
import com.ada.marcin.util.debug.DebugCameraController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;
import java.util.*;

public class GameScreen extends ScreenAdapter {

    public static final Logger logger = new Logger(GameScreen.class.getName(),
            Logger.DEBUG);

    private final AdashipGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;

    private Player player;

    private Player opponent;


    private TextButton nextButton;

    //focus camera functionality and   camera movements.
    // just for debugging purposes
    private DebugCameraController debugCameraController;
    private static Vector2 vector2 = new Vector2();
    private Map<Integer, ShipView> shipViews = new HashMap<>();
    private List<HUD> huds = new ArrayList<>();
    private Skin skin;
    private UiFactory uiFactory;

    public GameScreen(AdashipGame game, Player player, Player opponent) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        this.uiFactory = new UiFactory(this.assetManager);
        this.skin = assetManager.get(AssetsDescriptor.UISKIN);
        this.player = player;
        this.opponent = opponent;
        this.player.setShipLength(getShipsLength());
        this.opponent.setShipLength(getShipsLength());
    }

    private int[] getShipsLength(){
        int[] length;
        length=new int[GameConfig.getInstance().getBoats().size()];
        for(Boat boat :GameConfig.getInstance().getBoats()){
            length[boat.getBoatIdx()]=boat.getLength();
        }
        return length;
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
        createHUDPanel();
        initUi();

        updatePlayer();

    }


    //2nd column   of the main table
    private void onNextButtonClick() {
        Player player1 = GameConfig.getInstance().getPlayer1();
        Player player2 = GameConfig.getInstance().getPlayer2();
        this.player = (this.player == player1) ? player2 : player1;
        this.opponent = (this.player == player1) ? player2 : player1;
        for (Boat boat : GameConfig.getInstance()
                .getBoats()) {
            HUD hud = this.stage.getRoot().findActor(boat.getBoatIdx() + "HUD");
            hud.reset();
          //  hud.resetDamage();
        }

        this.updatePlayer();

    }

    private void updatePlayer() {
        // this.huds.clear();


        logger.debug("Player:" + this.player.getName());
        Label title = this.stage.getRoot().findActor("title");
        title.setText(this.player.getName());
        Table shipboard = this.stage.getRoot().findActor("Shipboard");
//SnapshotArray is libgdx inner data type .SnapshotArray    allows to change  its  element during iteration.
        // SnapshotArray<Actor>gridUnits=   shipboard.getChildren();
        //resetting grid units
        for (Actor actor : shipboard.getChildren()) {
            if (actor instanceof GridUnit) {
                ((GridUnit) actor).setRegionCurrent(getGridUnitTexture());
                ((GridUnit) actor).setTouchable(Touchable.enabled);
            }
        }
        Set<Coordinate> keys = this.player.getShipBoard().getKeys();
        for (Coordinate key : keys) {
            String name = key.toString();
            GridUnit gridUnit = shipboard.findActor(name);
            gridUnit.setRegionCurrent(getUnitViewReadyTexture());
            CellContent cellContent = player.getShipBoard().getValue(key);

            HUD foundHud = this.stage.getRoot().findActor(cellContent.getBoatIdx() + "HUD");


            foundHud.addCoordinate(key.toString());
            if (cellContent.isDamaged()) {
                foundHud.addOneToTheDamage();
            }
        }

// the same for the targetboard
        Table targetBoard = this.stage.getRoot().findActor("TargetBoard");
//SnapshotArray is libgdx inner data type .SnapshotArray    allows to change  its  element during iteration.
        // SnapshotArray<Actor>gridUnits=   shipboard.getChildren();
        //resetting grid units
        for (Actor actor : targetBoard.getChildren()) {
            if (actor instanceof GridUnit) {
                ((GridUnit) actor).setRegionCurrent(getGridUnitTexture());
                ((GridUnit) actor).setTouchable(Touchable.enabled);
            }
        }
        Set<Coordinate> keysOfTargetBoard = this.player.getTargetBoard().getKeys();
        for (Coordinate key : keysOfTargetBoard) {
            String name = key.toString();
            GridUnit gridUnit = targetBoard.findActor(name);
            CellContent cellContent = player.getTargetBoard().getValue(key);
            gridUnit.setTouchable(Touchable.disabled);
            if(cellContent.getBoatIdx()==-1){
                gridUnit.setRegionCurrent( getMissGridUnitTexture() );
            }else if(this.player.isShipSunk(cellContent.getBoatIdx())){

                gridUnit.setRegionCurrent(getGridUnitSunkTexture());
            }else{
                gridUnit.setRegionCurrent( getUnitViewDamagedTexture() );
            }
        }
    }

    private void initUi(){
        Label labelWinner=new Label("WINNER",skin,"winner");
        labelWinner.setPosition((GameConfig.WINDOWS_WIDTH-labelWinner.getWidth())/2,(GameConfig.WINDOWS_HEIGHT-labelWinner.getHeight())/2);
        Table table = new Table();
        table.background(getWindowBackground());
        Table shipboard = uiFactory.getGrid(GameConfig.getInstance()
                        .getBoardWidth(),
                GameConfig.getInstance()
                        .getBoardHeight(),
                GameConfig.SMALL_CELL_SIZE, Touchable.disabled);
        shipboard.setName("Shipboard");
        shipboard.row();
        shipboard.add(new Label("Ship Board", this.skin)).colspan(shipboard.getColumns());

        Table targetBoard = uiFactory.getGrid(GameConfig.getInstance()
                        .getBoardWidth(),
                GameConfig.getInstance()
                        .getBoardHeight(),
                GameConfig.SMALL_CELL_SIZE, Touchable.enabled);
        targetBoard.setName("TargetBoard");
        targetBoard.row();
        targetBoard.add(new Label("Target Board", this.skin)).colspan(shipboard.getColumns());
        targetBoard.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Actor actor = event.getTarget();
                if (actor instanceof GridUnit) {
                    Coordinate coordinate=((GridUnit) actor).getCoordinate();
                    torpedo(coordinate,(GridUnit)actor);
                }

            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        SplitPane splitAbout = new SplitPane(new ScrollPane(shipboard, skin, "default"), new ScrollPane(targetBoard, skin, "default"), false, skin, "default-horizontal");
        splitAbout.setSplitAmount(0.4f);
        Label titleLabel = new Label(this.player.getName(), skin, "title");
        titleLabel.setName("title");
        titleLabel.setFontScale(2.2f);
        table.add(titleLabel).colspan(2).pad(26);
        table.row();
        table.add(splitAbout).colspan(2);
        table.row();
        table.add(uiFactory.getInfoPanel(this.huds));
        table.add(uiFactory.getButtons(Arrays.asList(this.nextButton)));
        table.center();
        table.setFillParent(true);
        table.pack();
        table.setDebug(true);
        table.addActor(labelWinner);
        stage.addActor(table);
        // create debug camera controller
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition((float) GameConfig.WINDOWS_WIDTH / 2,
                (float) GameConfig.WINDOWS_HEIGHT / 2);
    }

    private void torpedo(Coordinate coordinate,GridUnit gridUnit){
        //so player will only be able to use this coordinate once only
        gridUnit.setTouchable(Touchable.disabled);
        //check if it is a hit
      int result= this.opponent.torpedoOpponent(coordinate);
      this.player.saveMyTorpedoResult(coordinate,result);
      if(result==-1){
                gridUnit.setRegionCurrent(getMissGridUnitTexture());
      }else  if(  this.player.isShipSunk(result)){

          gridUnit.setRegionCurrent(getGridUnitSunkTexture());
      }else{
          gridUnit.setRegionCurrent(getUnitViewDamagedTexture());
      }
      if(this.player.isPlayerAWinner()){
          logger.debug("You have won. Winner :"+this.player.getName());
      }

    }
    private void createHUDPanel() {

        List<Boat> myBoats = GameConfig.getInstance()
                .getBoats();
        for (Boat boat : myBoats) {
            HUD hud = new HUD(skin,
                    boat.getBoatIdx(),
                    boat.getName(),
                    boat.getLength(),
                    0,
                    ShipStatus.deployed.toString());

            this.huds.add(hud);
        }
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

    TextureRegion getMissGridUnitTexture() {
        return uiFactory.getTextureRegion(Color.BLACK);
    }

    TextureRegion getGridUnitSunkTexture() {
        return uiFactory.getTextureRegion(Color.BLUE);
    }
}

