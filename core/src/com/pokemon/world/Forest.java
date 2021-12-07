package com.pokemon.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.pokemon.game.Pokemon;
import com.pokemon.game.Sound;
import com.pokemon.model.*;
import com.pokemon.screen.BattleScreen;
import com.pokemon.screen.GameScreen;
import com.pokemon.transition.BattleBlinkTransition;
import com.pokemon.transition.BattleTransition;
import com.pokemon.transition.FadeInTransition;
import com.pokemon.transition.FadeOutTransition;
import com.pokemon.util.Action;
import com.pokemon.util.ObjectGenerator;

import java.util.ArrayList;

import static com.pokemon.game.Settings.SCALED_TILE_SIZE;
import static com.pokemon.screen.GameScreen.getAssetManager;
import static com.pokemon.screen.GameScreen.getTweenManager;

public class Forest implements World {
    private final TileMap map = new TileMap(24, 20);
    private GameScreen gameScreen;
    private Player player;
    private Pokemon game;
    private ArrayList<WorldObject> collisionObjects;
    private Portal mainWorldPortal;
    private ArrayList<BattleArea> battleAreas;

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

        mainWorldPortal = new Portal(0, 13, 1, 4);

        battleAreas = new ArrayList<>();
        battleAreas.add(new BattleArea(11,11,9,7));
        battleAreas.add(new BattleArea(4,2,16,5));
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
    public ArrayList<RenderHelper> getObjects() {
        return renderList;
    }

    @Override
    public String getBackground() {
        return "Forest";
    }

    @Override
    public void update(float delta) {
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
        // 전투 테스트 용
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            player.finishMove();
            gameScreen.getGameMusic().stop();
            game.getBattleMusic().play();
            gameScreen.getTransitionScreen().startTransition(
                    new BattleBlinkTransition(4f, 4 , Color.GRAY, gameScreen.getTransitionShader(), getTweenManager(), getAssetManager()),
                    new BattleTransition(1F,  10, true, gameScreen.getTransitionShader(), getTweenManager(), getAssetManager()),
                    new Action() {
                        @Override
                        public void action() {
                            System.out.println("배틀시작");
                            game.setScreen(new BattleScreen(game,player,gameScreen));
                        }
                    }
            );
        }
        for (BattleArea battleArea : battleAreas) {
            if (player.overlaps(battleArea)) {
                if (battleArea.battleStarter(delta, player.x, player.y)) {
                    player.finishMove();
                    gameScreen.getGameMusic().stop();
                    game.getBattleMusic().play();
                    gameScreen.getTransitionScreen().startTransition(
                            new BattleBlinkTransition(4f, 4 , Color.GRAY, gameScreen.getTransitionShader(), getTweenManager(), getAssetManager()),
                            new BattleTransition(1F,  10, true, gameScreen.getTransitionShader(), getTweenManager(), getAssetManager()),
                            new Action() {
                                @Override
                                public void action() {
                                    System.out.println("배틀시작");
                                    game.setScreen(new BattleScreen(game,player,gameScreen));
                                }
                            }
                    );
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (mainWorldPortal.overlaps(player) && player.getFacing() == DIRECTION.WEST) {
                player.finishMove();
                gameScreen.getTransitionScreen().startTransition(
                        new FadeOutTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new FadeInTransition(0.8f, Color.BLACK, getTweenManager(), getAssetManager()),
                        new Action() {
                            @Override
                            public void action() {
                                GameScreen.setWorld(new MainWorld(player, game, gameScreen));
                                player.setX(28);
                                player.setY(10.5f);
                            }
                        }
                );
            }
        }
    }
}
