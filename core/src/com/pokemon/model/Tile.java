package com.pokemon.model;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import static com.pokemon.world.World.atlas;

public class Tile {
    private int x, y;
    private TextureAtlas.AtlasRegion texture;

    public Tile(int x, int y, String tex) {
        this.x = x;
        this.y = y;
        this.texture = atlas.findRegion(tex);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TextureAtlas.AtlasRegion getTex() {
        return texture;
    }

    // 좌표 디버깅용
    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
