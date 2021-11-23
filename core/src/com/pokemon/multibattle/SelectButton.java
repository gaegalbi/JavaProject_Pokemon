package com.pokemon.multibattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pokemon.game.Pokemon;
import com.pokemon.ui.AbstractUi;

import java.util.LinkedList;

import static com.pokemon.ui.LoginUi.playerID;

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
    String totalname;
    int c = 0;
    int x=0;
    int y=0;


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
        button1.setPosition(150,230);
        button1.setColor(Color.CLEAR);

        button2 = new TextButton("2",skin);
        stage.addActor(button2);
        button2.setWidth(100);
        button2.setHeight(100);
        button2.setPosition(350,230);
        button2.setColor(Color.CLEAR);

        button3 = new TextButton("3",skin);
        stage.addActor(button3);
        button3.setWidth(100);
        button3.setHeight(100);
        button3.setPosition(550,230);
        button3.setColor(Color.CLEAR);

        button4 = new TextButton("4",skin);
        stage.addActor(button4);
        button4.setWidth(100);
        button4.setHeight(100);
        button4.setPosition(150,100);
        button4.setColor(Color.CLEAR);

        button5 = new TextButton("5",skin);
        stage.addActor(button5);
        button5.setWidth(100);
        button5.setHeight(100);
        button5.setPosition(350,100);
        button5.setColor(Color.CLEAR);

        button6 = new TextButton("6",skin);
        stage.addActor(button6);
        button6.setWidth(100);
        button6.setHeight(100);
        button6.setPosition(550,100);
        button6.setColor(Color.CLEAR);

        this.pss =pss;



        button1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //totalname = playerID +" "+"1";
                pss.setStackList(playerID +" "+"1");
                c++;
                setX(150);
                setY(250);
            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //totalname = playerID +" "+"1";
                pss.setStackList(playerID +" "+"2");
                c++;
                setX(350);
                setY(250);
            }
        });
        button3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //totalname = playerID +" "+"1";
                pss.setStackList(playerID +" "+"3");
                c++;
                setX(550);
                setY(250);
            }
        });
        button4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("버튼눌림");
                //totalname = playerID +" "+"1";
                pss.setStackList(playerID +" "+"4");
                c++;
                setX(150);
                setY(120);
            }
        });
        button5.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //totalname = playerID +" "+"1";
                pss.setStackList(playerID +" "+"5");
                c++;
                setX(350);
                setY(120);
            }
        });
        button6.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //totalname = playerID +" "+"1";
                pss.setStackList(playerID +" "+"6");
                c++;
                setX(550);
                setY(120);
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

    public int getC() {return c;}

    public void setC(int c) {this.c = c;}

    public int getX() {return x;}

    public void setX(int x) {this.x = x;}

    public int getY() {return y;}

    public void setY(int y) {this.y = y;}

}
