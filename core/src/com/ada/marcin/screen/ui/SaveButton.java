package com.ada.marcin.screen.ui;

import com.ada.marcin.model.ShipEvent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;

public class SaveButton extends TextButton implements Observer {

    public static final Logger logger = new Logger(SaveButton.class.getName(),
            Logger.DEBUG);
    private final int observerableCount;
    private int currentCount;

    public SaveButton(String text,
                      Skin skin,
                      String styleName,
                      int observerableCount) {
        super(text,
                skin,
                styleName);
        this.observerableCount = observerableCount;
    }

    {
        this.currentCount = 0;
        this.setDisabled(true);
    }

    @Override
    public void notify(ShipEvent shipEvent,
                       String data) {

        logger.debug("notification event:"+shipEvent.toString());
        if (shipEvent == ShipEvent.Deployment) {
            this.currentCount++;
        }

        if (shipEvent == ShipEvent.Training) {
            this.currentCount--;
        }
        refreshButton();
    }

    private void refreshButton() {
        logger.debug("current count:" + this.currentCount);
        if (this.observerableCount == this.currentCount) {
            this.setDisabled(false);
        } else {
            this.setDisabled(true);
        }
    }
}
