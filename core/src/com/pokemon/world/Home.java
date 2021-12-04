package com.pokemon.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.pokemon.game.Pokemon;
import com.pokemon.model.*;
import com.pokemon.screen.GameScreen;
import com.pokemon.transition.FadeInTransition;
import com.pokemon.transition.FadeOutTransition;
import com.pokemon.util.Action;
import com.pokemon.util.ObjectGenerator;

import java.util.ArrayList;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;
import static com.pokemon.screen.GameScreen.getAssetManager;
import static com.pokemon.screen.GameScreen.getTweenManager;

public class Home implements World {
    private final TileMap map = new TileMap(10, 8);
    private GameScreen gameScreen;
    private Player player;
    private Pokemon game;
    private ArrayList<WorldObject> collisionObjects;
    private Portal mainWorldPortal;

    public Home(Player player, Pokemon game, GameScreen gameScreen) {
        this.player = player;
        this.game = game;
        this.gameScreen = gameScreen;

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.tiles[x][y] = new Tile(x, y);
            }
        }

        collisionObjects = ObjectGenerator.generateCollisionObject("HomeCollision");
        renderList.clear();
        renderList.add(player);
        renderList.addAll(ObjectGenerator.generateObject("Home"));

        mainWorldPortal = new Portal(3, 0, 2, 1);
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
        return "Home";
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
                player.finishMove();
                gameScreen.getTransitionScreen().startTransition(
                        new FadeOutTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new FadeInTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new Action() {
                            @Override
                            public void action() {
                                GameScreen.setWorld(new MainWorld(player, game, gameScreen));
                                player.setX(13);
                                player.setY(6);
                            }
                        }
                );
            }
        }
    }
}
