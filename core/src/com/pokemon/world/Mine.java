package com.pokemon.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.model.Player;
import com.pokemon.model.Tile;
import com.pokemon.model.TileMap;
import com.pokemon.screen.GameScreen;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;

public class Mine implements World {
    private final TileMap map = new TileMap(10, 10);
    private Player player;

    public Mine(Player player) {
        this.player = player;
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.tiles[x][y] = new Tile(x, y, "stone");
            }
        }
    }

    @Override
    public TileMap getMap() {
        return map;
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
        if (player.x > 256 && player.y < 32) {
            player.setX(0);
            player.setY(0);
            GameScreen.setWorld(new MainWorld(player));
        }
    }
}
