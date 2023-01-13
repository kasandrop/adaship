package com.ada.marcin.screen.menu;

import com.ada.marcin.model.Board;
import com.ada.marcin.screen.ui.GridUnit;
import com.ada.marcin.screen.ui.ShipUnit;
import com.ada.marcin.screen.ui.ShipView;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Event Listener. Separated to its own class.
 */
public class MyInputListener extends InputListener {
    public static final Logger logger = new Logger(MyInputListener.class.getName(),
                                                   Logger.DEBUG);
    private static Vector2 vector2 = new Vector2();
    private final ShipView shipView;
    private final Board board;

    public MyInputListener(ShipView shipView, Board board) {
        this.shipView = shipView;
        this.board    = board;
    }

    @Override
    public boolean touchDown(InputEvent event,
                             float x,
                             float y,
                             int pointer,
                             int button) {
        shipView.setZIndex(1000000);
        board.removeShipFromTheGrid(shipView);
        //back to brown colour
        shipView.trainShip();
        if (button == Input.Buttons.RIGHT) {
            shipView.rotateBy(90);
            return false;
        }
        //  returning true means touchUp or drag is going to be handled
        return true;
    }

    //Ship is built of shipUnits   When user drops on the grid a ship event is raised :touchUp. That event is raised
    // on the ViewShip.  I calculate here a  middle point of each unitActor.  Carrier for example consists of 5 unitActors
    // and will have 5 middle points (These are  originX and originY built-in variable of the actor class)
    //Because all actors coordinates are relative to  its parent .  I translate them to be relative to the stage (root group)
    //Next, I am finding actors which my ship overlaps (To be precise the deepest actor in the stage hierarchy which contains
    // the middle point  (originX,originY)
    // These found actors   are  cells of the table which my ship overlaps.
    // I use them for 1) aligning a ship
    //              2) remembering a position on the board
    @Override
    public void touchUp(InputEvent event,
                        float x,
                        float y,
                        int pointer,
                        int button) {
        logger.debug("touchUp event");
        SnapshotArray<Actor> actors    = shipView.getShipUnits();
        List<GridUnit>       gridUnits = new ArrayList<>();
        Stage                myStage   = event.getStage();
        for (Actor actor : actors) {
            vector2.setZero();
            if (!(actor instanceof ShipUnit)) continue;
            //Scene2d all points are local that is relative to actor's parent
            //changing to stage coordinates
            vector2 = actor.localToStageCoordinates(vector2.set(actor.getOriginX(),
                                                                actor.getOriginY()));

            //shipView is going to be hit that is why i made it untouchable
            shipView.setTouchable(Touchable.disabled);

            Actor newActor = myStage.hit(vector2.x,
                                         vector2.y,
                                         true);
            shipView.setTouchable(Touchable.childrenOnly);
            if (newActor instanceof GridUnit) {
                gridUnits.add((GridUnit) newActor);
            }
        }
        //the amount of cells must be equal to the length of the shipView
        //if it is not it means the shipView   placed on the grid only partially
        if (gridUnits.size() != shipView.getLength()) {
            return;
        }
        for (GridUnit gridUnit : gridUnits) {
            if (gridUnit == null) {
                // logger.debug("actor is null");
                return;
            }
            //  logger.debug(gridUnit.toString());
            //for testing purposes i mark grid which contains the ship
            //  ((GridUnit) gridUnit).chengecolor();
            shipView.addCoordinate(gridUnit.getCoordinate());
        }
        //aligning  a ship  (converting stage position of the ship to a local one.
        //Actor position is  relative to its parent.
        GridUnit firstGridUnit;
        //logger.debug("Direction:"+shipView.getDirection().toString()+"POSITION:"+shipView.getHorizontalGroup().getX()+" y:"+shipView.getHorizontalGroup().getY());
        //together with rotation first child becomes the last one and vice versa
//                                         if (shipView.getRotation()%180.0f>1){
//                                                firstGridUnit=gridUnits.get(shipView.getLength()-1);
//                                         }else{
        firstGridUnit = gridUnits.get(0);
//                                         }
        //logger.debug("Rotation:" + shipView.getRotation());
        vector2 = firstGridUnit.localToStageCoordinates(vector2.setZero());
        event.getStage()
             .addActor(shipView);
        shipView.setPositionAlign(vector2.x,
                                  vector2.y);
        shipView.setZIndex(1000);
        logger.debug(shipView.getCoordinatesAsString());
        boolean answer = board.placeShipOnTheGrid(shipView);
        // logger.debug("Is placement allowed?:"+answer);
        if (answer) {
            shipView.deployShip();
        } else {
            shipView.deleteCoordinates();
        }
    }

    @Override
    public void touchDragged(InputEvent event,
                             float x,
                             float y,
                             int pointer) {
        shipView.moveBy(x - shipView.getWidth() / 2,
                        y - shipView.getHeight() / 2);
    }
}


