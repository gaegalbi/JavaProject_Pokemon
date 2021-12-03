package com.pokemon.multibattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.pokemon.game.Pokemon;
import com.pokemon.screen.GameScreen;
import com.pokemon.util.GifDecoder;

public class Loading implements Screen {
    int i=0;
    final Pokemon game;
    GameScreen gameScreen;
    SpriteBatch batch;
    Animation<TextureRegion> animation;
    Animation<TextureRegion> animation2;
    Animation<TextureRegion> ballRight;
    Animation<TextureRegion> ballLeft;
    private Texture fieldimage1;
    private Texture fieldimage2;
    private Texture background2;
    Timer timer = new Timer();
    float elapsed;
    private BattleClient bc;
    public Loading(Pokemon game,final BattleClient bc) {
        this.game = game;
        this.bc = bc;
        batch = new SpriteBatch();
        background2 = new Texture(Gdx.files.internal("multibattle/battlebgField.png"));
        fieldimage1 = new Texture(Gdx.files.internal("multibattle/enemybaseFieldGrass.png"));
        fieldimage2 = new Texture(Gdx.files.internal("multibattle/enemybaseFieldGrass.png"));
        animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("multibattle/loding4.gif").read());
        animation2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("multibattle/battle7.gif").read());
        ballRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("multibattle/ball.gif").read());
        ballLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("multibattle/ball2.gif").read());
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(animation.getKeyFrame(elapsed), 140f, 50f);
        batch.draw(animation2.getKeyFrame(elapsed), 0f, 0f);
        if(i==1){
            batch.draw(background2,0,0,800,600);
            batch.draw(fieldimage1, 80, 140);
            batch.draw(fieldimage2, 450, 140);
            batch.draw(ballRight.getKeyFrame(elapsed),-60,120);
            batch.draw(ballLeft.getKeyFrame(elapsed),260,120);
            Timer.schedule(new Timer.Task(){
                @Override
                public void run(){
                    i=2;
                }
            },1);
        }
        batch.end();
        if(i==0) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    i = 1;
                }
            }, 3);
        }
        if(i==2){
            BattleScreenStart();
        }
    }
    public void BattleScreenStart(){
        game.setScreen(new MultiBattleScreen(game,bc));
        dispose();
    }
    @Override
    public void resize(int width, int height) {

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
        batch.dispose();
    }
}