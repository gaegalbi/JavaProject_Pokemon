package com.pokemon.screen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.pokemon.game.Settings;
import com.pokemon.model.Player;
import com.pokemon.model.RenderHelper;
import com.pokemon.model.WorldObject;

import java.util.Collections;

import static com.pokemon.world.World.fakeRenderList;
import static com.pokemon.world.World.renderList;

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
        for (WorldObject object : GameScreen.getWorld().getCollisionObjects()) {
            batch.draw(object.getSprites(), object.x,object.y,object.width,object.height);
        }
        Collections.sort(renderList,new WorldObjectYComparator());
        for (RenderHelper renderHelper : renderList) {
            batch.draw(renderHelper.getSprites(), renderHelper.getX(), renderHelper.getY(), renderHelper.getSizeX(), renderHelper.getSizeY());
        }
        for (WorldObject worldObject : fakeRenderList) {
            batch.draw(worldObject.getSprites(), worldObject.getX(), worldObject.getY(), worldObject.getSizeX(), worldObject.getSizeY());
        }
        //batch.draw(player.getSprites(), player.x, player.y, player.getSizeX(), player.getSizeY());
    }
}
