package com.pokemon.model;

import com.badlogic.gdx.math.MathUtils;

public class TileMap {
    private final int width;
    private final int height;
    public Tile[][] tiles;
    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
    }
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
