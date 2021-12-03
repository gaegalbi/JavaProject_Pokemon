package com.pokemon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.pokemon.chat.ChatClient;
import com.pokemon.chat.Person;
import com.pokemon.game.Pokemon;
import com.pokemon.screen.GameScreen;

public class ChatButton extends AbstractUi{
    Texture buttonimage;
    Skin skin;
    Stage stage;
    SpriteBatch batch;
    GameScreen gameScreen;
    Pokemon game;
    public ChatButton(final GameScreen gameScreen, final Pokemon game) {
        batch = new SpriteBatch();
        stage = new Stage();
        this.gameScreen = gameScreen;
        this.game = game;
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setPosition(-350, -210);

        TextButton chatButton = new TextButton("CHAT", skin);
        chatButton.setColor(Color.RED);

        table.row();
        table.add(chatButton).width(100).height(50).pad(5).colspan(2);

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