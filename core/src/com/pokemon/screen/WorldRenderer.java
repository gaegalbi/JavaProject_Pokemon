package com.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.pokemon.game.Settings;
import com.pokemon.model.Player;
import com.pokemon.model.RenderHelper;
import com.pokemon.model.WorldObject;
import com.pokemon.world.World;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Stack;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;
import static com.pokemon.world.World.renderQueue;

public class WorldRenderer {
    private Player player;


    public WorldRenderer(Player player) {
        this.player = player;
    }

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
        for (WorldObject object : GameScreen.getWorld().getObjects()) {
            batch.draw(object.getSprites(), object.x,object.y,object.width,object.height);
        }
        Collections.sort(renderQueue,new WorldObjectYComparator());
        for (RenderHelper renderHelper : renderQueue) {
            batch.draw(renderHelper.getSprites(), renderHelper.getX(), renderHelper.getY(), renderHelper.getSizeX(), renderHelper.getSizeY());
        }
        //batch.draw(player.getSprites(), player.x, player.y, player.getSizeX(), player.getSizeY());
    }
}
