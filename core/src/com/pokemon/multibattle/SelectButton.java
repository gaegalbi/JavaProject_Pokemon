package com.pokemon.multibattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pokemon.game.Pokemon;
import com.pokemon.ui.AbstractUi;

public class SelectButton extends AbstractUi {
    final Pokemon game;
    private SpriteBatch batch;
    private Stage stage;
    TextButton button1;
    TextButton button2;
    TextButton button3;
    TextButton button4;
    TextButton button5;
    TextButton button6;
    Skin skin;
    PokemonSelectScreen pss;



    public SelectButton(final PokemonSelectScreen pss,Pokemon game) {
        this.game = game;
        stage = new Stage();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        button1 = new TextButton("1",skin);
        stage.addActor(button1);
        button1.setWidth(100);
        button1.setHeight(100);
        button1.setPosition(150,300);
        button1.setColor(Color.CLEAR);

        button2 = new TextButton("2",skin);
        stage.addActor(button2);
        button2.setWidth(100);
        button2.setHeight(100);
        button2.setPosition(350,300);
        button2.setColor(Color.CLEAR);

        button3 = new TextButton("3",skin);
        stage.addActor(button3);
        button3.setWidth(100);
        button3.setHeight(100);
        button3.setPosition(550,300);
        button3.setColor(Color.CLEAR);

        button4 = new TextButton("4",skin);
        stage.addActor(button4);
        button4.setWidth(100);
        button4.setHeight(100);
        button4.setPosition(150,150);
        button4.setColor(Color.CLEAR);

        button5 = new TextButton("5",skin);
        stage.addActor(button5);
        button5.setWidth(100);
        button5.setHeight(100);
        button5.setPosition(350,150);
        button5.setColor(Color.CLEAR);

        button6 = new TextButton("6",skin);
        stage.addActor(button6);
        button6.setWidth(100);
        button6.setHeight(100);
        button6.setPosition(550,150);
        button6.setColor(Color.CLEAR);

        this.pss =pss;



        button1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pss.setStackList(pss.pokemonList[0]);

            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pss.setStackList(pss.pokemonList[1]);
            }
        });
        button3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pss.setStackList(pss.pokemonList[2]);
            }
        });
        button4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("버튼눌림");
              pss.setStackList(pss.pokemonList[3]);
            }
        });
        button5.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pss.setStackList(pss.pokemonList[4]);
            }
        });
        button6.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pss.setStackList(pss.pokemonList[5]);
            }
        });
       }

    @Override
    public void resize(int width, int height) {

    }

    public void update () {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


}
