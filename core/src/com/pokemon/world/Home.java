package com.pokemon.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.game.Pokemon;
import com.pokemon.model.Player;
import com.pokemon.model.TileMap;
import com.pokemon.model.WorldObject;

import java.util.ArrayList;

public class Home implements World{
    private final TileMap map = new TileMap(9, 9);
    private Player player;
    private Pokemon game;
    private ArrayList<WorldObject> collisionObjects;
    private ArrayList<WorldObject> objects;

    @Override
    public TileMap getMap() {
        return null;
    }

    @Override
    public ArrayList<WorldObject> getCollisionObjects() {
        return null;
    }

    @Override
    public ArrayList<WorldObject> getObjects() {
        return null;
    }

    @Override
    public String getBackground() {
        return null;
    }

    @Override
    public void update() {

    }
}
