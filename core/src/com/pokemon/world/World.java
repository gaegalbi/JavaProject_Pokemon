package com.pokemon.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.model.TileMap;
import com.pokemon.model.WorldObject;

import java.util.ArrayList;

public interface World {
    TextureAtlas atlas = new TextureAtlas("texture/texture.atlas");

    TileMap getMap();

    ArrayList<WorldObject> getObjects();

    void update();
}
