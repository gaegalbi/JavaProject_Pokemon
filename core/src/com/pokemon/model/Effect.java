package com.pokemon.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pokemon.util.GifDecoder;

public class Effect {
    private float timer;
    private float duration;
    private static Animation<TextureRegion> hitEffect = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("getItem/hit.gif").read());
    private static Animation<TextureRegion> getEffect = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("getItem/get.gif").read());
    private Animation<TextureRegion> effect;

    public Effect(float duration,boolean isGet) {
        this.duration = duration;
        if (isGet) {
            this.effect = getEffect;
        } else {
            this.effect = hitEffect;
        }
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


