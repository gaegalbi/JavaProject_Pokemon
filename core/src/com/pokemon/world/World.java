package com.pokemon.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.model.RenderHelper;
import com.pokemon.model.TileMap;
import com.pokemon.model.WorldObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public interface World {
    TextureAtlas atlas = new TextureAtlas("texture/texture.atlas");
    ArrayList<RenderHelper> renderList = new ArrayList<>();
    ArrayList<WorldObject> fakeRenderList = new ArrayList<>();

    TileMap getMap();

    ArrayList<WorldObject> getCollisionObjects();

    ArrayList<WorldObject> getObjects();

    void update();
}
