package com.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pokemon.game.Settings;
import com.pokemon.model.Player;
import com.pokemon.world.World;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;

public class WorldRenderer {
    private AssetManager assetManager;
    private World world;
    private TextureAtlas atlas = new TextureAtlas("texture/texture.atlas");

    private TextureAtlas.AtlasRegion tex;

    public WorldRenderer(World world) {
        this.world = world;
        tex = atlas.findRegion(world.getTexName());
    }

    public void render(Batch batch) {
        for (int x = 0; x < world.getMap().getWidth(); x++) {
            for (int y = 0; y < world.getMap().getHeight(); y++) {
                batch.draw(tex,
                        x * Settings.SCALED_TILE_SIZE,
                        y * Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE
                );
            }
        }
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
