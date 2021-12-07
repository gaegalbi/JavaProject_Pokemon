package com.pokemon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.pokemon.db.db;
import com.pokemon.game.Pokemon;
import com.pokemon.game.Sound;
import com.pokemon.inventory.Item;
import com.pokemon.model.*;
import com.pokemon.screen.GameScreen;

import java.util.StringTokenizer;

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
                System.out.println("철 1개 획득");
                db.ITEM_UPDATE("ITEM_04",1);
                player.inventory.addItem("ITEM_04",1);
            } else {
                System.out.println("돌 1개 획득");
                db.ITEM_UPDATE("ITEM_02",1);
                player.inventory.addItem("ITEM_02",1);
            }
        } else if (playerLevel < 20){
            if (percent < 0.2f) {
                System.out.println("금 1개 획득");
                db.ITEM_UPDATE("ITEM_05",1);
                player.inventory.addItem("ITEM_05",1);
            } else if (percent < 0.5f) {
                System.out.println("철 5개 획득");
                db.ITEM_UPDATE("ITEM_04",5);
                player.inventory.addItem("ITEM_04",5);
            } else {
                System.out.println("돌 10개 획득");
                db.ITEM_UPDATE("ITEM_02",10);
                player.inventory.addItem("ITEM_02",10);
            }
        } else if (playerLevel < 30){
            if (percent < 0.1f) {
                System.out.println("다이아 1개 획득");
                db.ITEM_UPDATE("ITEM_06",1);
                player.inventory.addItem("ITEM_06",1);
            } else if(percent < 0.3f){
                System.out.println("금 5개 획득");
                db.ITEM_UPDATE("ITEM_05",5);
                player.inventory.addItem("ITEM_05",5);
            }
            else if(percent < 0.3f){
                System.out.println("철 10개 획득");
                db.ITEM_UPDATE("ITEM_04",10);
                player.inventory.addItem("ITEM_04",10);
            }
            else {
                System.out.println("돌 20개 획득");
                db.ITEM_UPDATE("ITEM_02",20);
                player.inventory.addItem("ITEM_02",20);
            }
        }
    }

    public void pickGrass() {
        int playerLevel = player.getLV();
        float percent = MathUtils.random();
        if (playerLevel < 10) {
            if (percent < 0.1f) {
                System.out.println("상처약 1개 획득");
                db.ITEM_UPDATE("ITEM_35",1);
                player.inventory.addItem("ITEM_35",1);
            }else if (percent < 0.3f) {
                System.out.println("풀 1개, 나무 1개 획득");
                db.ITEM_UPDATE("ITEM_01",1);
                db.ITEM_UPDATE("ITEM_03",1);
                player.inventory.addItem("ITEM_01",1);
                player.inventory.addItem("ITEM_03",1);
            } else{
                System.out.println("풀 1개 획득");
                db.ITEM_UPDATE("ITEM_01",1);
                player.inventory.addItem("ITEM_01",1);
            }
        } else if (playerLevel < 20){
            if (percent < 0.1f) {
                System.out.println("좋은상처약 1개 획득");
                db.ITEM_UPDATE("ITEM_36",1);
                player.inventory.addItem("ITEM_36",1);
            }else if (percent < 0.3f) {
                System.out.println("풀 5개, 나무 5개 획득");
                db.ITEM_UPDATE("ITEM_01",5);
                db.ITEM_UPDATE("ITEM_03",5);
                player.inventory.addItem("ITEM_01",5);
                player.inventory.addItem("ITEM_03",5);
            }  else if (percent < 0.5f) {
                System.out.println("풀 3개, 나무 3개 획득");
                db.ITEM_UPDATE("ITEM_01",3);
                db.ITEM_UPDATE("ITEM_03",3);
                player.inventory.addItem("ITEM_01",3);
                player.inventory.addItem("ITEM_03",3);
            } else {
                System.out.println("풀 1개 획득");
                db.ITEM_UPDATE("ITEM_01",5);
                player.inventory.addItem("ITEM_01",5);
            }
        } else if (playerLevel < 30){
            if (percent < 0.1f) {
                System.out.println("풀회복약 1개 획득");
                db.ITEM_UPDATE("ITEM_38",1);
                player.inventory.addItem("ITEM_38",1);
            } else if(percent < 0.3f){
                System.out.println("고급상처약 1개 획득");
                db.ITEM_UPDATE("ITEM_37",1);
                player.inventory.addItem("ITEM_37",1);
            }
            else if(percent < 0.3f){
                System.out.println("좋은상처약 1개 획득");
                db.ITEM_UPDATE("ITEM_36",1);
                player.inventory.addItem("ITEM_36",1);
            }
            else {
                System.out.println("풀 20개, 나무 10개 획득");
                db.ITEM_UPDATE("ITEM_01",20);
                db.ITEM_UPDATE("ITEM_03",10);
                player.inventory.addItem("ITEM_01",20);
                player.inventory.addItem("ITEM_03",3);
            }
        }
    }

    public void pickWood() {
        int playerLevel = player.getLV();
        float percent = MathUtils.random();
        if (playerLevel < 10) {
            if (percent < 0.1f) {
                System.out.println("몬스터볼 1개 획득");
                db.ITEM_UPDATE("ITEM_32",1);
                player.inventory.addItem("ITEM_32",1);
            }else if (percent < 0.3f) {
                System.out.println("나무 3개 획득");
                db.ITEM_UPDATE("ITEM_03",2);
                player.inventory.addItem("ITEM_03",2);
            } else{
                System.out.println("나무 1개 획득");
                db.ITEM_UPDATE("ITEM_03",1);
                player.inventory.addItem("ITEM_03",1);
            }
        } else if (playerLevel < 20){
            if (percent < 0.1f) {
                System.out.println("몬스터볼 2개 획득");
                db.ITEM_UPDATE("ITEM_32",2);
                player.inventory.addItem("ITEM_32",2);
            }else if (percent < 0.3f) {
                System.out.println("나무 10개 획득");
                db.ITEM_UPDATE("ITEM_03",10);
                player.inventory.addItem("ITEM_03",10);
            }  else if (percent < 0.5f) {
                System.out.println("나무 5개 획득");
                db.ITEM_UPDATE("ITEM_03",5);
                player.inventory.addItem("ITEM_03",5);
            } else {
                System.out.println("나무 3개 획득");
                db.ITEM_UPDATE("ITEM_03",3);
                player.inventory.addItem("ITEM_03",3);
            }
        } else if (playerLevel < 30){
            if (percent < 0.1f) {
                System.out.println("마스터볼 1개 획득");
                db.ITEM_UPDATE("ITEM_34",1);
                player.inventory.addItem("ITEM_34",1);
            } else if(percent < 0.3f){
                System.out.println("수퍼볼 1개 획득");
                db.ITEM_UPDATE("ITEM_33",1);
                player.inventory.addItem("ITEM_33",1);
            }
            else if(percent < 0.3f){
                System.out.println("몬스터볼 10개 획득");
                db.ITEM_UPDATE("ITEM_32",10);
                player.inventory.addItem("ITEM_32",10);
            }
            else {
                System.out.println("나무 20개 획득");
                db.ITEM_UPDATE("ITEM_03",20);
                player.inventory.addItem("ITEM_03",20);
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

//        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
//            System.out.println("player.x = " + player.x);
//            System.out.println("player.y = " + player.y);
//        }

//        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
//            System.out.println(GameScreen.getWorld().getMap().getTile((int)(player.x/32),(int)(player.y/32)));
//        }
        if (!isReady) {
            isReadyPick(delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X) && isReady) {
            hitRange.setPosition(player.x + player.getFacing().getDx() * 32, player.y + player.getFacing().getDy() * 32);
            if (player.equips.equips[4] != null && player.getState() == Player.PLAYER_STATE.STANDING) {
                isReady = false;
                for (RenderHelper object : GameScreen.getWorld().getObjects()) {
                    if (hitRange.overlaps((Rectangle) object)) {
                        percent = MathUtils.random();

                        StringTokenizer[] st = new StringTokenizer[]{
                                new StringTokenizer(player.equips.equips[4].name,"나무"),
                                new StringTokenizer(player.equips.equips[4].name,"철"),
                                new StringTokenizer(player.equips.equips[4].name,"금"),
                                new StringTokenizer(player.equips.equips[4].name,"다이아")};

                        switch (object.getName()) {
                            case "rock":
                                if (st[0].nextToken().equals("곡괭이")||st[1].nextToken().equals("곡괭이")||st[2].nextToken().equals("곡괭이")||st[3].nextToken().equals("곡괭")) {
                                   System.out.println(player.getLV());
                                    if (percent < 0.8f) {
                                        Sound.hitBlock.play();
                                        gameScreen.getEffects().add(new Effect(0.43f, false));
                                    } else {
                                        Sound.getStone.play();
                                        gameScreen.getEffects().add(new Effect(0.3f, true));
                                        pickRock();
                                        // 경험치 +1
                                        db.user_EXP_UP();
                                        player.setEXP(1);

                                        db.user_SK_EXP_UP();
                                        player.setSkillEXP(0,1);

                                        // 경험치 도달하면 레벨업
                                        if(db.user_EXP_get() == db.user_EXP_need()) {
                                            db.user_LV_UP();
                                            player.setLV(1);
                                        }

                                        if(db.user_SK_EXP_get() == db.user_SK_EXP_need()) {
                                            db.user_SK_LV_UP();
                                            player.setSkillLV(0,1);;
                                        }
                                    }
                                }
                                break;
                            case "wood":
                                if (st[0].nextToken().equals("도끼")||st[1].nextToken().equals("도끼")||st[2].nextToken().equals("도끼")||st[3].nextToken().equals("도끼")) {
                                    if (percent < 0.8f) {
                                        Sound.hitBlock.play();
                                        gameScreen.getEffects().add(new Effect(0.43f, false));
                                    } else {
                                        Sound.getWood.play();
                                        gameScreen.getEffects().add(new Effect(0.3f, true));
                                        pickWood();
                                        // 경험치 +1
                                        db.user_EXP_UP();
                                        player.setEXP(1);

                                        db.user_SK_EXP_UP();
                                        player.setSkillEXP(0,1);

                                        // 경험치 도달하면 레벨업
                                        if(db.user_EXP_get() == db.user_EXP_need()) {
                                            db.user_LV_UP();
                                            player.setLV(1);
                                        }
                                        if(db.user_SK_EXP_get() == db.user_SK_EXP_need()) {
                                            db.user_SK_LV_UP();
                                            player.setSkillLV(0,1);;
                                        }
                                    }
                                }
                                break;
                            case "grass":
                                if (st[0].nextToken().equals("괭이")||st[1].nextToken().equals("괭이")||st[2].nextToken().equals("괭이")||st[3].nextToken().equals("괭")) {
                                    if (percent < 0.8f) {
                                        Sound.hitBlock.play();
                                        gameScreen.getEffects().add(new Effect(0.43f, false));
                                    } else {
                                        Sound.getGrass.play();
                                        gameScreen.getEffects().add(new Effect(0.3f, true));
                                        pickGrass();
                                        // 경험치 +1
                                        db.user_EXP_UP();
                                        player.setEXP(1);

                                        db.user_SK_EXP_UP();
                                        player.setSkillEXP(0,1);

                                        // 경험치 도달하면 레벨업
                                        if(db.user_EXP_get() == db.user_EXP_need()) {
                                            db.user_LV_UP();
                                            player.setLV(1);
                                        }
                                        if(db.user_SK_EXP_get() == db.user_SK_EXP_need()) {
                                            db.user_SK_LV_UP();
                                            player.setSkillLV(0,1);;
                                        }
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
