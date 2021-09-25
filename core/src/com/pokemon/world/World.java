package com.pokemon.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.model.TileMap;

public interface World {
    TextureAtlas atlas = new TextureAtlas("texture/texture.atlas");

    TileMap getMap();

    TextureAtlas.AtlasRegion getTex();

    void update();
}
