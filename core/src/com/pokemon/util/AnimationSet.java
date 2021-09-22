package com.pokemon.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.pokemon.model.DIRECTION;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class AnimationSet<TextureRegion> {

    private final Map<DIRECTION, Animation<TextureRegion>> walking;
    private final Map<DIRECTION, TextureRegion> standing;

    public AnimationSet(Animation<TextureRegion> walkNorth,
                        Animation<TextureRegion> walkSouth,
                        Animation<TextureRegion> walkEast,
                        Animation<TextureRegion> walkWest,
                        TextureRegion standNorth,
                        TextureRegion standSouth,
                        TextureRegion standEast,
                        TextureRegion standWest ) {
        walking = new HashMap<>();
        walking.put(DIRECTION.NORTH, walkNorth);
        walking.put(DIRECTION.SOUTH, walkSouth);
        walking.put(DIRECTION.EAST, walkEast);
        walking.put(DIRECTION.WEST, walkWest);
        standing = new Hashtable<>();
        standing.put(DIRECTION.NORTH, standNorth);
        standing.put(DIRECTION.SOUTH, standSouth);
        standing.put(DIRECTION.EAST, standEast);
        standing.put(DIRECTION.WEST, standWest);
    }

    public Animation<TextureRegion> getWalking(DIRECTION dir){
        return walking.get(dir);
    }

    public TextureRegion getStanding(DIRECTION dir){
        return standing.get(dir);
    }
}