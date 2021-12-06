package com.pokemon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.pokemon.game.Pokemon;
import com.pokemon.inventory.Item;
import com.pokemon.model.*;
import com.pokemon.screen.GameScreen;

import static com.pokemon.game.Settings.PLAYER_MOVE_SPEED;

public class PlayerController extends InputAdapter {
    private final Player player;
    private float tempX,tempY;
    public Rectangle hitRange;
    public GameScreen gameScreen;

    public PlayerController(Player player, GameScreen gameScreen) {
        this.player = player;
        this.gameScreen = gameScreen;
        hitRange = new Rectangle(0, 0, 32, 32);
    }

    public void update(float delta) {
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
            if (player.equips.equips[4] != null && gameScreen.getEffects().isEmpty() && player.getState() == Player.PLAYER_STATE.STANDING) {
                for (RenderHelper object : GameScreen.getWorld().getObjects()) {
                    if (hitRange.overlaps((Rectangle) object)) {
                        switch (object.getName()) {
                            case "rock":
                                if (player.equips.equips[4].name.equals("나무곡괭이")) {
                                    gameScreen.getEffects().add(new Effect(0.2f));
                                    System.out.println("돌캐기");
                                }
                                break;
                            case "wood":
                                if (player.equips.equips[4].name.equals("나무도끼")) {
                                    gameScreen.getEffects().add(new Effect(0.2f));
                                    System.out.println("나무캐기");
                                }
                                break;
                            case "grass":
                                if (player.equips.equips[4].name.equals("나무괭이")) {
                                    gameScreen.getEffects().add(new Effect(0.2f));
                                    System.out.println("풀베기");
                                }
                                break;
                        }
                    }
                }
            }
        }
    }
}
