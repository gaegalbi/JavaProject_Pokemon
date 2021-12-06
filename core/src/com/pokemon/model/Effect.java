package com.pokemon.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pokemon.util.GifDecoder;

public class Effect {
    private float timer;
    private float duration;
    private static Animation<TextureRegion> effect = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/hit.gif").read());

    public Effect(float duration) {
        this.duration = duration;
    }

    public boolean update(float delta) {
        timer += delta;
        return timer > duration;
    }

    public Animation<TextureRegion> getEffect() {
        return effect;
    }

    public float getTimer() {
        return timer;
    }
}


