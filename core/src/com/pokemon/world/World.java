package com.pokemon.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.model.RenderHelper;
import com.pokemon.model.TileMap;
import com.pokemon.model.WorldObject;

import java.util.ArrayList;

public interface World {
    TextureAtlas atlas = new TextureAtlas("texture/texture.atlas");
    TextureAtlas backGround = new TextureAtlas("maps/background.atlas");
    ArrayList<RenderHelper> renderList = new ArrayList<>();

    TileMap getMap();

    ArrayList<WorldObject> getCollisionObjects();

    ArrayList<WorldObject> getObjects();

    String getBackground();

    void update();
}
