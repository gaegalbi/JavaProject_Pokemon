package com.pokemon.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.pokemon.game.Pokemon;
import com.pokemon.model.*;
import com.pokemon.screen.GameScreen;
import com.pokemon.transition.FadeInTransition;
import com.pokemon.transition.FadeOutTransition;
import com.pokemon.ui.rank.window;
import com.pokemon.util.Action;
import com.pokemon.util.ObjectGenerator;

import java.util.ArrayList;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;
import static com.pokemon.screen.GameScreen.getAssetManager;
import static com.pokemon.screen.GameScreen.getTweenManager;

public class MainWorld implements World {
    private final TileMap map = new TileMap(29, 22);
    private Player player;
    private Pokemon game;
    private ArrayList<WorldObject> collisionObjects;
    private GameScreen gameScreen;
    private Portal homePortal, minePortal, forestPortal, rankBoard, beachPortal;

    public MainWorld(Player player, Pokemon game, GameScreen gameScreen) {
        this.player = player;
        this.game = game;
        this.gameScreen = gameScreen;

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.tiles[x][y] = new Tile(x, y);
            }
        }
        collisionObjects = ObjectGenerator.generateCollisionObject("MainWorldCollision");
        renderList.clear();
        renderList.add(player);
        renderList.addAll(ObjectGenerator.generateObject("MainWorld"));

        rankBoard = new Portal(6, 12, 1, 1);
        homePortal = new Portal(13, 6, 1, 1);
        minePortal = new Portal(11, 19, 1, 1);
        forestPortal = new Portal(28, 9, 1, 4);
        beachPortal = new Portal(0, 10, 1, 3);
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
        return "MainWorld";
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
            if (homePortal.overlaps(player) && player.getFacing() == DIRECTION.NORTH) {
                player.finishMove();
                gameScreen.getTransitionScreen().startTransition(
                        new FadeOutTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new FadeInTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new Action() {
                            @Override
                            public void action() {
                                GameScreen.setWorld(new Home(player, game, gameScreen));
                                player.setX(3.5f);
                                player.setY(0);
                            }
                        }
                );
            }
            if (minePortal.overlaps(player) && player.getFacing() == DIRECTION.NORTH) {
                player.finishMove();
                gameScreen.getTransitionScreen().startTransition(
                        new FadeOutTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new FadeInTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new Action() {
                            @Override
                            public void action() {
                                GameScreen.setWorld(new Mine(player, game, gameScreen));
                                player.setX(8);
                                player.setY(2);
                            }
                        });
            }

            if (forestPortal.overlaps(player) && player.getFacing() == DIRECTION.EAST) {
                player.finishMove();
                gameScreen.getTransitionScreen().startTransition(
                        new FadeOutTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new FadeInTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new Action() {
                            @Override
                            public void action() {
                                GameScreen.setWorld(new Forest(player, game, gameScreen));
                                player.setX(0);
                                player.setY(15.5f);
                            }
                        });

            }
            if (beachPortal.overlaps(player) && player.getFacing() == DIRECTION.WEST) {
                player.finishMove();
                gameScreen.getTransitionScreen().startTransition(
                        new FadeOutTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new FadeInTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new Action() {
                            @Override
                            public void action() {
                                GameScreen.setWorld(new Beach(player, game, gameScreen));
                                player.setX(26);
                                player.setY(10.5f);
                            }
                        });
            }
            if (rankBoard.overlaps(player) && player.getFacing() == DIRECTION.NORTH) {
                player.finishMove();
                if (gameScreen.getUiStack().isEmpty()) {
                    gameScreen.pushUi(new window(gameScreen, game, player));
                } else {
                    gameScreen.popUi();
                }
            }
        }
    }
}
