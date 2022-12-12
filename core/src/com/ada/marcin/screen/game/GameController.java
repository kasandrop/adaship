package com.ada.marcin.screen.game;

import com.ada.marcin.config.GameConfig;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Logger;


public class GameController {

    // == constants ==
    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);

    // == attributes ==

    private float obstacleTimer;
    private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;



    // == constructors ==
    public GameController() {
        init();
    }

    // == init ==
    private void init() {
        // create player

    }

    // == public methods ==
    public void update(float delta) {

    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    // == private methods ==
    private boolean isGameOver() {
        return lives <= 0;
    }



    private void updatePlayer() {

    }



    private void blockPlayerFromLeavingTheWorld() {

    }






    private void updateScore(float delta) {
        scoreTimer += delta;

        if (scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            score += MathUtils.random(1, 5);
            scoreTimer = 0.0f;
        }
    }


}
