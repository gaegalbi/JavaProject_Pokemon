package com.pokemon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.pokemon.chat.ChatClient;
import com.pokemon.chat.Person;
import com.pokemon.game.Pokemon;
import com.pokemon.screen.GameScreen;
import com.pokemon.util.SkinGenerator;

public class ChatButton extends AbstractUi{
    Texture buttonimage;
    Skin skin;
    Stage stage;
    SpriteBatch batch;
    GameScreen gameScreen;
    Pokemon game;
    private Texture chat;
    private Label a;
    private Skin skin2;
    private AssetManager assetManager;
    public ChatButton(final GameScreen gameScreen, final Pokemon game) {
        batch = new SpriteBatch();
        stage = new Stage();
        this.gameScreen = gameScreen;
        this.game = game;
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        //skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        //폰트 버튼
        skin2 = SkinGenerator.generateSkin(assetManager);
        Label.LabelStyle labelColors = new Label.LabelStyle(skin2.getFont("font"), new Color(1, 212 / 255.f, 0, 1));
        a = new Label("",labelColors);
        a.setSize(100,100);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setPosition(-350, -210);

        chat = new Texture(Gdx.files.internal("ui/chatimage.png"));
        TextureRegionDrawable chat2 = new TextureRegionDrawable(chat);
        ImageButton chatButton = new ImageButton(chat2);
        //TextButton chatButton = new TextButton("CHAT", skin);
        //chatButton.setColor(Color.RED);

        table.row();
        table.add(chatButton).width(100).height(50).pad(5).colspan(2);
        stage.addActor(a);
        a.setPosition(30,30);
        buttonimage = new Texture(Gdx.files.internal("badlogic.jpg"));

       chatButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("눌림");
                gameScreen.pushScreen(new ChatClient(gameScreen,game));
            }
        });
    }



    public void resize ( int width, int height){
        if (width == 0 && height == 0)
            return; //see https://github.com/libgdx/libgdx/issues/3673#issuecomment-177606278
        stage.getViewport().update(width, height, true);
        PopupMenu.removeEveryMenu(stage);
    }

    public void update () {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void dispose () {
        stage.dispose();
    }
}