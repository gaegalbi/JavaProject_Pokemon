package com.pokemon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.pokemon.model.Player;
import com.pokemon.model.Portal;
import com.pokemon.world.World;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;

public class WorldRestrict {
    private World world;
    private Player player;

    public WorldRestrict(World world,Player player) {
        this.world = world;
        this.player = player;
    }

    public void update() {
        if (player.x < 0){
            player.x = 0;
        }
        if (player.y < 0) {
            player.y = 0;
        }
        if (player.x > world.getMap().getWidth() * SCALED_TILE_SIZE - SCALED_TILE_SIZE) {
            player.x = world.getMap().getWidth() * SCALED_TILE_SIZE - SCALED_TILE_SIZE;
        }
        if (player.y > world.getMap().getHeight() * SCALED_TILE_SIZE - SCALED_TILE_SIZE){
            player.y = world.getMap().getHeight() * SCALED_TILE_SIZE - SCALED_TILE_SIZE;
        }

        // 좌표 디버깅용
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println(world.getMap().getTile((int)(player.x/32),(int)(player.y/32)));
        }

    }

    public void setWorld(World world) {
        this.world = world;
    }
}
