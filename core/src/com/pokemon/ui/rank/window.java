package com.pokemon.ui.rank;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pokemon.game.Pokemon;
import com.pokemon.model.Player;
import com.pokemon.screen.GameScreen;
import com.pokemon.ui.AbstractUi;
import com.pokemon.util.SkinGenerator;

public class window extends AbstractUi {
    private Stage stage;
    private rankWindow rankWindow;
    private MovingImageUI ui;

    private Label[] rankLabel;
    private String[] rankStr = {"100", "200", "300", "400", "500", "600"};

    private Label[] IDLabel;
    private String[] IDStr = {"홍성주", "김성주", "박성주", "최성주", "갱성주", "주성주"};

    private Label myRankLabel;
    private String myRankStr = "100";

    private Skin skin;

    private Pokemon game;
    private GameScreen gameScreen;
    private Player player;
    private AssetManager assetManager;

    protected Texture rankTexture;

    static class rankWindow extends Window{
        static WindowStyle windowStyle = new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable());
        public rankWindow() {
            super("", windowStyle);
        }
    }

    public window(GameScreen gameScreen, Pokemon game, Player player) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.player = player;
        stage = new Stage(new ScreenViewport());

        assetManager = new AssetManager();
        assetManager.load("ui/rank.png", Texture.class);
        assetManager.finishLoading();

        skin = SkinGenerator.generateSkin_2(assetManager);

        rankTexture = assetManager.get("ui/rank.png", Texture.class);

        ui = new MovingImageUI(rankTexture, new Vector2(Gdx.graphics.getWidth() / 2 - 485 / 2, Gdx.graphics.getHeight() / 2 - 428 / 2), new Vector2(100, 100),
                225.f, 485, 428);

        //인벤 윈도우
        rankWindow = new rankWindow();
        rankWindow.setSize(400, 300);
        rankWindow.setModal(true);
        rankWindow.setVisible(true);
        rankWindow.setPosition(Gdx.graphics.getWidth() / 2 - rankWindow.getWidth() / 2, Gdx.graphics.getHeight() / 2 - rankWindow.getHeight() / 2);

        // Fonts and Colors
        Label.LabelStyle[] labelColors = new Label.LabelStyle[]{
                new Label.LabelStyle(skin.getFont("font"), new Color(1, 212 / 255.f, 0, 1)), // yellow
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 0, 255, 1)), // blue
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 1, 60 / 255.f, 1)), // green
                new Label.LabelStyle(skin.getFont("font"), new Color(255 / 255.f, 255 / 255.f, 255 / 255.f, 1)), // white
        };

        IDLabel = new Label[6];
        for (int i = 0; i < IDLabel.length; i++) {
            IDLabel[i] = new Label(IDStr[i], labelColors[3]);
            IDLabel[i].setTouchable(Touchable.disabled);
            IDLabel[i].setAlignment(Align.left);
        }

        rankLabel = new Label[6];
        for (int i = 0; i < rankLabel.length; i++) {
            rankLabel[i] = new Label(rankStr[i], labelColors[0]);
            rankLabel[i].setTouchable(Touchable.disabled);
            rankLabel[i].setAlignment(Align.left);
        }

        myRankLabel = new Label("50000", labelColors[0]);
        myRankLabel.setSize(50, 14);
        myRankLabel.setTouchable(Touchable.disabled);
        myRankLabel.setAlignment(Align.right);

        //ui 및 슬롯, 버튼들 추가(init)
        stage.addActor(ui);
        for (int i = 0; i < IDLabel.length; i++) stage.addActor(IDLabel[i]);
        for (int i = 0; i < rankLabel.length; i++) stage.addActor(rankLabel[i]);
        stage.addActor(myRankLabel);

        Gdx.input.setInputProcessor(this.stage);

       // updateText();
    }

    private void updateText() {
        rankLabel[0].setText("HI");
        rankLabel[1].setText("123");
    }

    public void update() {
        IDLabel[0].setPosition(ui.getX() + 198, ui.getY() + 322);
        IDLabel[1].setPosition(ui.getX() + 198, ui.getY() + 266);
        IDLabel[2].setPosition(ui.getX() + 198, ui.getY() + 210);
        IDLabel[3].setPosition(ui.getX() + 198, ui.getY() + 154);
        IDLabel[4].setPosition(ui.getX() + 198, ui.getY() + 98);
        IDLabel[5].setPosition(ui.getX() + 198, ui.getY() + 42);

        rankLabel[0].setPosition(ui.getX() + 385, ui.getY() + 322);
        rankLabel[1].setPosition(ui.getX() + 385, ui.getY() + 266);
        rankLabel[2].setPosition(ui.getX() + 385, ui.getY() + 210);
        rankLabel[3].setPosition(ui.getX() + 385, ui.getY() + 154);
        rankLabel[4].setPosition(ui.getX() + 385, ui.getY() + 98);
        rankLabel[5].setPosition(ui.getX() + 385, ui.getY() + 42);

        myRankLabel.setPosition(ui.getX() + 82, ui.getY() + 42);

        stage.draw();
        stage.act();
    }
}