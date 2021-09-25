package com.pokemon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.pokemon.model.Player;
import com.pokemon.model.Portal;
import com.pokemon.screen.GameScreen;
import com.pokemon.world.MainWorld;
import com.pokemon.world.Mine;
import com.pokemon.world.World;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;

public class WorldRestrict {
    private Player player;

    public WorldRestrict(Player player) {
        this.player = player;
    }

    public void update() {
        if (player.x < 0){
            player.x = 0;
        }
        if (player.y < 0) {
            player.y = 0;
        }
        if (player.x > GameScreen.getWorld().getMap().getWidth() * SCALED_TILE_SIZE - SCALED_TILE_SIZE) {
            player.x = GameScreen.getWorld().getMap().getWidth() * SCALED_TILE_SIZE - SCALED_TILE_SIZE;
        }
        if (player.y > GameScreen.getWorld().getMap().getHeight() * SCALED_TILE_SIZE - SCALED_TILE_SIZE){
            player.y = GameScreen.getWorld().getMap().getHeight() * SCALED_TILE_SIZE - SCALED_TILE_SIZE;
        }

        // 좌표 디버깅용
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println(GameScreen.getWorld().getMap().getTile((int)(player.x/32),(int)(player.y/32)));
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            GameScreen.setWorld(new Mine());
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            GameScreen.setWorld(new MainWorld());
        }

    }

}
