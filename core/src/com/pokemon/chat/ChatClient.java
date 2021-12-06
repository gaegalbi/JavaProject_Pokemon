package com.pokemon.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.pokemon.game.Pokemon;
import com.pokemon.screen.GameScreen;
import com.pokemon.ui.AbstractUi;
import com.pokemon.ui.ChatButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import static com.pokemon.ui.LoginUi.playerID;


public class ChatClient extends AbstractUi {
    private Stage stage;
    private GameScreen gameScreen;
    private Pokemon game;
    Skin skin;
    SpriteBatch batch;
    private Socket socket;
    OutputStream out;
    BufferedReader in;
    int i = 900;
    Person person;
    public ChatClient(final GameScreen gameScreen, final Pokemon game){
        person = new Person();
        person.setName(playerID);
        batch = new SpriteBatch();
        stage = new Stage();
        this.gameScreen = gameScreen;
        this.game = game;
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setPosition(-300, 20);

        final TextArea chatArea = new TextArea("", skin);
        final TextField chatField = new TextField("", skin);
        chatArea.setPrefRows(50f);

        final ScrollPane scroll = new ScrollPane(chatArea, skin);
        scroll.setScrollingDisabled(true, false);
        scroll.setFadeScrollBars(true);
        scroll.setForceScroll(false,true);
        scroll.scrollTo(0,900,0,0);

        chatField.setMaxLength(20);

        final TextButton chatButton = new TextButton("output", skin);
        chatButton.setColor(Color.RED);
        final TextButton exitButton = new TextButton("exit", skin);
        exitButton.setColor(Color.PINK);

        table.row();
        table.add(scroll).width(200).height(300);

        table.row();
        table.add(chatField).width(200).pad(2);

        table.row();
        table.add(chatButton).width(50).pad(5).left();
        table.row();
        table.add(exitButton).width(50).pad(3).left();

        try {
            socket = new Socket("localhost", 9040);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        chatButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("눌림");
                String text;
                text = person.getName() + ": "+chatField.getText();
                System.out.println(text);
                try {
                    out.write(new String(text+"\n").getBytes());
                    chatField.setText("");
                    scroll.scrollTo(0,i-=20,0,100);
                    scroll.layout();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//        stage.addListener(new InputListener(){
//            public boolean keyUp(InputEvent event, int keycode) {
//                if (keycode == Input.Keys.ENTER) {
//                    String text;
//                    text = chatField.getText();
//                    try {
//                        out.write(new String(text+"\n").getBytes());
//                        scroll.scrollTo(0,i-=20,0,100);
//                        scroll.layout();
//
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//                return false;
//            }
//        });
        chatField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
                    String text;
                    text = person.getName() + ": "+chatField.getText();
                    try {
                        out.write(new String(text+"\n").getBytes());
                        chatField.setText("");
                        scroll.scrollTo(0,i-=20,0,100);
                        scroll.layout();

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        Gdx.input.setInputProcessor(stage);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.popScreen();
                gameScreen.pushScreen(new ChatButton(gameScreen,game));
            }
        });
        Runnable recieved = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String recievedMessage = null;
                    try {
                        System.out.println("전");
                        recievedMessage = in.readLine();
                        System.out.println("후");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    chatArea.appendText(recievedMessage+"\n");
                }
            }


        };

        new Thread(recieved).start();

    }




    public void resize (int width, int height) {
        if (width == 0 && height == 0) return; //see https://github.com/libgdx/libgdx/issues/3673#issuecomment-177606278
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