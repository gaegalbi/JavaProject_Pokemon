package com.pokemon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.pokemon.model.DIRECTION;
import com.pokemon.model.Player;
import com.pokemon.model.RenderHelper;
import com.pokemon.model.WorldObject;
import com.pokemon.screen.GameScreen;

import static com.pokemon.game.Settings.PLAYER_MOVE_SPEED;

public class PlayerController extends InputAdapter {
    private final Player player;
    private float tempX,tempY;
    public Rectangle hitRange;

    public PlayerController(Player player) {
        this.player = player;
        hitRange = new Rectangle(0, 0, 32, 32);
    }

    public void update() {
        player.setState(Player.PLAYER_STATE.STANDING);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setFacing(DIRECTION.WEST);
            player.setState(Player.PLAYER_STATE.WALKING);
            tempX = player.x;
            player.x -= PLAYER_MOVE_SPEED * Gdx.graphics.getDeltaTime();
            for (WorldObject object : GameScreen.getWorld().getCollisionObjects()) {
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
            for (WorldObject object : GameScreen.getWorld().getCollisionObjects()) {
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
            for (WorldObject object : GameScreen.getWorld().getCollisionObjects()) {
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
            for (WorldObject object : GameScreen.getWorld().getCollisionObjects()) {
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            hitRange.setPosition(player.x + player.getFacing().getDx() * 32, player.y + player.getFacing().getDy() * 32);
            for (RenderHelper object : GameScreen.getWorld().getObjects()) {
                if (hitRange.overlaps((Rectangle) object)) {
                    switch (object.getName()) {
                        case "rock":
                            System.out.println("돌캐기");
                            break;
                        case "wood":
                            System.out.println("나무캐기");
                            break;
                        case "grass":
                            System.out.println("풀베기");
                            break;
                    }
                }
            }
        }
 /*       if(Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            GameScreen.setWorld(new Mine(player));
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            GameScreen.setWorld(new MainWorld(player));
        }*/
    }
}
