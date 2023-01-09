package com.ada.marcin.screen.menu;

import com.ada.marcin.AdashipGame;
import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.assets.RegionNames;
import com.ada.marcin.common.GameManager;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.util.GdxUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HighScoreScreen extends ScreenAdapter {
    public static final Logger logger = new Logger(HighScoreScreen.class.getName(),
            Logger.DEBUG);

    private final AdashipGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;

    private Skin skin;

    public HighScoreScreen(AdashipGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH,
                GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport,
                game.getBatch());

        Gdx.input.setInputProcessor(stage);

        this.skin = assetManager.get(AssetsDescriptor.UISKIN);
        initUi();
    }

    private void initUi() {
        Table table = new Table();
        table.setDebug(true);

        TextureAtlas gamePlayAtlas = assetManager.get(AssetsDescriptor.GAME_PLAY);


        BitmapFont font = assetManager.get(AssetsDescriptor.FONT);

        TextureRegion backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);


        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        //label style


        //back button
        TextButton backButton = new TextButton("Back",
                skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event,
                                Actor actor) {
                back();
            }
        });
        //quite button

        //setup table
        Table buttonTable = new Table();
        buttonTable.defaults()
                .pad(20);
        buttonTable.center();
        //background

        //label
        Label highScoreText = new Label("HIGHSCORE",
                skin);

        Label highScoreLabel = new Label(GameManager.INSTANCE.getHighScore(),
                skin);

        buttonTable.add(highScoreText)
                .row();
        buttonTable.add(highScoreLabel)
                .row();
        buttonTable.add(backButton)
                .row();


        table.add(buttonTable);

        table.center();
        table.setFillParent(true);
        table.pack();

        stage.addActor(table);

    }


    private void back() {
        game.setScreen(new MenuScreen(game));
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
