package com.pokemon.ui.rank;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pokemon.game.Pokemon;
import com.pokemon.model.Player;
import com.pokemon.screen.GameScreen;
import com.pokemon.ui.AbstractUi;

public class window extends AbstractUi {
    private Stage stage;
    private rankWindow rankWindow;
    private MovingImageUI ui;

    private Pokemon game;
    private GameScreen gameScreen;
    private Player player;
    private AssetManager assetManager;
    private Skin skin;

    protected Texture rankTexture;

    static class rankWindow extends Window{
        static WindowStyle windowStyle = new Window.WindowStyle(new BitmapFont(),Color.BLACK, new TextureRegionDrawable());
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

        rankTexture = assetManager.get("ui/rank.png", Texture.class);

        ui = new MovingImageUI(rankTexture, new Vector2(Gdx.graphics.getWidth() / 2 - 485 / 2, Gdx.graphics.getHeight() / 2 - 428 / 2), new Vector2(100, 100), 225.f, 485, 428);

        //인벤 윈도우
        rankWindow = new rankWindow();
        rankWindow.setSize(400, 300);
        rankWindow.setModal(true);
        rankWindow.setVisible(true);
        rankWindow.setPosition(Gdx.graphics.getWidth() / 2 - rankWindow.getWidth() / 2, Gdx.graphics.getHeight() / 2 - rankWindow.getHeight() / 2);

        stage.addActor(ui);

        Gdx.input.setInputProcessor(this.stage);
    }

    public void update() {
        stage.draw();
        stage.act();
    }
}