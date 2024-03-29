package com.pokemon.multibattle;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pokemon.battle.BATTLE_PARTY;
import com.pokemon.battle.Battle;
import com.pokemon.battle.SingleBattle;
import com.pokemon.battle.event.*;
import com.pokemon.controller.BattleScreenController;
import com.pokemon.db.db;
import com.pokemon.game.Sound;
import com.pokemon.model.Player;
import com.pokemon.screen.EventQueueRenderer;
import com.pokemon.ui.*;
import com.pokemon.util.GifDecoder;
import com.pokemon.game.Pokemon;
import com.pokemon.screen.GameScreen;
import com.pokemon.util.MultiSkinGenerator;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

import static com.pokemon.ui.LoginUi.playerID;
import static java.lang.Integer.parseInt;

public class MultiBattleScreen implements Screen, BattleEventPlayer {
    int i=0;
    int skillcount=0;
    final Pokemon game;
    private GameScreen gameScreen;
    SpriteBatch batch;
    Animation<TextureRegion> animation;
    Animation<TextureRegion> animation2;
    Animation<TextureRegion> enemyanimation;
    Animation<TextureRegion> enemyanimation2;
    Animation<TextureRegion> ballRight;
    Animation<TextureRegion> ballLeft;
    Timer timer = new Timer();
    private Texture fieldimage1;
    private Texture fieldimage2;
    private Texture background;
    float elapsed;
    float elapsed2;
    private Texture buttonTexture;
    private TextureRegion buttonTextureRegion;
    private TextureRegionDrawable buttonTextureRegionDrawable;
    private ImageButton button;
    private SelectButton selectButton;
    private OrthographicCamera camera;
    private int playercount=0;
    private int enemycount=0;
    private int ballTime1=0;
    private int ballTime2=0;
    private boolean battleFinish;
    /*private String pokemonSelectedList = selectButton.pokemonList.list;*/
    private String[] selectedPokemon = new String[4];
    private String[] selectedPokemon2 = new String[4];
    private String[] selected = new String[4];
    private PokemonSelectScreen pokemonSelectScreen;

    /* Controller */
    private BattleScreenController controller;

    /* View */
    private Viewport gameViewport;

    /* Model */
    private Battle battle;

