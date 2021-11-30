package com.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.pokemon.controller.GameController;
import com.pokemon.controller.PlayerController;
import com.pokemon.db.db;
import com.pokemon.game.Pokemon;
import com.pokemon.model.Player;
import com.pokemon.ui.AbstractUi;
//import com.pokemon.ui.inventory.InventoryRenderer;
//import com.pokemon.ui.inventory.InventoryUI;
import com.pokemon.ui.SkillListUi;
import com.pokemon.ui.inventory.InventoryUI;
import com.pokemon.util.AnimationSet;
import com.pokemon.util.SkinGenerator;
import com.pokemon.world.World;
import com.pokemon.world.MainWorld;

import java.util.Stack;

public class GameScreen implements Screen {
    final Pokemon game;
    private static World world;
    private Skin skin;

    private AssetManager assetManager;
    private OrthographicCamera camera;
    private Player player;
    private PlayerController playerController;
    private WorldRenderer worldRenderer;
    private GameController gameController;
   // private InventoryRenderer inventoryRenderer;
    private Stack<AbstractUi> uiStack;
    Stage stage;
    private boolean invenCheck=false;
    private boolean skillCheck=false;

    public GameScreen(Pokemon game) {
        this.game = game;
        assetManager = new AssetManager();
        assetManager.load("players/players.atlas", TextureAtlas.class);
        assetManager.load("texture/texture.atlas", TextureAtlas.class);
        assetManager.finishLoading();


        TextureAtlas playerTexture = assetManager.get("players/players.atlas", TextureAtlas.class);
        //TextureAtlas Texture = assetManager.get("texture/texture.atlas", TextureAtlas.class);

        skin = SkinGenerator.generateSkin(assetManager);

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
        worldRenderer = new WorldRenderer(player);
        playerController = new PlayerController(player);
        gameController = new GameController(game);
        uiStack = new Stack<>();
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
        game.batch.end();
        playerController.update();
        player.update(delta);
        world.update();
        gameController.update();
        this.update(delta);


        for (AbstractUi abstractUi : uiStack) {
            abstractUi.update();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    public void update(float delta){

        if (Gdx.input.isKeyJustPressed(Input.Keys.F9)){
            invenCheck = (!invenCheck);
            if(invenCheck) {
                //아이템 추가
                db.UPDATE("ITEM_01",1);
                player.inventory.addItem("ITEM_01",1);
                this.pushScreen(new InventoryUI(this, game,player));

            }else {
                AbstractUi popped = uiStack.pop();
                popped.dispose();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)){
            skillCheck = (! skillCheck);
            if( skillCheck) {
                this.pushScreen(new SkillListUi(this, game,player));
            }else {
                AbstractUi popped = uiStack.pop();
                popped.dispose();
            }
        }
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

    public void pushScreen(AbstractUi ui) {
        uiStack.add(ui);
    }

    public void popScreen() {
        AbstractUi popped = uiStack.pop();
        popped.dispose();
        Gdx.input.setInputProcessor(uiStack.peek().getStage());
    }
}
