package com.ada.marcin;

import com.ada.marcin.screen.loading.LoadingScreen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AdashipGame extends Game {
    private AssetManager assetManager;
    private SpriteBatch batch;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        setScreen(new LoadingScreen(this));
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        batch.dispose();
    }
}
