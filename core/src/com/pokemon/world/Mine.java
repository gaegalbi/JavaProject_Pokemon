package com.pokemon.world;

import com.badlogic.gdx.utils.Array;
import com.pokemon.model.Portal;
import com.pokemon.model.Tile;
import com.pokemon.model.TileMap;

public class Mine implements World{

    private final TileMap map = new TileMap(10,10);
    private String texName = "stone";

    public Mine() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.tiles[x][y] = new Tile(x,y);
            }
        }
    }

    @Override
    public TileMap getMap() {
        return null;
    }

    @Override
    public String getTexName() {
        return null;
    }
}
