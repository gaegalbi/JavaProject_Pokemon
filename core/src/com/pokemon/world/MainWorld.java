package com.pokemon.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.game.Pokemon;
import com.pokemon.game.Settings;
import com.pokemon.model.*;
import com.pokemon.screen.GameScreen;
import com.pokemon.screen.TransitionScreen;
import com.pokemon.screen.WorldObjectYComparator;
import com.pokemon.transition.FadeInTransition;
import com.pokemon.transition.FadeOutTransition;
import com.pokemon.ui.AbstractUi;
import com.pokemon.ui.rank.window;
import com.pokemon.util.Action;
import com.pokemon.util.ObjectGenerator;

import java.util.ArrayList;
import java.util.Collections;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;
import static com.pokemon.screen.GameScreen.getAssetManager;
import static com.pokemon.screen.GameScreen.getTweenManager;

public class MainWorld implements World {
    private final TileMap map = new TileMap(29, 22);
    private Player player;
    private Pokemon game;
    private ArrayList<WorldObject> collisionObjects;
    private TransitionScreen transitionScreen;
    private GameScreen gameScreen;
    private Portal homePortal,MinePortal,FieldPortal;

    int check = 1;

    public MainWorld(Player player, Pokemon game, GameScreen gameScreen) {
        this.player = player;
        this.game = game;
        this.gameScreen = gameScreen;

        this.transitionScreen = new TransitionScreen(game);
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                map.tiles[x][y] = new Tile(x, y);
            }
        }
        collisionObjects = ObjectGenerator.generateObject("MainWorldCollision");
        renderList.clear();
        renderList.add(player);
        renderList.addAll(ObjectGenerator.generateObject("MainWorld"));

        homePortal = new Portal(13, 6, 1, 1);
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
                GameScreen.setWorld(new Home(player,game,gameScreen));
                player.setX(3.5f);
                player.setY(0);
            }
        }

        if (((int)(player.x/32) == 5 || (int)(player.x/32) == 6) && (int)(player.y/32) == 12){
            if (check == 1)
                gameScreen.pushUi(new window(gameScreen, game,player));
            check++;
        }
        else{
            if (check != 1){
                AbstractUi popped = gameScreen.popUi();
                popped.dispose();
            }
            check = 1;
        }
    }
}
