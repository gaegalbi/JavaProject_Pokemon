package com.pokemon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.pokemon.game.Pokemon;
import com.pokemon.inventory.Item;
import com.pokemon.model.*;
import com.pokemon.screen.GameScreen;

import static com.pokemon.game.Settings.PLAYER_MOVE_SPEED;

public class PlayerController extends InputAdapter {
    private final Player player;
    private float tempX,tempY;
    private Rectangle hitRange;
    private GameScreen gameScreen;
    private float percent;
    private float ready;
    private boolean isReady;

    public Rectangle getHitRange() {
        return hitRange;
    }

    public PlayerController(Player player, GameScreen gameScreen) {
        this.player = player;
        this.gameScreen = gameScreen;
        hitRange = new Rectangle(0, 0, 32, 32);
    }

    public void isReadyPick(float delta) {
        if (ready >= 0.5f) {
            ready = 0;
            isReady = true;
        }
        ready += delta;
    }

    public void pickRock() {
        int playerLevel = player.getLV();
        float percent = MathUtils.random();
        if (playerLevel < 10) {
            if (percent < 0.2f) {
                System.out.println("철 획득");
            } else {
                System.out.println("돌 획득");
            }
        } else if (playerLevel < 20){
            if (percent < 0.2f) {
                System.out.println("금 획득");
            } else if (percent < 0.5f) {
                System.out.println("철 획득");
            } else {
                System.out.println("돌 획득");
            }
        } else if (playerLevel < 30){
            if (percent < 0.3f) {
                System.out.println("금 획득");
            } else {
                System.out.println("철 획득");
            }
        }
    }

    public void pickGrass() {
        int playerLevel = player.getLV();
        float percent = MathUtils.random();
        if (playerLevel < 10) {
            if (percent < 0.2f) {
                System.out.println("허브 획득");
            } else {
                System.out.println("잡초 획득");
            }
        } else if (playerLevel < 20){
            if (percent < 0.2f) {
                System.out.println("산삼 획득");
            } else if (percent < 0.5f) {
                System.out.println("허브 획득");
            } else {
                System.out.println("잡초 획득");
            }
        } else if (playerLevel < 30){
            if (percent < 0.3f) {
                System.out.println("산삼 획득");
            } else {
                System.out.println("허브 획득");
            }
        }
    }

    public void pickWood() {
        int playerLevel = player.getLV();
        float percent = MathUtils.random();
        if (playerLevel < 10) {
            if (percent < 0.2f) {
                System.out.println("목재 획득");
            } else {
                System.out.println("나뭇가지 획득");
            }
        } else if (playerLevel < 20){
            if (percent < 0.2f) {
                System.out.println("단단한목재 획득");
            } else if (percent < 0.5f) {
                System.out.println("목재 획득");
            } else {
                System.out.println("나뭇가지 획득");
            }
        } else if (playerLevel < 30){
            if (percent < 0.3f) {
                System.out.println("단단한목재 획득");
            } else {
                System.out.println("목재 획득");
            }
        }
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
        if (!isReady) {
            isReadyPick(delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            hitRange.setPosition(player.x + player.getFacing().getDx() * 32, player.y + player.getFacing().getDy() * 32);
            if (player.equips.equips[4] != null && isReady && player.getState() == Player.PLAYER_STATE.STANDING) {
                isReady = false;
                for (RenderHelper object : GameScreen.getWorld().getObjects()) {
                    if (hitRange.overlaps((Rectangle) object)) {
                        percent = MathUtils.random();
                        switch (object.getName()) {
                            case "rock":
                                if (player.equips.equips[4].name.equals("나무곡괭이")) {
                                    if (percent < 0.8f) {
                                        gameScreen.getEffects().add(new Effect(0.43f, false));
                                    } else {
                                        gameScreen.getEffects().add(new Effect(0.3f, true));
                                        pickRock();
                                    }
                                }
                                break;
                            case "wood":
                                if (player.equips.equips[4].name.equals("나무도끼")) {
                                    if (percent < 0.8f) {
                                        gameScreen.getEffects().add(new Effect(0.43f, false));
                                    } else {
                                        gameScreen.getEffects().add(new Effect(0.3f, true));
                                        pickWood();
                                    }
                                }
                                break;
                            case "grass":
                                if (player.equips.equips[4].name.equals("나무괭이")) {
                                    if (percent < 0.8f) {
                                        gameScreen.getEffects().add(new Effect(0.43f, false));
                                    } else {
                                        gameScreen.getEffects().add(new Effect(0.3f, true));
                                        pickGrass();
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }
    }
}
