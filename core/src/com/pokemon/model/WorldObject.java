package com.pokemon.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static com.pokemon.world.World.atlas;

public class WorldObject extends Rectangle implements RenderHelper, Comparable<RenderHelper> {
    private Texture texture;
    private TextureAtlas.AtlasRegion textures;

    public WorldObject(float x, float y, float width, float height, TextureAtlas.AtlasRegion texture) {
        super(x, y, width, height);
        this.textures = texture;
    }

    public WorldObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.texture = new Texture(Gdx.files.internal("texture/null.png"));
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public TextureRegion getSprites() {
        return textures;
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
}
