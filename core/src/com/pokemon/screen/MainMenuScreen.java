package com.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.pokemon.game.Pokemon;
import com.pokemon.ui.AbstractUi;
import com.pokemon.ui.LoginUi;
import com.pokemon.util.GifDecoder;

import java.util.Stack;

public class MainMenuScreen implements Screen {
    final Pokemon game;
    OrthographicCamera camera;
    private Texture logoImage;
    private Stack<AbstractUi> uiStack;
    private Animation<TextureRegion> pikachu = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pikachu.gif").read());
    private float timer;
    private int pikachuX = -225;
    private Music mainMusic;

    public MainMenuScreen(Pokemon game) {
        VisUI.load(VisUI.SkinScale.X1);
        this.game = game;
        uiStack = new Stack<>();
        logoImage = new Texture(Gdx.files.internal("logo.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);

        uiStack.add(new LoginUi(this,game));
        mainMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mainMusic.ogg"));
        mainMusic.setVolume(0.05f);
        mainMusic.setLooping(true);
    }

    @Override
    public void show() {
        mainMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(logoImage, 0, 0,800, 480);
        game.batch.draw(pikachu.getKeyFrame(timer),pikachuX,0);
        game.batch.end();
        timer += delta;
        pikachuX += 2;
        if (timer > 0.4f) {
            timer = 0;
        }
        if (pikachuX > 1025) {
            pikachuX = -225;
        }
//        testUi.update();
        for (AbstractUi abstractUi : uiStack) {
            abstractUi.update();
        }
    }

    public void gameStart() {
        game.setScreen(new GameScreen(game));
        //game.setScreen(new BattleScreen(game)); // 배틀스크린 테스트용
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
        VisUI.dispose();
        for (AbstractUi abstractUi : uiStack) {
            abstractUi.dispose();
        }
        logoImage.dispose();
        mainMusic.dispose();
    }

    public void pushScreen(AbstractUi ui) {
        uiStack.add(ui);
    }

    public void popScreen() {
        AbstractUi popped = uiStack.pop();
        popped.dispose();
        Gdx.input.setInputProcessor(uiStack.peek().getStage());
    }

}
