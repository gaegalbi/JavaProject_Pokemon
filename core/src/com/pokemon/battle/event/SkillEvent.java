package com.pokemon.battle.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import com.pokemon.game.Pokemon;
import com.pokemon.multibattle.MultiBattleScreen;
import com.pokemon.util.GifDecoder;

public class SkillEvent{
    Pokemon game;
    Animation<TextureRegion> playeranimation;
    Animation<TextureRegion> enemyanimation;
    int count=0;
    int enemyCount=0;
    int time = 1;
    int time2 = 2;
    int time3 = 3;
    float elapsed;
    float elapsed2;
    boolean turn;
    SpriteBatch batch;
    MultiBattleScreen multiBattleScreen;
    public SkillEvent(final MultiBattleScreen mu,SpriteBatch batch, final Pokemon game, String playerType, String oppentType, boolean turn){
        this.multiBattleScreen = mu;
        this.game = game;
        this.turn = turn;
        this.batch = batch;
        playeranimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("multibattle/"+playerType+".gif").read());
        enemyanimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("multibattle/"+oppentType+".gif").read());
        //playeranimation.isAnimationFinished(3);
    }
    public void effectSkill(){
        //elapsed += Gdx.graphics.getDeltaTime();
        if(turn){
            if(count == 1) {
                elapsed += Gdx.graphics.getDeltaTime();
                batch.draw(playeranimation.getKeyFrame(elapsed), 500.0f, 200.0f);
            }
            if(enemyCount == 1) {
                elapsed2 += Gdx.graphics.getDeltaTime();
                batch.draw(enemyanimation.getKeyFrame(elapsed2), 130.0f, 200.0f);
            }
            if(game.isOnoff()) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        count = 1;
                    }
                }, time);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        enemyCount = 1;
                    }
                }, time2);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        isFinised();
                    }
                }, time3);
            }
            System.out.println(count + ", "+enemyCount);

        }else{
            if(enemyCount == 1) {
                elapsed += Gdx.graphics.getDeltaTime();
                batch.draw(enemyanimation.getKeyFrame(elapsed), 130.0f, 200.0f);
            }
            if(count == 1) {
                elapsed2 += Gdx.graphics.getDeltaTime();
                batch.draw(playeranimation.getKeyFrame(elapsed2), 500.0f, 200.0f);
            }
            if(game.isOnoff()) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        enemyCount = 1;
                    }
                }, time);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        count = 1;
                    }
                }, time2);
                System.out.println(count + ", "+enemyCount);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        isFinised();
                    }
                }, time3);
            }
        }
    }

    public void isFinised(){
        count = 0;
        enemyCount = 0;
        Timer.instance().clear();
        multiBattleScreen.setSkillcount(1);
        game.setOnoff(false);
    }

}
