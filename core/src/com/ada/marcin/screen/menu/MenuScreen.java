package com.ada.marcin.screen.menu;

import com.ada.marcin.AdashipGame;
import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.config.GameConfig;
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
    public static final Logger logger = new Logger(MenuScreen.class.getName(), Logger.DEBUG);

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
        Table table = uiFactory.createTableForBackground();
        Table buttonTable = uiFactory.createContainerForButtons();

        //play button
        TextButton playButton = new TextButton("PLAY", skin);
        playButton.getLabel().setFontScale(2, 2);
        playButton.pad(20) ;
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                play();
            }
        });
        //high score button
        TextButton highScoreButton = new TextButton("High Score", skin);
        highScoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                highScore();
            }
        });
        //options
        TextButton player1Button = new TextButton("Player 1. Set up the game", skin);
        player1Button.getLabel().setFontScale(2, 2);
        player1Button.pad(20) ;
        player1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setUpPlayer1();
            }
        });
        //quite button

        TextButton player2Button = new TextButton("Player 2. Set up the game", skin);
        player2Button.getLabel().setFontScale(2, 2);
        player2Button.pad(20) ;
        player2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setUpPlayer2();
            }
        });

        //setup table

        buttonTable.add(playButton).width(400).row();
       // buttonTable.add(player2Button).row();
        buttonTable.add(player1Button).width(400).row();
        buttonTable.add(player2Button).width(400).row();

        table.add(buttonTable);

        table.center();
        table.setFillParent(true);
        table.pack();

        stage.addActor(table);

    }

    private void play() {

       // game.setScreen(new GameScreen(game));
    }

    private void highScore() {
        game.setScreen(new HighScoreScreen(game));
    }

    private void setUpPlayer1() {
        logger.debug("setUpPlayer1()");
        game.setScreen(new OptionsScreen(game));
    }

    private void setUpPlayer2() {
        logger.debug("setUpPlayer2()");
        game.setScreen(new OptionsScreen(game));
    }


    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        initUi();
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
