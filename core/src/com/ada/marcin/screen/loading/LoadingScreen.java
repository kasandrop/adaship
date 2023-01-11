package com.ada.marcin.screen.loading;

import com.ada.marcin.AdashipGame;
import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.Boat;
import com.ada.marcin.model.Coordinate;
import com.ada.marcin.screen.menu.MenuScreen;
import com.ada.marcin.util.GdxUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class LoadingScreen extends ScreenAdapter {

    public static final Logger logger = new Logger(LoadingScreen.class.getName(),
            Logger.DEBUG);
    public static final float PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2;
    public static final float PROGRESS_BAR_HEIGHT = 50;
    private final AdashipGame adashipGame;
    private final AssetManager assetManager;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    private BitmapFont bitmapFont;
    private SpriteBatch spriteBatch;
    private float progress;
    private float waitTime = 0.75f;

    public LoadingScreen(AdashipGame adashipGame) {
        this.adashipGame = adashipGame;
        this.assetManager = adashipGame.getAssetManager();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.HUD_WIDTH,
                GameConfig.HUD_HEIGHT,
                camera);
        shapeRenderer = new ShapeRenderer();

        bitmapFont = new BitmapFont();
        spriteBatch = new SpriteBatch();

        assetManager.load(AssetsDescriptor.FONT_SMALL);
        assetManager.load(AssetsDescriptor.FONT);
        assetManager.load(AssetsDescriptor.GAME_PLAY);
        assetManager.load(AssetsDescriptor.UISKIN);
        FileHandle handle = Gdx.files.internal("adaship_config.ini");

        //  end of the line  \r?
        String[] data = handle.readString()
                .split(System.lineSeparator());
        if (data[0].contains("Board")) {
            String xx = left((data[0].split(":"))[1],
                    "x");
            String yy = right((data[0].split(":"))[1],
                    "x");
            logger.debug("x:" + xx);
            logger.debug("y:" + yy);

            //creating singleton
            GameConfig.getInstance(Integer.parseInt(xx.trim()),
                    Integer.parseInt(yy.trim()));


        }
        for (int i = 1; i < data.length; i++) {
            if (data[i].contains("Boat")) {
                String length = right((data[i].split(":"))[1],
                        ",");
                String name = left((data[i].split(":"))[1],
                        ",");
                GameConfig.getInstance()
                        .registerBoat(new Boat(name,
                                Integer.parseInt(length.trim())));
            }


        }
        logger.debug("  size of created array:" + GameConfig.getInstance()
                .getCountOfBoats());
        for (int i = 0; i < 80; i++) {
            logger.debug(Coordinate.columnLabel(i));
        }
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        draw();

        shapeRenderer.end();

        update(delta);
    }

    private void draw() {
        float progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2f;
        float progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2f;

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        bitmapFont.draw(spriteBatch,
                progress * 100 + "%",
                progressBarX,
                progressBarY - 40);
        spriteBatch.end();
        shapeRenderer.rect(progressBarX,
                progressBarY,
                PROGRESS_BAR_WIDTH * progress,
                PROGRESS_BAR_HEIGHT);
    }

    private void update(float delta) {

        //progress between 0 and 1
        progress = assetManager.getProgress();

        if (assetManager.update()) {
            waitTime -= delta;
            if (waitTime <= 0)
                adashipGame.setScreen(new MenuScreen(adashipGame));
        }
    }

    @Override
    public void resize(int width,
                       int height) {
        viewport.update(width,
                height,
                true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
        bitmapFont.dispose();
    }

    String left(String haystack,
                String needle) {

        return haystack.split(needle)[0];
    }

    String right(String haystack,
                 String needle) {
        return haystack.split(needle)[1];
    }
}
