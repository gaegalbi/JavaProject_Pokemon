package com.pokemon.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.pokemon.model.Portal;
import com.pokemon.model.Tile;
import com.pokemon.model.TileMap;

public class MainWorld implements World{

    private final TileMap map = new TileMap(20,20);
    private String texName = "grass";

    public MainWorld() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.tiles[x][y] = new Tile(x,y);
            }
        }
    }

    @Override
    public TileMap getMap() {
        return map;
    }

    @Override
    public String getTexName() {
        return texName;
    }
}
