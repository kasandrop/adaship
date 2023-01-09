package com.ada.marcin.screen.ui;

import com.ada.marcin.model.ShipEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class HUD extends Table implements Observer {

    private Skin skin;
    private String boatIDX;
    private String name;
    private String length;
    private String damage;
    private String status;
    private ShipView shipView;
    private Label lblBoatIDX;
    private Label lblName;
    private Label lblLength;
    private Label lblDamage;
    private Label lblStatus;
    private Label lblCoordinates;


    public HUD(Skin skin,
               String boatIDX,
               String name,
               String length,
               String damage,
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
        this.lblBoatIDX = new Label(this.boatIDX + "",
                skin);
        this.lblName = new Label(this.name + "",
                skin);
        this.lblLength = new Label(this.length + "",
                skin);

        this.lblDamage = new Label(this.damage + "",
                skin);

        this.lblStatus = new Label(this.status,
                skin);

        this.lblCoordinates = new Label("",
                skin);


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
/*







 for (Map.Entry<Integer, ShipView> entry : this.shipViews.entrySet()) {
            int id = entry.getKey();
            final ShipView shipView = entry.getValue();
            tbl.add();
            tbl.add(new Label(shipView.getName(),
                    skin));
            tbl.add(new Label(String.valueOf(shipView.getLength()),
                    skin));
            tbl.add(new Label(String.valueOf(shipView.getLength()),
                    skin));
            tbl.add(new Label(shipView.printPoints(),
                    skin));
            tbl.row();
        }
        tbl.pad(5);

 */