package com.pokemon.model;

import com.badlogic.gdx.math.Rectangle;
import com.pokemon.game.Settings;
import com.pokemon.world.World;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;
import static com.pokemon.world.World.atlas;

public class Portal extends Rectangle {
    private World world;
    private int goX,goY;

    public Portal(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.x *= SCALED_TILE_SIZE;
        this.y *= SCALED_TILE_SIZE;
        this.width *= SCALED_TILE_SIZE;
        this.height *= SCALED_TILE_SIZE;
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
