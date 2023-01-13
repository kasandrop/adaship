package com.ada.marcin.screen.ui;

import com.ada.marcin.model.ShipEvent;
import com.ada.marcin.model.ShipStatus;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class HUD extends Table implements Observer {

    private Skin skin;
    private int boatIDX;
    private String name;
    private int length;
    private int damage;
    private String status;
    private Label lblBoatIDX;
    private Label lblName;
    private Label lblLength;
    private Label lblDamage;
    private Label lblStatus;
    private Label lblCoordinates;


    public HUD(Skin skin,
               int boatIDX,
               String name,
               int length,
               int damage,
               String status) {
        super(skin);
        this.skin = skin;
        this.boatIDX = boatIDX;
        this.name = name;
        this.length = length;
        this.damage = damage;
        this.status = status;
        init();
    }


    private void init() {
        this.setName(boatIDX + "HUD");
        this.lblBoatIDX = new Label(this.boatIDX + "",
                skin, "default");
        this.lblName = new Label(this.name + "",
                skin, "default");
        this.lblLength = new Label(this.length + "",
                skin, "default");

        this.lblDamage = new Label(this.damage + "",
                skin, "default");

        this.lblStatus = new Label(this.status,
                skin, "default");

        this.lblCoordinates = new Label("",
                skin, "default");


        this.add(this.lblBoatIDX)
                .width(50);
        this.add(this.lblName)
                .width(100);
        this.add(this.lblLength)
                .width(50);
        this.add(this.lblDamage)
                .width(50);
        this.add(this.lblStatus)
                .width(200);
        this.add(this.lblCoordinates)
                .width(200);
        this.row();
        this.pad(1);
    }


    public void reset() {
        this.lblCoordinates.setText("");
        this.lblDamage.setText("0");
        this.lblStatus.setText(ShipStatus.Deployed.toString());
        this.damage = 0;
    }

    public void addCoordinate(String coordinate) {
        String textFromLabel = this.lblCoordinates.getText().toString();
        this.lblCoordinates.setText(textFromLabel + " " + coordinate);
    }

    public void addOneToTheDamage() {
        this.damage++;
        this.lblDamage.setText(this.damage);
        this.setStatus(ShipStatus.Damaged.toString());
        if (this.damage == this.length) {
            this.setStatus(ShipStatus.Sunk.toString());
        }
    }

    public void setStatus(String status) {
        this.lblStatus.setText(status);
    }

    @Override
    public void notify(ShipEvent shipEvent,
                       String data) {
        if (shipEvent == ShipEvent.Deployment) {
            this.lblStatus.setText("Deployed");
            this.lblCoordinates.setText(data);
        } else if (shipEvent == ShipEvent.Damage) {
            this.lblDamage.setText(data);
        } else if (shipEvent == ShipEvent.Training) {
            this.lblStatus.setText("Training");
            this.lblCoordinates.setText(data);
        }
    }
}






