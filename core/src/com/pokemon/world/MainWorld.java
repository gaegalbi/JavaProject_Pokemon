package com.pokemon.world;

import com.badlogic.gdx.graphics.Color;
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

import java.util.ArrayList;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;
import static com.pokemon.screen.GameScreen.getAssetManager;
import static com.pokemon.screen.GameScreen.getTweenManager;

public class MainWorld implements World {

    private final TileMap map = new TileMap(20, 20);
    private Player player;
    private Pokemon game;
    private ArrayList<WorldObject> fakeObjects;
    private ArrayList<WorldObject> objects;
    private TransitionScreen transitionScreen;
    private GameScreen gameScreen;

    public MainWorld(Player player, Pokemon game, GameScreen gameScreen) {
        this.player = player;
        this.game = game;
        this.gameScreen = gameScreen;
        this.transitionScreen = new TransitionScreen(game);
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.tiles[x][y] = new Tile(x, y, "grass");
            }
        }
        fakeObjects = new ArrayList<>();
        objects = new ArrayList<>();

        fakeObjects.add(new WorldObject(4 * Settings.SCALED_TILE_SIZE, 7 * Settings.SCALED_TILE_SIZE, 2*SCALED_TILE_SIZE, SCALED_TILE_SIZE,atlas.findRegion("null")));
        objects.add(new WorldObject(4 * Settings.SCALED_TILE_SIZE, 7 * Settings.SCALED_TILE_SIZE, 2*SCALED_TILE_SIZE, 3*SCALED_TILE_SIZE,atlas.findRegion("tree")));

        fakeObjects.add(new WorldObject(11 * Settings.SCALED_TILE_SIZE, 7 * Settings.SCALED_TILE_SIZE, 2*SCALED_TILE_SIZE, SCALED_TILE_SIZE,atlas.findRegion("null")));
        objects.add(new WorldObject(11 * Settings.SCALED_TILE_SIZE, 7 * Settings.SCALED_TILE_SIZE, 2*SCALED_TILE_SIZE, 3*SCALED_TILE_SIZE,atlas.findRegion("tree")));

        fakeObjects.add(new WorldObject(5 * Settings.SCALED_TILE_SIZE, 8 * Settings.SCALED_TILE_SIZE, 7*SCALED_TILE_SIZE, 4*SCALED_TILE_SIZE,atlas.findRegion("null")));
        objects.add(new WorldObject(5 * Settings.SCALED_TILE_SIZE, 8 * Settings.SCALED_TILE_SIZE, 7*SCALED_TILE_SIZE, 6*SCALED_TILE_SIZE,atlas.findRegion("house1")));

        renderQueue.clear();

        renderQueue.add(player);
        renderQueue.addAll(objects);
    }

    @Override
    public TileMap getMap() {
        return map;
    }

    @Override
    public ArrayList<WorldObject> getFakeObjects() {
        return fakeObjects;
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
            transitionScreen.startTransition(
                gameScreen,
                gameScreen,
                new FadeOutTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                new FadeInTransition(0.8f,  Color.BLACK, getTweenManager(), getAssetManager()),
                new Action() {
                    @Override
                    public void action() {
                        System.out.println("FadeOut");
                    }
            });
            GameScreen.setWorld(new Mine(player,game,gameScreen));
        }
    }
}