    /* UI */
    private Skin skin;
    private Stage uiStage;
    private Table dialogueRoot;
    private DialogueBox dialogueBox;
    private OptionBox optionBox;

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
    private AssetManager assetManager;
    private BattleClient bc;
    private MoveEvent moveEvent;
    private SkillEvent skillEvent;
    private Player player;
    public MultiBattleScreen(final Pokemon game,final BattleClient bc, GameScreen gameScreen, Player player) {
        this.player = player;
        this.bc = bc;
        this.game = game;
        this.gameScreen = gameScreen;
        game.setI(0);
        game.setJ(0);
        batch = new SpriteBatch();
        /*selectedPokemon =  pokemonSelectedList.split(" ");*/
        selectedPokemon =  game.getStr().split(" ");
        game.setSelectedPokemon(selectedPokemon);
        selectedPokemon2 =  game.getrecieveMessage().split(" ");
        System.out.println(game.getrecieveMessage());
        selected[0] = db.sP(selectedPokemon[0],parseInt(selectedPokemon[1]));
        selected[1] = db.sP(selectedPokemon[2],parseInt(selectedPokemon[3]));
        selected[2] = db.sP(selectedPokemon2[0],parseInt(selectedPokemon2[1]));
        selected[3] = db.sP(selectedPokemon2[2],parseInt(selectedPokemon2[3]));
        background = new Texture(Gdx.files.internal("multibattle/battlebgField.png"));
        fieldimage1 = new Texture(Gdx.files.internal("multibattle/enemybaseFieldGrass.png"));
        fieldimage2 = new Texture(Gdx.files.internal("multibattle/enemybaseFieldGrass.png"));
        animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/"+selected[1]+".gif").read());
        animation2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/"+selected[0]+".gif").read());
        enemyanimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/"+selected[3]+".gif").read());
        enemyanimation2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/"+selected[2]+".gif").read());
        ballRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("multibattle/ball.gif").read());
        ballLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("multibattle/ball2.gif").read());
        buttonTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
        buttonTextureRegion = new TextureRegion(buttonTexture);
        buttonTextureRegionDrawable = new TextureRegionDrawable(buttonTextureRegion);
        button = new ImageButton(buttonTextureRegionDrawable); //Set the button up

        //폰트 및 UI 불러오기
        assetManager= new AssetManager();
        assetManager.load("ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("font/han/gul.fnt", BitmapFont.class);
        assetManager.finishLoading();

        this.battle = new Battle(game,true,player);
        battle.setEventPlayer(this);
        skillEvent = new SkillEvent(this,batch,game,battle.getPTrainer().getPokemon(battle.getPoket1()).getType(),battle.getOTrainer().getPokemon(battle.getPoket2()).getType(),battle.isSkill());
        skin = MultiSkinGenerator.generateSkin(assetManager);

        eventRenderer = new EventQueueRenderer(skin, queue);

        initUI();
        controller = new BattleScreenController(game, battle,true, queue, dialogueBox, moveSelectBox, optionBox,new Stack<AbstractUi>(), gameScreen.player);


        battle.beginBattle();

        moveEvent = new MoveEvent(animation,1,true,150,200);

    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        this.update(delta);
        batch.draw(background,0,0,800,600);
        batch.draw(fieldimage1, 80, 140);
        batch.draw(fieldimage2, 450, 140);
        if(playercount==0) {
            batch.draw(animation.getKeyFrame(elapsed), 150.0f, 200.0f);
        }else{
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    ballTime1 = 1;
                }
            }, 1);

            System.out.println(ballTime1);
            if(ballTime1 ==1){
                batch.draw(animation2.getKeyFrame(elapsed), 150.0f, 200.0f);
            }else{
                elapsed2 += Gdx.graphics.getDeltaTime();
                batch.draw(ballRight.getKeyFrame(elapsed2),-60.0f,120.0f);
            }
        }

        if(enemycount==0) {
            batch.draw(enemyanimation.getKeyFrame(elapsed), 550.0f, 200.0f);
        }else{
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    ballTime2 = 1;
                }
            }, 1);
            System.out.println(ballTime2);
            if(ballTime2 == 1){
                batch.draw(enemyanimation2.getKeyFrame(elapsed), 550.0f, 200.0f);
            }else{
                elapsed2 += Gdx.graphics.getDeltaTime();
                batch.draw(ballLeft.getKeyFrame(elapsed2),260.0f,120.0f);
            }
        }

        if(controller.getCount()==1){
            String s = String.valueOf(controller.X());
            if (battle.getP_P().getSkill()[controller.X()] == null || battle.getP_P().getCurrent_SK_CNT()[controller.X()] <= 0)  {
                controller.endTurn();
                queue.add(new TextEvent("사용할수 없습니다...", 0.5f));

            } else {
                bc.setSendMessage(s);
                controller.endTurn();
                queue.add(new TextEvent("상대가 선택 중 입니다.", true));
            }
            controller.setCount(0);

        }
        if(game.getI()==1 && game.getJ()==1){
            currentEvent = null;
            controller.setX(controller.X());
            game.setI(0);
            game.setJ(0);
        }
        if (currentEvent != null) {
            eventRenderer.render(batch, currentEvent);
        }
        if(game.isOnoff()){
            skillEvent.effectSkill();
        }
        if(skillcount == 1){
            skillEvent = new SkillEvent(this,batch,game,battle.getPTrainer().getPokemon(battle.getPoket1()).getType(),battle.getOTrainer().getPokemon(battle.getPoket2()).getType(),battle.isSkill());
            skillcount = 0;
        }
        batch.end();
        uiStage.draw();
        Timer.schedule(new Timer.Task(){
            @Override
            public void run(){

            }
        },3);
        if(i==1){
            gameScreenStart();
        }
    }
    public void gameScreenStart(){
        game.setScreen(gameScreen);
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
        batch.dispose();
        assetManager.dispose();
        skin.dispose();
        uiStage.dispose();
    }
    private void initUI() {
        /* ROOT UI STAGE */
        uiStage = new Stage(new ScreenViewport());
        uiStage.getViewport().update(
                (int)(Gdx.graphics.getWidth()),
                (int)(Gdx.graphics.getHeight()),
                true);
        uiStage.setDebugAll(false);

        /* STATUS BOXES */
        statusBoxRoot = new Table();
        statusBoxRoot.setFillParent(true);
        statusBoxRoot.setPosition(5,90);
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
    public SpriteBatch getBatch(){return batch;}
    @Override
    public TweenManager getTweenManager() {
        return null;
    }

    @Override
    public void queueEvent(BattleEvent event) {
        queue.add(event);
    }
    public void update(float delta) {
        if (queue.isEmpty()) {
            if (battle.getState() == Battle.STATE.WIN) {
                if (!battleFinish) {
                    game.getBattleMusic().stop();
                    Sound.win.play(0.05f);
                }
                battleFinish = true;
            } else if (battle.getState() == Battle.STATE.LOSE){
                if (!battleFinish) {
                    game.getBattleMusic().stop();
                    Sound.lose.play(0.05f);
                }
                battleFinish = true;
            } else if (battle.getState() == Battle.STATE.RAN) {
                if (!battleFinish) {
                    game.getBattleMusic().stop();
                    Sound.lose.play(0.05f);
                }
                battleFinish = true;
            }
        }
        while (currentEvent == null || currentEvent.finished()) { // no active event
            if (queue.peek() == null) { // no event queued up
                currentEvent = null;

                if (battle.getState() == Battle.STATE.SELECT_NEW_POKEMON) {
                    /*if (controller.getState() != BattleScreenController.STATE.USE_NEXT_POKEMON) {
                        controller.displayNextDialogue();
                    }*/
                    if(battle.getChangecharacter()){
                        if (!battle.getPTrainer().getPokemon(1).isFainted()) {
                            battle.chooseNewPokemon(battle.getPTrainer().getPokemon(1));
                            battle.getPTrainer().getPokemon(1).applyDamage(0);
                            System.out.println(battle.getPTrainer().getPokemon(1).getCurrentHP());
                            playercount++;
                            optionBox.setVisible(false);
                            controller.state = BattleScreenController.STATE.USE_NEXT_POKEMON;
                            break;
                        }
                    }else{
                        if (!battle.getOTrainer().getPokemon(1).isFainted()) {
                            battle.chooseNewPokemon2(battle.getOTrainer().getPokemon(1));
                            enemycount++;
                            optionBox.setVisible(false);
                            controller.state = BattleScreenController.STATE.USE_NEXT_POKEMON;
                            break;
                        }
                    }
                } else if (battle.getState() == Battle.STATE.READY_TO_PROGRESS) {
                    controller.restartTurn();
                } else if (battle.getState() == Battle.STATE.WIN) {
                    db.RANK_SET_RANK(playerID,10);
                    player.setRANK(10);
                    game.setScreen(gameScreen);
                } else if (battle.getState() == Battle.STATE.LOSE) {
                    game.setScreen(gameScreen);
                } else if (battle.getState() == Battle.STATE.RAN) {
                    game.setScreen(gameScreen);
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

        //controller.update(delta);
        uiStage.act(); // update ui
    }
    public void setSkillcount(int skillcount) {
        this.skillcount = skillcount;
    }

}