package com.pokemon.screen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.pokemon.game.Settings;
import com.pokemon.model.Player;
import com.pokemon.model.RenderHelper;
import com.pokemon.model.WorldObject;
import com.pokemon.world.World;

import java.util.Collections;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;
import static com.pokemon.world.World.renderList;

public class WorldRenderer {
    private Player player;


    public WorldRenderer(Player player) {
        this.player = player;
    }

    public void render(Batch batch) {

        batch.draw(World.backGround.findRegion(GameScreen.getWorld().getBackground()), 0, 0, GameScreen.getWorld().getMap().getWidth()*SCALED_TILE_SIZE, GameScreen.getWorld().getMap().getHeight()*SCALED_TILE_SIZE);

        Collections.sort(renderList,new WorldObjectYComparator());
        for (RenderHelper renderHelper : renderList) {
            batch.draw(renderHelper.getSprites(), renderHelper.getX(), renderHelper.getY(), renderHelper.getSizeX(), renderHelper.getSizeY());
        }
    }
}
