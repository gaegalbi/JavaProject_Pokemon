package com.pokemon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.pokemon.model.DIRECTION;
import com.pokemon.model.Player;
import com.pokemon.model.WorldObject;
import com.pokemon.screen.GameScreen;
import com.pokemon.world.MainWorld;
import com.pokemon.world.Mine;
import com.pokemon.world.World;

import static com.pokemon.game.Settings.PLAYER_MOVE_SPEED;
import static com.pokemon.game.Settings.SCALED_TILE_SIZE;

public class PlayerController extends InputAdapter {
    private final Player player;
    private float tempX,tempY;


    public PlayerController(Player player) {
        this.player = player;
    }

    public void update() {
        player.setState(Player.PLAYER_STATE.STANDING);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setFacing(DIRECTION.WEST);
            player.setState(Player.PLAYER_STATE.WALKING);
            tempX = player.x;
            player.x -= PLAYER_MOVE_SPEED * Gdx.graphics.getDeltaTime();
            for (WorldObject object : GameScreen.getWorld().getObjects()) {
                if (object.overlaps(player)) {
                    player.x = tempX;
                }
            }

        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setFacing(DIRECTION.EAST);
            player.setState(Player.PLAYER_STATE.WALKING);
            tempX = player.x;
            player.x += PLAYER_MOVE_SPEED * Gdx.graphics.getDeltaTime();
            for (WorldObject object : GameScreen.getWorld().getObjects()) {
                if (object.overlaps(player)) {
                    player.x = tempX;
                }
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.setFacing(DIRECTION.NORTH);
            player.setState(Player.PLAYER_STATE.WALKING);
            tempY = player.y;
            player.y += PLAYER_MOVE_SPEED * Gdx.graphics.getDeltaTime();
            for (WorldObject object : GameScreen.getWorld().getObjects()) {
                if (object.overlaps(player)) {
                    player.y = tempY;
                }
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setFacing(DIRECTION.SOUTH);
            player.setState(Player.PLAYER_STATE.WALKING);
            tempY = player.y;
            player.y -= PLAYER_MOVE_SPEED * Gdx.graphics.getDeltaTime();
            for (WorldObject object : GameScreen.getWorld().getObjects()) {
                if (object.overlaps(player)) {
                    player.y = tempY;
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            System.out.println("player.x = " + player.x);
            System.out.println("player.y = " + player.y);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println(GameScreen.getWorld().getMap().getTile((int)(player.x/32),(int)(player.y/32)));
        }

 /*       if(Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            GameScreen.setWorld(new Mine(player));
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            GameScreen.setWorld(new MainWorld(player));
        }*/
    }
}
