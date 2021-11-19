package com.pokemon.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.game.Settings;
import com.pokemon.model.Player;
import com.pokemon.model.Tile;
import com.pokemon.model.TileMap;
import com.pokemon.model.WorldObject;
import com.pokemon.screen.GameScreen;

import java.util.ArrayList;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;

public class MainWorld implements World {

    private final TileMap map = new TileMap(20, 20);
    private Player player;
    private ArrayList<WorldObject> objects;

    public MainWorld(Player player) {
        this.player = player;
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.tiles[x][y] = new Tile(x, y, "grass");
            }
        }
        objects = new ArrayList<>();
        objects.add(new WorldObject(5 * Settings.TILE_SIZE, 5 * Settings.TILE_SIZE, 100, 100, "badlogic.jpg"));
    }

    @Override
    public TileMap getMap() {
        return map;
    }

    @Override
    public ArrayList<WorldObject> getObjects() {
        return objects;
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
        if (player.x > 576 && player.y < 32) {
            player.setX(0);
            player.setY(0);
            GameScreen.setWorld(new Mine(player));
        }
    }
}
