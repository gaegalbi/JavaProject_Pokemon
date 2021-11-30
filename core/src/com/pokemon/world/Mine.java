package com.pokemon.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.pokemon.game.Pokemon;
import com.pokemon.model.*;
import com.pokemon.screen.GameScreen;
import com.pokemon.screen.TransitionScreen;
import com.pokemon.util.ObjectGenerator;

import java.util.ArrayList;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;

public class Mine implements World {
    private final TileMap map = new TileMap(17, 14);
    private Player player;
    private Pokemon game;
    private GameScreen gameScreen;
    private TransitionScreen transitionScreen;
    private ArrayList<WorldObject> collisionObjects;
    private Portal mainWorldPortal;

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
        collisionObjects = ObjectGenerator.generateCollisionObject("MineCollision");
        renderList.clear();
        renderList.add(player);
        renderList.addAll(ObjectGenerator.generateObject("Mine"));
        mainWorldPortal = new Portal(8, 2, 1, 1);
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (mainWorldPortal.overlaps(player) && player.getFacing() == DIRECTION.SOUTH) {
                GameScreen.setWorld(new MainWorld(player,game,gameScreen));
                player.setX(11);
                player.setY(19);
            }
        }

    }
}
