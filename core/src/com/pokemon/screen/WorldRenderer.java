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
    public void render(Batch batch) {
        for (int x = 0; x < GameScreen.getWorld().getMap().getWidth(); x++) {
            for (int y = 0; y < GameScreen.getWorld().getMap().getHeight(); y++) {
                batch.draw(GameScreen.getWorld().getMap().getTile(x, y).getTex(),
                        x * Settings.SCALED_TILE_SIZE,
                        y * Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE
                );
            }
        }
    }
}
