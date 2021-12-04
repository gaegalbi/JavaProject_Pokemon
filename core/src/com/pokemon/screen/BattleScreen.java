package com.pokemon.screen;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pokemon.battle.BATTLE_PARTY;
import com.pokemon.battle.Battle;
import com.pokemon.battle.event.BattleEvent;
import com.pokemon.battle.event.BattleEventPlayer;
import com.pokemon.controller.GameController;
import com.pokemon.controller.PlayerController;
import com.pokemon.controller.BattleScreenController;
import com.pokemon.game.Pokemon;
import com.pokemon.game.Settings;
import com.pokemon.model.Player;
import com.pokemon.ui.*;
import com.pokemon.util.GifDecoder;
import com.pokemon.util.SkinGenerator;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class BattleScreen implements Screen, BattleEventPlayer {
    final Pokemon game;
    private AssetManager assetManager;
    private OrthographicCamera camera;

    private PlayerController playerController;
    private BattleRenderer battleRenderer;
    private GameController gameController;
    public static int playerNum=2; //전투 순서

    /* Controller */
    private BattleScreenController controller;

    /* View */
    private Viewport gameViewport;

    /* Model */
    private Battle battle;

    /* UI */
    float elapsed;
    private Skin skin;
    private Stage uiStage;
    private Table dialogueRoot;
    private DialogueBox dialogueBox;
    private OptionBox optionBox;
    private Player player;

    private Table moveSelectRoot;
    private MoveSelectBox moveSelectBox;

    private Table statusBoxRoot;
    //private DetailedStatusBox playerStatus;
    private StatusBox playerStatus;
    private StatusBox opponentStatus;

    /* 이벤트 시스템 */
    private BattleEvent currentEvent;
    private EventQueueRenderer eventRenderer;
    private Queue<BattleEvent> queue = new ArrayDeque<>();

    private Stack<AbstractUi> uiStack;
    public static boolean useCheck= true;

    private int playercount=0;
    private int enemycount=0;

    public BattleScreen(Pokemon game, Player player) {
        this.game = game;
        this.uiStack = new Stack<>();

        gameViewport = new ScreenViewport();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);

        //폰트 및 UI 불러오기
        assetManager= new AssetManager();
        assetManager.load("ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("font/han/gul.fnt", BitmapFont.class);
        assetManager.finishLoading();

        //배틀 생성 및 이벤트 할당
        this.battle = new Battle(this.game,this,player);
        battle.setEventPlayer(this);

        skin = SkinGenerator.generateSkin(assetManager);



        eventRenderer = new EventQueueRenderer(skin, queue);
        initUI();

        controller = new BattleScreenController(this.game,this,battle, queue, dialogueBox, moveSelectBox, optionBox,uiStack,player);
        battleRenderer = new BattleRenderer(this,controller,this.game,battle,camera);


        battle.beginBattle();
    }

    private void initUI() {
        /* ROOT UI STAGE */
        uiStage = new Stage(new ScreenViewport());
        uiStage.getViewport().update(
                Gdx.graphics.getWidth()/Settings.SCALE,
                Gdx.graphics.getHeight()/Settings.SCALE,
                true);
        uiStage.setDebugAll(false);

        /* STATUS BOXES */
        statusBoxRoot = new Table();
        statusBoxRoot.setFillParent(true);
        uiStage.addActor(statusBoxRoot);

        //playerStatus = new DetailedStatusBox(skin);
        playerStatus = new StatusBox(skin);
        playerStatus.setText(battle.getP_P().getName());
        playerStatus.setLV("LV"+battle.getP_P().getLV());
        playerStatus.setHPText(battle.getP_P().getCurrentChHP() + "/" + battle.getP_P().getChStat()[2]);

        opponentStatus = new StatusBox(skin);
        opponentStatus.setText(battle.getO_P().getName());
        opponentStatus.setLV("LV"+battle.getO_P().getLV());
        opponentStatus.setHPText(battle.getO_P().getCurrentChHP() + "/" + battle.getO_P().getChStat()[2]);


        statusBoxRoot.add(playerStatus).expand().align(Align.left);
        statusBoxRoot.add(opponentStatus).expand().align(Align.right);

        /* MOVE SELECTION BOX */
        moveSelectRoot = new Table();
        moveSelectRoot.setFillParent(true);
        uiStage.addActor(moveSelectRoot);

        moveSelectBox = new MoveSelectBox(skin);
        moveSelectBox.setVisible(false);

        moveSelectRoot.add(moveSelectBox).expand().align(Align.bottom);

        /* OPTION BOX */
        dialogueRoot = new Table();
        dialogueRoot.setFillParent(true);
        uiStage.addActor(dialogueRoot);

        optionBox = new OptionBox(skin);
        optionBox.setVisible(false);

        /* DIALOGUE BOX */
        dialogueBox = new DialogueBox(skin);
        dialogueBox.setVisible(false);

        Table dialogTable = new Table();
        dialogTable.add(optionBox).expand().align(Align.right).space(8f).row();
        dialogTable.add(dialogueBox).expand().align(Align.bottom).space(8f);

        dialogueRoot.add(dialogTable).expand().align(Align.bottom);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    public void update(float delta) {

        while (currentEvent == null || currentEvent.finished()) { // no active event
            if (queue.peek() == null) { // no event queued up
                currentEvent = null;
                if (battle.getState() == Battle.STATE.SELECT_NEW_POKEMON) {
                    if (controller.getState() != BattleScreenController.STATE.USE_NEXT_POKEMON) {
                        controller.displayNextDialogue();
                    }
                } else if (battle.getState() == Battle.STATE.READY_TO_PROGRESS) {
                    controller.restartTurn();
                } else if (battle.getState() == Battle.STATE.WIN) {
                    game.setScreen(new GameScreen(game));
                } else if (battle.getState() == Battle.STATE.LOSE) {
                    game.setScreen(new GameScreen(game));
                } else if (battle.getState() == Battle.STATE.RAN) {
                    game.setScreen(new GameScreen(game));
                }
                break;
            } else {					// event queued up
                currentEvent = queue.poll();
                currentEvent.begin(this);
            }
        }

        if (currentEvent != null) {
            currentEvent.update(delta);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F9)){
           useCheck = (!useCheck);
            if(uiStack!=null&&useCheck) {
                AbstractUi popped = uiStack.pop();
                popped.dispose();
                Gdx.input.setInputProcessor(controller);
                controller.restartTurn();
            }
        }
        controller.update(delta);
        uiStage.act(); // update ui
    }

    @Override
    public void render(float delta) {

        gameViewport.apply();
        camera.position.x =  Gdx.graphics.getWidth()/2f;
        camera.position.y = Gdx.graphics.getHeight()/2f;
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        elapsed += Gdx.graphics.getDeltaTime();

        game.batch.begin();

        this.update(delta);
        battleRenderer.render(game.batch,elapsed);
        if (currentEvent != null) {
            eventRenderer.render(game.batch, currentEvent);
        }

        game.batch.end();

        uiStage.draw();
        if(uiStack!=null) {
            for (AbstractUi abstractUi : uiStack) {
                abstractUi.update();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        game.batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        uiStage.getViewport().update(
                Gdx.graphics.getWidth()/Settings.SCALE,
                Gdx.graphics.getHeight()/Settings.SCALE,
                true);
        gameViewport.update(width, height);
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

    }

    @Override
    public void setPokemonSprite(Texture region, BATTLE_PARTY party) {

    }

    @Override
    public DialogueBox getDialogueBox() {
        return dialogueBox;
    }

    @Override
    public StatusBox getStatusBox(BATTLE_PARTY party) {
        if (party == BATTLE_PARTY.PLAYER) {
            return playerStatus;
        } else if (party == BATTLE_PARTY.OPPONENT) {
            return opponentStatus;
        } else {
            return null;
        }
    }

    @Override
    public TweenManager getTweenManager() {
        return null;
    }

    @Override
    public void queueEvent(BattleEvent event) {
        queue.add(event);
    }

}
