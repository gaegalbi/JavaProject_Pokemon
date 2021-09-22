package com.pokemon.model;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Tile {
    private int x,y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
