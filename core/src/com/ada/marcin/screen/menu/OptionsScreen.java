package com.ada.marcin.screen.menu;

import com.ada.marcin.AdashipGame;
import com.ada.marcin.assets.AssetsDescriptor;
import com.ada.marcin.config.GameConfig;
import com.ada.marcin.screen.ui.SeaCell;
import com.ada.marcin.util.GdxUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OptionsScreen extends ScreenAdapter {


    public static final Logger logger = new Logger(OptionsScreen.class.getName(), Logger.DEBUG);

    private final AdashipGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;

    private Pixmap pixmap;
    private SeaCell seaCell;
    private TextureRegion region;

    private Skin skin;

    public OptionsScreen(AdashipGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        pixmap = new Pixmap(26, 26, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fill();
        region=new TextureRegion(new Texture(pixmap));
        this.skin=assetManager.get(AssetsDescriptor.UISKIN);

    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        initUi();
    }


      private Table  drawGrid(int sizeX,int sizeY){
          Table table = new Table();
          for(int i=0 ;i<=sizeX;i++){
              table.row();
              for(int j=0;j<=sizeY;j++){

                  if(i==0 &&  j!=0){
                      Label label=new Label("A",skin);
                      table.add(label).pad(1);
                  }else if(j==0 && i!=0){

                      String number=String.valueOf(i);
                      Label label=new Label( number,skin,"default");

                      table.add(label).pad(1);
                  }else{
                      SeaCell seaCell=new SeaCell(this.region,i,j);
                      seaCell.addListener(new InputListener(){

                      });
                      table.add(seaCell).pad(1);
                  }

              }
          }
       return table;

      }

    private void initUi() {
        pixmap = new Pixmap(26, 26, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fill();
        region=new TextureRegion(new Texture(pixmap));
        Table table = new Table();
     //   table.setDebug(true);
        table.add(drawGrid(10,10));
        table.background(getTableBackground());

        table.center();
        table.setFillParent(true);
        table.pack();

        stage.addActor(table);

    }

    private TextureRegionDrawable getTableBackground(){
        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.GRAY);
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
