package com.pokemon.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class WorldObject extends Rectangle {
    private Texture texture;

    public WorldObject(float x, float y, float width, float height, String texture) {
        super(x, y, width, height);
        this.texture = new Texture(Gdx.files.internal(texture));
    }

    public Texture getTexture() {
        return texture;
    }
}
