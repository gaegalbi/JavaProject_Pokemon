package com.pokemon.model;

import com.badlogic.gdx.math.Rectangle;
import com.pokemon.game.Settings;
import com.pokemon.world.World;

public class Portal extends Rectangle {
    private World world;
    private int goX,goY;

    public Portal(float x, float y, World world, int goX, int goY) {
        super(x, y, Settings.TILE_SIZE, Settings.TILE_SIZE);
        this.world = world;
        this.goX = goX;
        this.goY = goY;
    }

    public World getWorld() {
        return world;
    }

    public int getGoX() {
        return goX;
    }

    public int getGoY() {
        return goY;
    }
}
