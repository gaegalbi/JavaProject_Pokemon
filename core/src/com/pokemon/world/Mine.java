package com.pokemon.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.model.Tile;
import com.pokemon.model.TileMap;

public class Mine implements World{

    private final TileMap map = new TileMap(10,10);
    private TextureAtlas.AtlasRegion tex = atlas.findRegion("stone");

    public Mine() {
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
    public TextureAtlas.AtlasRegion getTex() {
        return tex;
    }
}
