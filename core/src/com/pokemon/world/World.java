package com.pokemon.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.pokemon.model.Portal;
import com.pokemon.model.TileMap;

public interface World {

    public TileMap getMap();

    public String getTexName();

}
