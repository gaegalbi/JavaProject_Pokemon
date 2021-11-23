package com.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.pokemon.controller.GameController;
import com.pokemon.controller.PlayerController;
import com.pokemon.game.Pokemon;
import com.pokemon.model.Player;
import com.pokemon.model.Portal;
import com.pokemon.multibattle.BattleLoadingScreen;
import com.pokemon.ui.AbstractUi;
import com.pokemon.ui.ChatButton;
import com.pokemon.util.AnimationSet;
import com.pokemon.world.World;
import com.pokemon.world.Mine;
import com.pokemon.world.MainWorld;

import java.util.HashMap;
import java.util.Stack;

public class GameScreen implements Screen {
    final Pokemon game;
    private static World world;

    private AssetManager assetManager;
    private OrthographicCamera camera;
    private Player player;
    private PlayerController playerController;
    private WorldRenderer worldRenderer;
    private GameController gameController;
    private Stack<AbstractUi> uiStack1;
    public GameScreen(Pokemon game) {
        this.game = game;
        uiStack1 = new Stack<>();
        assetManager = new AssetManager();
        assetManager.load("players/players.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        uiStack1.add(new ChatButton(this,game));
        TextureAtlas playerTexture = assetManager.get("players/players.atlas", TextureAtlas.class);

        AnimationSet<TextureRegion> animations = new AnimationSet<>(
                new Animation<TextureRegion>(0.3f / 2f, playerTexture.findRegions("dawn_walk_north"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(0.3f / 2f, playerTexture.findRegions("dawn_walk_south"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(0.3f / 2f, playerTexture.findRegions("dawn_walk_east"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(0.3f / 2f, playerTexture.findRegions("dawn_walk_west"), Animation.PlayMode.LOOP_PINGPONG),
                playerTexture.findRegion("dawn_stand_north"),
                playerTexture.findRegion("dawn_stand_south"),
                playerTexture.findRegion("dawn_stand_east"),
                playerTexture.findRegion("dawn_stand_west")
        );

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        player = new Player(0, 0, animations);
        world = new MainWorld(player);
        worldRenderer = new WorldRenderer();
        playerController = new PlayerController(player);
        gameController = new GameController(game);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.position.x = player.x;
        camera.position.y = player.y;
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        worldRenderer.render(game.batch);
        game.batch.draw(player.getSprites(), player.x, player.y, player.getPlayerSizeX(), player.getPlayerSizeY());
        game.batch.end();
        playerController.update();
        player.update(delta);
        world.update();
        gameController.update();
        for (AbstractUi abstractUi : uiStack1) {
            abstractUi.update();
        }
        if (player.x > 576 && player.y < 32){
            loadingStart();
        }
    }
    public void pushScreen(AbstractUi ui) {
        uiStack1.add(ui);
    }

    public void popScreen() {
        AbstractUi popped = uiStack1.pop();
        popped.dispose();
        Gdx.input.setInputProcessor(uiStack1.peek().getStage());
    }
    public void loadingStart(){
        game.setScreen(new BattleLoadingScreen(game));
        dispose();
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public static World getWorld() {
        return world;
    }

    public static void setWorld(World world) {
        GameScreen.world = world;
    }
}
