package com.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.pokemon.game.Pokemon;
import com.pokemon.ui.LoginUi;
import com.pokemon.ui.TestUi;

public class MainMenuScreen implements Screen {

    final Pokemon game;
    OrthographicCamera camera;
    TestUi testUi;
    private Texture logoImage;
//    LoginUi loginUi = new LoginUi();

    public MainMenuScreen(Pokemon game) {
        this.game = game;
        logoImage = new Texture(Gdx.files.internal("logo.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);
        testUi = new TestUi();
        testUi.gameSet(this);
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
//        game.font.draw(game.batch, "Welcome to Pokemon!!! ", 100, 150);
//        game.font.draw(game.batch, "Click anywhere to Start! ", 100, 100);
        game.batch.end();
        testUi.update();
        //loginUi.update();
            
        // 엔터 누르면 게임시작
        /*if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }*/
    }

    public void gameStart() {
        game.setScreen(new GameScreen(game));
        dispose();
    }

    @Override
    public void resize(int width, int height) {
//        loginUi.resize(width, height);
        testUi.resize(width, height);
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
//        loginUi.dispose();
        testUi.dispose();
        logoImage.dispose();
    }
}
