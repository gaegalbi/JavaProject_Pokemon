package com.pokemon.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;

public class BattleArea extends Rectangle {
    private static float battleChance;
    private static float value;
    private float tempX,tempY;

    public BattleArea(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.x *= SCALED_TILE_SIZE;
        this.y *= SCALED_TILE_SIZE;
        this.width *= SCALED_TILE_SIZE;
        this.height *= SCALED_TILE_SIZE;
        if (battleChance == 0) {
            resetChance();
        }
    }

    public void resetChance() {
        value = 0;
        tempX = 0;
        tempY = 0;
        battleChance = MathUtils.random() * 10 + 3;
        System.out.println("battleChance: " + battleChance);
    }

    public boolean battleStarter(float delta, float x, float y) {
        if (tempX == x && tempY == y) {
            tempX = x;
            tempY = y;
            return false;
        }
        tempX = x;
        tempY = y;
        value += delta;
        System.out.println("Value:" + value);
        if (value > battleChance) {
            resetChance();
            return true;
        }
        return false;
    }
}
