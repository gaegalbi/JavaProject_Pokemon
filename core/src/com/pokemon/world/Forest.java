package com.pokemon.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.pokemon.game.Pokemon;
import com.pokemon.model.*;
import com.pokemon.screen.GameScreen;
import com.pokemon.util.ObjectGenerator;

import java.util.ArrayList;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;

public class Forest implements World {
    private final TileMap map = new TileMap(24, 20);
    private GameScreen gameScreen;
    private Player player;
    private Pokemon game;
    private ArrayList<WorldObject> collisionObjects;
    private Portal mainWorldPortal;

    public Forest(Player player, Pokemon game, GameScreen gameScreen) {
        this.player = player;
        this.game = game;
        this.gameScreen = gameScreen;

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.tiles[x][y] = new Tile(x, y);
            }
        }

        collisionObjects = ObjectGenerator.generateCollisionObject("ForestCollision");
        renderList.clear();
        renderList.add(player);
        renderList.addAll(ObjectGenerator.generateObject("Forest"));

        mainWorldPortal = new Portal(0,13,1,4);
    }

    @Override
    public TileMap getMap() {
        return map;
    }

    @Override
    public ArrayList<WorldObject> getCollisionObjects() {
        return collisionObjects;
    }

    @Override
    public String getBackground() {
        return "Forest";
    }

    @Override
    public void update() {
        if (player.x < 0) {
            player.x = 0;
        }
        if (player.y < 0) {
            player.y = 0;
        }
        if (player.x > map.getWidth() * SCALED_TILE_SIZE - SCALED_TILE_SIZE) {
            player.x = map.getWidth() * SCALED_TILE_SIZE - SCALED_TILE_SIZE;
        }
        if (player.y > map.getHeight() * SCALED_TILE_SIZE - SCALED_TILE_SIZE) {
            player.y = map.getHeight() * SCALED_TILE_SIZE - SCALED_TILE_SIZE;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (mainWorldPortal.overlaps(player) && player.getFacing() == DIRECTION.WEST) {
                GameScreen.setWorld(new MainWorld(player,game,gameScreen));
                player.setX(28);
                player.setY(10.5f);
            }
        }
    }
}
