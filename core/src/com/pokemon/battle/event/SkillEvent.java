package com.pokemon.battle.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import com.pokemon.battle.BATTLE_PARTY;
import com.pokemon.battle.Battle;
import com.pokemon.game.Pokemon;
import com.pokemon.screen.BattleRenderer;
import com.pokemon.screen.BattleScreen;
import com.pokemon.util.GifDecoder;

public class SkillEvent {
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
    float x,y;
    boolean turn;
    SpriteBatch batch;
    //MultiBattleScreen multiBattleScreen;
    BattleScreen battleScreen;
    Battle battle;
    public SkillEvent(BattleScreen battleScreen, Battle battle, SpriteBatch batch, final Pokemon game, String playerType, String oppentType, boolean turn){
        this.battleScreen = battleScreen;
        this.game = game;
        this.turn = turn;
        this.batch = batch;
        this.battle = battle;
        playeranimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/"+playerType+".gif").read());
        enemyanimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/"+oppentType+".gif").read());
        //playeranimation.isAnimationFinished(3);
    }
    public void effectSkill(){
        //elapsed += Gdx.graphics.getDeltaTime();
        if(turn){
            if(count == 1&&battle.getInput()!=4&&battle.getUDamage()!=0&&!battle.getP_P().isFainted()) {
                elapsed += Gdx.graphics.getDeltaTime();
                batch.draw(playeranimation.getKeyFrame(elapsed), 480.0f, 280.0f);
            }
            if(enemyCount == 1&&!battle.getO_P().isCapture()&&battle.getODamage()!=0&&!battle.getO_P().isFainted()) {
                elapsed2 += Gdx.graphics.getDeltaTime();
                batch.draw(enemyanimation.getKeyFrame(elapsed2), 150.0f, 180.0f);
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
        }else{
            if(enemyCount == 1) {
                elapsed += Gdx.graphics.getDeltaTime();
                batch.draw(enemyanimation.getKeyFrame(elapsed), 150.0f, 180.0f);
            }
            if(count == 1&&battle.getInput()!=4) {
                elapsed2 += Gdx.graphics.getDeltaTime();
                batch.draw(playeranimation.getKeyFrame(elapsed2), 480.0f, 230.0f);
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
        battleScreen.setSkillcount(1);
        game.setOnoff(false);
    }

}
