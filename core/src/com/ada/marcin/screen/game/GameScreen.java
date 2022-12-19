package com.ada.marcin.screen.game;

import com.ada.marcin.AdashipGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;


public class GameScreen implements Screen {

    private final AssetManager assetManager;
    private GameController controller;
    private GameRenderer renderer;
    private AdashipGame adashipGame;

    public GameScreen(AdashipGame adashipGame) {
        this.adashipGame = adashipGame;
        assetManager = this.adashipGame.getAssetManager();
    }

    @Override
    public void show() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        controller = new GameController();
        renderer = new GameRenderer(adashipGame.getBatch(), assetManager, controller);
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
