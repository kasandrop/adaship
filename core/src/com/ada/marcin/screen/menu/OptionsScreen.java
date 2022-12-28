package com.ada.marcin.screen.menu;

import com.ada.marcin.AdashipGame;
import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.model.Direction;
import com.ada.marcin.model.ShipType;
import com.ada.marcin.screen.ui.ShipView;
import com.ada.marcin.screen.ui.UnitActor;
import com.ada.marcin.util.GdxUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;

public class OptionsScreen extends ScreenAdapter {


    public static final Logger logger = new Logger(OptionsScreen.class.getName(), Logger.DEBUG);

    private final AdashipGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;

    private Pixmap pixmap;


    private Map<ShipType,ShipView> shipViews=new HashMap<ShipType,ShipView> ();


    private UnitActor unitActor;
    private TextureRegion region;

    private Skin skin;


    public OptionsScreen(AdashipGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        pixmap = new Pixmap(GameConfig.CELL_SIZE, GameConfig.CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.SKY);
        pixmap.fill();
        region = new TextureRegion(new Texture(pixmap));
        this.skin = assetManager.get(AssetsDescriptor.UISKIN);

    }


    TextureRegion getUnitViewTexture() {
        Pixmap pixmapShip = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapShip.setColor(Color.DARK_GRAY);
        pixmapShip.fill();
        return new TextureRegion(new Texture(pixmapShip));
    }

    TextureRegion getUnitViewDamagedTexture() {
        Pixmap pixmapShip = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapShip.setColor(Color.RED);
        pixmapShip.fill();
        return new TextureRegion(new Texture(pixmapShip));
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);
        initShipsViews();
        initUi();
        //logger.debug("origin x,y="+shipView.getWidth()/2+"  "+shipView.getHeight()/2);
    }


    private void initUi() {

        Table table = new Table();
        //   table.setDebug(true);
        table.add(getGrid(10, 10));
        table.add(getShipViews());
        table.background(getWindowBackground());

        table.center();
        table.setFillParent(true);
        table.pack();
        table.setDebug(true);

        stage.addActor(table);

    }

    private VerticalGroup getShipViews() {
        VerticalGroup verticalGroup = new VerticalGroup();
        for (Map.Entry<ShipType, ShipView> entry : this.shipViews.entrySet()) {
            //System.out.println(entry.getKey() + ":" + entry.getValue());
            verticalGroup.addActor(getContainer(entry.getValue()));
        }
        verticalGroup.space(10);
        verticalGroup.pad(10);
        verticalGroup.pack();
        return verticalGroup;

    }
   private void  initShipsViews(){
        this.shipViews.put(ShipType.PatrolBoat,shipViewFactory(ShipType.PatrolBoat,2,getUnitViewDamagedTexture(), getUnitViewTexture()));
        this.shipViews.put(ShipType.Submarine,shipViewFactory(ShipType.Submarine,3,getUnitViewDamagedTexture(), getUnitViewTexture()));
        this.shipViews.put(ShipType.Destroyer,shipViewFactory(ShipType.Destroyer,3,getUnitViewDamagedTexture(), getUnitViewTexture()));
        this.shipViews.put(ShipType.Battleship,shipViewFactory(ShipType.Battleship,4,getUnitViewDamagedTexture(), getUnitViewTexture()));
        this.shipViews.put(ShipType.Carrier,shipViewFactory(ShipType.Carrier,5,getUnitViewDamagedTexture(), getUnitViewTexture()));


    }

    Container<Container<HorizontalGroup>> getContainer(ShipView  shipView){
        Container<Container<HorizontalGroup>> container = new  Container<Container<HorizontalGroup>>();
        container.background(getContainerBackground());
        container.size(GameConfig.CELL_SIZE * shipView.getLength()+shipView.getLength()*2);
        container.setActor(shipView);
        return container;
    }

    private Table getGrid(int sizeX, int sizeY) {
        Table table = new Table(skin);
        for (int i = 0; i <= sizeX; i++) {
            table.row();
            for (int j = 0; j <= sizeY; j++) {

                if (i == 0 && j != 0) {
                    Label label = new Label("A", skin);
                    table.add(label).pad(1);
                } else if (j == 0 && i != 0) {

                    String number = String.valueOf(i);
                    Label label = new Label(number, skin, "default");

                    table.add(label).pad(1);
                } else {
                    UnitActor unitActor = new UnitActor(this.region);
                    unitActor.addListener(new InputListener() {
                        @Override
                        public boolean handle(Event e) {
                            logger.debug(e.toString());
                            return super.handle(e);

                        }

                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            logger.debug(event.toString() + " " + x);
                            return super.touchDown(event, x, y, pointer, button);
                        }

                        @Override
                        public void touchDragged(InputEvent event, float x, float y, int pointer) {
                            logger.debug(event.toString() + " " + x);
                            super.touchDragged(event, x, y, pointer);
                        }

                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            logger.debug(event.toString() + " " + x);
                            super.enter(event, x, y, pointer, fromActor);
                        }

                        @Override
                        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                            super.exit(event, x, y, pointer, toActor);
                        }
                    });
                    table.add(unitActor).pad(1);
                }

            }
        }
        return table;

    }

    ShipView shipViewFactory(ShipType shipType, int length, TextureRegion unitViewDamagedTexture, TextureRegion unitViewTexture) {
        final ShipView shipView = new ShipView( shipType, length,false, Direction.Horizontal, unitViewDamagedTexture,
                unitViewTexture);

        //  logger.debug("1origin x,y="+shipView.getWidth()/2+"  "+shipView.getHeight()/2);
        //  logger.debug("2origin x,y="+shipView.getMaxWidth()/2+"  "+shipView.getMaxHeight()/2);

        //if event was processed return true
        shipView.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                shipView.setZIndex(100);
                if(button== Input.Buttons.RIGHT){
                    shipView.changeDirection();
                 //  shipView.localToParentCoordinates(new Vector2(x,y));
                    shipView.setOrigin(shipView.getWidth() / 2, shipView.getHeight() / 2);
                    shipView.rotateBy(90);
                    //logger.debug("Pointer:" + pointer + " button:" + button);
                  //  logger.debug("1origin x,y=" +shipView.getWidth() / 2 + "  " +shipView.getHeight() / 2);
                    return true;
                }

                return false;

            }
        });
        shipView.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                logger.debug("diretion:::"+shipView.getDirection().toString());
                logger.debug("x:::"+shipView.getX());
                logger.debug("y:::"+shipView.getY());

                shipView.localToParentCoordinates(new Vector2(x,y));
                shipView.moveBy(x - shipView.getWidth() / 2, y - shipView.getHeight() / 2);


            }
        });

        return shipView;
    }

    /*
     public void touchDragged(InputEvent event, float x, float y, int pointer) {
        float oldTouchY = vec.y;
        table.localToParentCoordinates(vec.set(x, y));
        table.moveBy(0f, vec.y - oldTouchY);
    }
     */

    private TextureRegionDrawable getWindowBackground() {
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.GRAY);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }

    private TextureRegionDrawable getContainerBackground() {
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.BROWN);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }

    private void back() {
        game.setScreen(new MenuScreen(game));
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();
        stage.act();
        stage.draw();
    }
}
