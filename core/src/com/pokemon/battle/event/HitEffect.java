package com.pokemon.battle.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import com.pokemon.game.Pokemon;
import com.pokemon.screen.GameScreen;
import com.pokemon.util.GifDecoder;

public class HitEffect {
    Pokemon game;
    SpriteBatch batch;
    GameScreen gameScreen;
//    Animation<TextureRegion> playeranimation;
    Texture texture;
    float elapsed;
    int count;

    public HitEffect(Pokemon game, GameScreen gameScreen) {
        this.game = game;
        this.batch = game.batch;
        this.gameScreen = gameScreen;
        texture = new Texture(Gdx.files.internal("texture/hit.png"));
//        playeranimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/노말.gif").read());
    }

    public void hitEffect(float x, float y) {
        batch.draw(texture, x, y);
    }
}
