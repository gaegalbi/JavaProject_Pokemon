package com.pokemon.util;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.pokemon.game.Settings;
import com.pokemon.model.WorldObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;
import static com.pokemon.world.World.atlas;

public class ObjectGenerator {
    public static ArrayList<WorldObject> generateObject(String file) {
        String[] objects;
        FileHandle mapFile = Gdx.files.internal("maps/" + file + ".txt");
        ArrayList<WorldObject> objectList = new ArrayList<>();
        objects = mapFile.readString().split("\n");

        for (String object : objects) {
            String[] tempObject = object.split(",");
            objectList.add(
                    new WorldObject(
                            Float.parseFloat(tempObject[0]) * SCALED_TILE_SIZE,
                            Float.parseFloat(tempObject[1]) * SCALED_TILE_SIZE,
                            Integer.parseInt(tempObject[2]) * SCALED_TILE_SIZE,
                            Integer.parseInt(tempObject[3]) * SCALED_TILE_SIZE,
                            tempObject[4].trim()
                    )
            );
        }
        return objectList;
    }
}
