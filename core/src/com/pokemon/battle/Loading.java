
package com.pokemon.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.pokemon.game.Pokemon;
import com.pokemon.game.Settings;
import com.pokemon.screen.BattleScreen;
import com.pokemon.screen.GameScreen;
import com.pokemon.util.GifDecoder;

public class Loading implements Screen {
    int i=1;
    final Pokemon game;
    GameScreen gameScreen;
    SpriteBatch batch;
    Animation<TextureRegion> animation;
    Animation<TextureRegion> animation2;
    Animation<TextureRegion> ballRight;
    Animation<TextureRegion> ball;
   // private Texture fieldimage1;
   private TextureRegion background;
    private TextureRegion platform;
    private AssetManager assetManager;

    private float playerSquareMiddleX = 0;
    private float playerSquareMiddleY = 0;
    private float opponentSquareMiddleX = 0;
    private float opponentSquareMiddleY = 0;
    private int squareSize = 100;

    Timer timer = new Timer();
    float elapsed;
    public Loading(Pokemon game) {
        this.game = game;
        batch = new SpriteBatch();
 /*       background2 = new Texture(Gdx.files.internal("C:\\battlebgField.png"));
        fieldimage1 = new Texture(Gdx.files.internal("C:\\enemybaseFieldGrass.png"));
        fieldimage2 = new Texture(Gdx.files.internal("C:\\enemybaseFieldGrass.png"));
        animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("C:\\loding4.gif").read());
        animation2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("C:\\battle7.gif").read());
        ballRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("C:\\ball.gif").read());
        ballLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("C:\\ball2.gif").read());*/

        assetManager = new AssetManager();
        assetManager.load("battle/battlepack.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("battle/battlepack.atlas", TextureAtlas.class);
        background = atlas.findRegion("background");
        platform = atlas.findRegion("platform");
        ball = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/pokeball.gif").read());
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        //플레이어 플랫폼위치
        playerSquareMiddleX = Gdx.graphics.getWidth()/2 - (squareSize + Gdx.graphics.getWidth()/15);
        playerSquareMiddleY = Gdx.graphics.getHeight()/2 + (25*Settings.SCALE);

        //상대 플랫폼위치
        opponentSquareMiddleX = Gdx.graphics.getWidth()/2 + (squareSize + Gdx.graphics.getWidth()/15);
        opponentSquareMiddleY = Gdx.graphics.getHeight()/2 + (25*Settings.SCALE);

        float platformYOrigin = playerSquareMiddleY - platform.getRegionHeight()/2*Settings.SCALE;

        batch.draw(background, 0, 0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(platform,
                playerSquareMiddleX-platform.getRegionWidth()/2* Settings.SCALE,
                platformYOrigin-(50*Settings.SCALE),
                platform.getRegionWidth()*Settings.SCALE,
                platform.getRegionHeight()*Settings.SCALE);
        batch.draw(platform,
                opponentSquareMiddleX-platform.getRegionWidth()/2*Settings.SCALE,
                platformYOrigin,
                platform.getRegionWidth()*Settings.SCALE,
                platform.getRegionHeight()*Settings.SCALE);
        if(i==1){
            batch.draw(ball.getKeyFrame(elapsed),-60,120);
            //batch.draw(ball.getKeyFrame(elapsed),260,120);
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
        game.setScreen(new BattleScreen(game));
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
