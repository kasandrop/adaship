package com.ada.marcin.screen.menu;

import com.ada.marcin.AdashipGame;
import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.Player;
import com.ada.marcin.model.PlayerSetup;
import com.ada.marcin.screen.game.GameScreen;
import com.ada.marcin.screen.ui.UiFactory;
import com.ada.marcin.util.GdxUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MenuScreen extends ScreenAdapter {
    public static final Logger logger = new Logger(MenuScreen.class.getName(),
            Logger.DEBUG);

    private final AdashipGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;

    private UiFactory uiFactory;

    private Skin skin;

    public MenuScreen(AdashipGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        this.uiFactory = new UiFactory(this.assetManager);
        this.skin = assetManager.get(AssetsDescriptor.UISKIN);

    }

    private void initUi() {
        PlayerSetup playerSetup1=GameConfig.getInstance().getPlayer1().getPlayerSetup();
        PlayerSetup playerSetup2=GameConfig.getInstance().getPlayer2().getPlayerSetup();

        Table table = uiFactory.createTableForBackground();
        Table buttonTable = uiFactory.createContainerForButtons();

        //play button
        TextButton playButton = new TextButton("PLAY",
                skin,"default");
        playButton.pad(20);
        if(playerSetup2==PlayerSetup.Ready  && playerSetup1==PlayerSetup.Ready  ){
            playButton.setDisabled(false);
        }else{
            playButton.setDisabled(true);
        }
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event,
                                Actor actor) {
                play();
            }
        });

        //setup Player1
        TextButton player1SetupButton = new TextButton("Player 1", skin,"toggle");
         player1SetupButton.pad(20);

        if(playerSetup1==PlayerSetup.Ready  ){
            player1SetupButton.setChecked(true);
        }
        player1SetupButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event,
                                Actor actor) {
                onPlayer1SetupClick();
            }
        });

        //button 2
        TextButton player2SetupButton = new TextButton("Player 2",
                skin,"toggle");
        player2SetupButton.pad(20);
        if(playerSetup2==PlayerSetup.Ready  ){
            player2SetupButton.setChecked(true);
        }
        player2SetupButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event,
                                Actor actor) {
                onPlayer2SetupClick();
            }
        });

        //reset
        TextButton resetButton = new TextButton("Reset Players SetUp",
                skin,"default");
        resetButton.pad(20);
        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event,
                                Actor actor) {
                onResetClick();
            }
        });
        //setup table

        buttonTable.add(playButton)
                .width(400)
                .row();
        // buttonTable.add(player2Button).row();
        buttonTable.add(player1SetupButton)
                .width(400)
                .row();
        buttonTable.add(player2SetupButton)
                .width(400)
                .row();
        buttonTable.add(resetButton)
                .width(400)
                .row();

        buttonTable.setBackground("dialogTrans");
        table.add(buttonTable);

        table.center();
        table.setFillParent(true);
        table.pack();

        stage.addActor(table);

    }

    private void play() {

        game.setScreen(new GameScreen(game, GameConfig.getInstance().getPlayer1(), GameConfig.getInstance().getPlayer2()));
    }

    private void onPlayer1SetupClick() {
        logger.debug("setUpPlayer1()");
        Player player1=GameConfig.getInstance().getPlayer1();
        game.setScreen(new SetUpPlayerScreen(game,player1));
    }

    private void onPlayer2SetupClick() {
        logger.debug("setUpPlayer2()");
        Player player2=GameConfig.getInstance().getPlayer2();
        game.setScreen(new SetUpPlayerScreen(game,player2));
    }

    private  void onResetClick(){
        logger.debug("reset button()");
        GameConfig.getInstance().getPlayer1().resetPlayer();
        GameConfig.getInstance().getPlayer2().resetPlayer();
        game.setScreen(new MenuScreen(game));
    }


    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH,
                GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport,
                game.getBatch());

        Gdx.input.setInputProcessor(stage);

        initUi();
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
    }
}
