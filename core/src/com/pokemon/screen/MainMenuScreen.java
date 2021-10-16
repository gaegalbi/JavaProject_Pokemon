package com.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.pokemon.game.Pokemon;
import com.pokemon.ui.AbstractUi;
import com.pokemon.ui.LoginUi;

import java.util.HashMap;
import java.util.Stack;

public class MainMenuScreen implements Screen {

    final Pokemon game;
    OrthographicCamera camera;
    private Texture logoImage;
    private Stack<AbstractUi> uiStack;

    private HashMap<String, String> accounts = new HashMap<>(); // 임시 로그인 기능용

    public MainMenuScreen(Pokemon game) {
        this.game = game;
        uiStack = new Stack<>();
        logoImage = new Texture(Gdx.files.internal("logo.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);

        uiStack.add(new LoginUi(this));
    }
    
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(logoImage, 144, 280,512,192);
        game.batch.end();

//        testUi.update();
        for (AbstractUi abstractUi : uiStack) {
            abstractUi.update();
        }
    }

    public void gameStart() {
        game.setScreen(new GameScreen(game));
        dispose();
    }

    @Override
    public void resize(int width, int height) {
//        testUi.resize(width, height);
        for (AbstractUi abstractUi : uiStack) {
            abstractUi.resize(width, height);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
//        testUi.dispose();
        for (AbstractUi abstractUi : uiStack) {
            abstractUi.dispose();
        }
        logoImage.dispose();
    }

    public void pushScreen(AbstractUi ui) {
        uiStack.add(ui);
    }

    public void popScreen() {
        AbstractUi popped = uiStack.pop();
        popped.dispose();
        Gdx.input.setInputProcessor(uiStack.peek().getStage());
    }
    
    // 임시 로그인 기능용
    public void createAccount(String id,String password) {
        accounts.put(id, password);
    }

    // 임시 로그인 기능용
    public boolean loginValidate(String id,String password) {
        try {
            if (accounts.get(id).equals(password)) {
                return true;
            }
        } catch (NullPointerException ignored) {}

        return false;
    }
}
