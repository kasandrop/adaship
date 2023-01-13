package com.ada.marcin.screen.ui;

import com.ada.marcin.model.ShipEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;

/**
 * Button which actively observes ShipViews. Once All ShipViews are properly placed on the grid
 * The SaveButton becomes enabled.
 */
public class SaveButton extends TextButton implements Observer {

    public static final Logger logger = new Logger(SaveButton.class.getName(),
            Logger.DEBUG);
    private final int observablesCount;
    private int currentCount;

    {
        this.currentCount = 0;
        this.setDisabled(true);
    }

    /**
     *
     * @param text       text  displayed on the button
     * @param skin       storage of  the ui resources
     * @param styleName  a name of the style of this button
     * @param observablesCount  This is the amount of ViewShips
     */
    public SaveButton(String text,
                      Skin skin,
                      String styleName,
                      int observablesCount) {
        super(text,
                skin,
                styleName);
        this.observablesCount = observablesCount;
    }

    @Override
    public void notify(ShipEvent shipEvent,
                       String data) {
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
        if (this.observablesCount == this.currentCount) {
            this.setDisabled(false);
        } else {
            this.setDisabled(true);
        }
    }
}
