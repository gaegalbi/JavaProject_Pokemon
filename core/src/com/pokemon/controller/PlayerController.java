package com.pokemon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.pokemon.model.DIRECTION;
import com.pokemon.model.Player;
import com.pokemon.world.World;

import static com.pokemon.game.Settings.PLAYER_MOVE_SPEED;
import static com.pokemon.game.Settings.SCALED_TILE_SIZE;

public class PlayerController extends InputAdapter {
    private final Player player;


    public PlayerController(Player player) {
        this.player = player;
    }

    public void update() {
        player.setState(Player.PLAYER_STATE.STANDING);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setFacing(DIRECTION.WEST);
            player.setState(Player.PLAYER_STATE.WALKING);
            player.x -= PLAYER_MOVE_SPEED * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setFacing(DIRECTION.EAST);
            player.setState(Player.PLAYER_STATE.WALKING);
            player.x += PLAYER_MOVE_SPEED * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.setFacing(DIRECTION.NORTH);
            player.setState(Player.PLAYER_STATE.WALKING);
            player.y += PLAYER_MOVE_SPEED * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setFacing(DIRECTION.SOUTH);
            player.setState(Player.PLAYER_STATE.WALKING);
            player.y -= PLAYER_MOVE_SPEED * Gdx.graphics.getDeltaTime();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            System.out.println("player.x = " + player.x);
            System.out.println("player.y = " + player.y);
        }
    }
}
