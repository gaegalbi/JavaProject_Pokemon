package com.pokemon.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.game.Pokemon;
import com.pokemon.game.Settings;
import com.pokemon.model.Player;
import com.pokemon.model.Tile;
import com.pokemon.model.TileMap;
import com.pokemon.model.WorldObject;
import com.pokemon.screen.GameScreen;
import com.pokemon.screen.TransitionScreen;
import com.pokemon.transition.FadeInTransition;
import com.pokemon.transition.FadeOutTransition;
import com.pokemon.util.Action;
import com.pokemon.util.ObjectGenerator;

import java.util.ArrayList;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;
import static com.pokemon.screen.GameScreen.getAssetManager;
import static com.pokemon.screen.GameScreen.getTweenManager;

public class Mine implements World {
    private final TileMap map = new TileMap(10, 10);
    private Player player;
    private Pokemon game;
    private GameScreen gameScreen;
    private TransitionScreen transitionScreen;
    private ArrayList<WorldObject> collisionObjects;

    public Mine(Player player, Pokemon game, GameScreen gameScreen) {
        this.player = player;
        this.game = game;
        this.gameScreen = gameScreen;

        this.transitionScreen = new TransitionScreen(game);
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.tiles[x][y] = new Tile(x, y);
            }
        }
        collisionObjects = ObjectGenerator.generateObject("MineCollision");
        renderList.clear();
        renderList.add(player);
        renderList.addAll(ObjectGenerator.generateObject("Mine"));
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
        return "Mine";
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

    }
}
