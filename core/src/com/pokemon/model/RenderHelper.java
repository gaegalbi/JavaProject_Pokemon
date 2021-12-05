package com.pokemon.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface RenderHelper {
    float getX();

    float getY();

    TextureRegion getSprites();

    float getSizeX();

    float getSizeY();

    String getName();
}
