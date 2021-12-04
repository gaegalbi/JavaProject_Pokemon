package com.pokemon.model;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static com.pokemon.world.World.atlas;

public class WorldObject extends Rectangle implements RenderHelper, Comparable<RenderHelper> {
    private TextureAtlas.AtlasRegion texture;

    public WorldObject(float x, float y, float width, float height, String texture) {
        super(x, y, width, height);
        this.texture = atlas.findRegion(texture);
    }

    public WorldObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.texture = atlas.findRegion("null");
    }

    @Override
    public TextureRegion getSprites() {
        return texture;
    }

    @Override
    public float getSizeX() {
        return width;
    }

    @Override
    public float getSizeY() {
        return height;
    }

    @Override
    public int compareTo(RenderHelper o) {
        return (int) (y-o.getY());
    }

    @Override
    public String toString() {
        return "WorldObject{" +
                "texture=" + texture +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
