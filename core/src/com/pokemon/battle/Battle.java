package com.pokemon.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pokemon.battle.event.*;
import com.pokemon.game.Pokemon;
import com.pokemon.inventory.Item;
import com.pokemon.model.PK;
import com.pokemon.db.db;
import com.pokemon.model.Player;
import com.pokemon.screen.BattleScreen;
import com.pokemon.util.GifDecoder;
import com.pokemon.util.SkinGenerator;

import java.sql.SQLException;
import java.sql.Statement;

import static com.pokemon.controller.BattleScreenController.moveSelect;
import static com.pokemon.db.db.con;
import static com.pokemon.db.db.rs;

import static com.pokemon.screen.BattleScreen.playerNum;
import static com.pokemon.ui.LoginUi.playerID;
import static java.lang.Integer.parseInt;

public class Battle implements BattleEventQueuer {
    public enum STATE {
        READY_TO_PROGRESS,
        SELECT_NEW_POKEMON,
        RAN,
        WIN,
        LOSE,
    }

    private STATE state;
    private PK player;
    private PK opponent;
    private PK player2;
    private PK opponent2;
    private String pName;
    private String oName;
    private Trainer pTrainer;
    private Trainer oTrainer;
    //private Texture P_T;
    //private Texture O_T;
    private Animation<TextureRegion> P_T;
    private Animation<TextureRegion> P_T2;
    private Animation<TextureRegion> O_T;
    private Animation<TextureRegion> O_T2;
    Animation<TextureRegion> ball;

    private AssetManager assetManager;
    private BattleEventPlayer eventPlayer;
    private BattleMechanics mechanics;

    public static String OppoID;
    private String[] oppoKey;
    private String wildKey;
    private Skin skin;
    private Pokemon game;
    private BattleScreen battleScreen;
    private Item item;

    private int oppo;
    private String[] selectedPokemon;
    boolean multi;
    private boolean Changecharacter;
    private boolean skill;
    private boolean effect;
    int poket1 = 0;
    int poket2 = 0;

    public int getPoket1() {
        return poket1;
    }

    public void setPoket1(int poket1) {
        this.poket1 = poket1;
    }

    public int getPoket2() {
        return poket2;
    }

    public void setPoket2(int poket2) {
        this.poket2 = poket2;
    }

    public boolean isEffect() {
        return effect;
    }

    public void setEffect(boolean effect) {
        this.effect = effect;
    }

    public boolean isSkill() {
        return skill;
    }

    public void setSkill(boolean skill) {
        this.skill = skill;
    }


    public boolean getChangecharacter() {
        return setChangecharacter;
    }

    public void setChangecharacter(boolean setChangecharacter) {
        this.setChangecharacter = setChangecharacter;
    }

    private boolean setChangecharacter;

    public Battle(Pokemon game, boolean multi, Player player) {
        this.game = game;
//        this.battleScreen = battleScreen;
        this.multi = multi;

        assetManager = new AssetManager();
        assetManager.load("battle/battlepack.atlas", TextureAtlas.class);
        assetManager.load("font/han/gul.fnt", BitmapFont.class);
        assetManager.finishLoading();
        skin = SkinGenerator.generateSkin(assetManager);


        if (!multi) {
            pName = db.sP(playerID, playerNum + 1);
            P_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/" + pName + ".gif").read());
            String[] userKey = {playerID, String.valueOf(playerNum + 1)};
            this.player = new PK(userKey, P_T); //유저 포켓몬 가져오기

            String sql = "SELECT PM_ID FROM MAP_INFO WHERE LIVE = 'MAP01' ORDER BY RAND() LIMIT 1;"; //MAP_INFO 테이블에서 해당 맵의 랜덤 포켓몬 한개 가져오기
            String PM_ID = null;
            try {
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    PM_ID = rs.getString("PM_ID");
                }
            } catch (SQLException e) {
            }

            //일단 이상해풀로 가져옴
            wildKey = db.sP("PM_02");

            O_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/" + wildKey + ".gif").read());
            //this.opponent = new PK(wildKey, O_T); //야생 포켓몬
            this.opponent = new PK("PM_02", O_T); //야생 포켓몬
        }
        /*멀티 일때 실행*/
        else {
            oppoKey = new String[]{OppoID, String.valueOf(playerNum)};
            this.opponent = new PK(oppoKey, O_T); //상대 포켓몬
            /*pName = db.sP(game.getSelectedPokemon(0),parseInt(game.getSelectedPokemon(1)));
            //game에서 선택된 포켓몬 불러오기
           P_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/"+pName +".gif").read());

           String[] userKey = {game.getSelectedPokemon(0), game.getSelectedPokemon(1)};
           this.player = new PK(userKey, P_T); //유저 포켓몬 가져오기

           selectedPokemon =  game.getrecieveMessage().split(" ");
           oppoKey = new String[]{selectedPokemon[0], selectedPokemon[1]};
           wildKey = db.sP(selectedPokemon[0],parseInt(selectedPokemon[1]));
           O_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/" + wildKey+".gif").read());
           this.opponent = new PK(oppoKey, O_T); //상대 포켓몬*/
            pName = db.sP(game.getSelectedPokemon(0), parseInt(game.getSelectedPokemon(1)));
            //game에서 선택된 포켓몬 불러오기
            P_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/" + pName + ".gif").read());
            pName = db.sP(game.getSelectedPokemon(2), parseInt(game.getSelectedPokemon(3)));
            //game에서 선택된 포켓몬 불러오기
            P_T2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/" + pName + ".gif").read());

            String[] userKey1 = {game.getSelectedPokemon(0), game.getSelectedPokemon(1)};
            player2 = new PK(userKey1, P_T); //유저 포켓몬 가져오기
            String[] userKey2 = {game.getSelectedPokemon(2), game.getSelectedPokemon(3)};
            this.player = new PK(userKey2, P_T2); //유저 포켓몬 가져오기
            pTrainer = new Trainer(this.player, this.player2);

            selectedPokemon = game.getrecieveMessage().split(" ");
            oppoKey = new String[]{selectedPokemon[0], selectedPokemon[1]};
            wildKey = db.sP(selectedPokemon[0], parseInt(selectedPokemon[1]));
            O_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/" + wildKey + ".gif").read());
            opponent2 = new PK(oppoKey, O_T); //상대 포켓몬

            oppoKey = new String[]{selectedPokemon[2], selectedPokemon[3]};
            wildKey = db.sP(selectedPokemon[2], parseInt(selectedPokemon[3]));
            O_T2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/" + wildKey + ".gif").read());
            opponent = new PK(oppoKey, O_T); //상대 포켓몬

            oTrainer = new Trainer(opponent, opponent2);
        }

        mechanics = new BattleMechanics();
        setSkill(mechanics.goesFirst(this.player, opponent));
        this.state = STATE.READY_TO_PROGRESS;
    }

    public void progress(int input) {
        if (!multi) {
            if (state != STATE.READY_TO_PROGRESS) {
                return;
            }
            if (mechanics.goesFirst(player, opponent)) {
                playTurn(BATTLE_PARTY.PLAYER, input);
                if (state == STATE.READY_TO_PROGRESS) {
                    playTurn(BATTLE_PARTY.OPPONENT, 0);
                }
            } else {
                playTurn(BATTLE_PARTY.OPPONENT, 0);
                if (state == STATE.READY_TO_PROGRESS) {
                    playTurn(BATTLE_PARTY.PLAYER, input);
                }
            }
        } else {
            if (state != STATE.READY_TO_PROGRESS) {
                return;
            }
            if (mechanics.goesFirst(player, opponent)) {
                setSkill(true);
                playTurn(BATTLE_PARTY.PLAYER, input);
                if (state == STATE.READY_TO_PROGRESS) {
                    playTurn(BATTLE_PARTY.OPPONENT, parseInt(game.getrecieveMessage()));
                }
            } else {
                setSkill(false);
                playTurn(BATTLE_PARTY.OPPONENT, parseInt(game.getrecieveMessage()));
                if (state == STATE.READY_TO_PROGRESS) {
                    playTurn(BATTLE_PARTY.PLAYER, input);
                }
            }
        }
    }

    public boolean getGoesFirst() {
        return mechanics.goesFirst(player, opponent);
    }

    public void beginBattle() {
        System.out.print(player.getName());
        //queueEvent(new PokeSpriteEvent(opponent.getSprite(), BATTLE_PARTY.OPPONENT));
        queueEvent(new TextEvent("가랏! " + player.getName() + "!", 1f));
        //queueEvent(new PokeSpriteEvent(player.getSprite(), BATTLE_PARTY.PLAYER));
        // queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new PokeballAnimation()));
    }

    public void chooseNewPokemon(PK pokemon) {
        this.player = pokemon;
        queueEvent(new HPAnimationEvent(
                BATTLE_PARTY.PLAYER,
                pokemon.getCurrentChHP(),
                pokemon.getCurrentChHP(),
                pokemon.getChStat()[2],
                0.5f));
        //queueEvent(new PokeSpriteEvent(pokemon.getSprite(), BATTLE_PARTY.PLAYER));
        queueEvent(new NameChangeEvent(pokemon.getName(), BATTLE_PARTY.PLAYER));
        queueEvent(new TextEvent("가랏! " + pokemon.getName() + "!", 2));
        //queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new PokeballAnimation()));
        this.state = STATE.READY_TO_PROGRESS;
    }

    public void chooseNewPokemon2(PK pokemon) {
        this.opponent = pokemon;
        queueEvent(new HPAnimationEvent(
                BATTLE_PARTY.OPPONENT,
                pokemon.getCurrentChHP(),
                pokemon.getCurrentChHP(),
                pokemon.getChStat()[2],
                0.5f));
        //queueEvent(new PokeSpriteEvent(pokemon.getSprite(), BATTLE_PARTY.PLAYER));
        queueEvent(new NameChangeEvent(pokemon.getName(), BATTLE_PARTY.OPPONENT));
        queueEvent(new TextEvent("가랏! " + pokemon.getName() + "!", 2));

        //queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new PokeballAnimation()));
        this.state = STATE.READY_TO_PROGRESS;
    }

    public void attemptRun() {
        queueEvent(new TextEvent("도망치는데 성공했다..", false));
        this.state = STATE.RAN;
    }

    public void selectItem() {
        queueEvent(new TextEvent("무슨 아이템을 사용할까?", 0.5f));
        moveSelect.setVisible(false);
    }

    public void useItem(Item item) {
        this.item = item;
        queueEvent(new TextEvent(item.getName() + "을 사용했다.", 0.5f));
    }

    private void playTurn(BATTLE_PARTY user, int input) {
        //상대 포켓몬 공격 랜덤
       /* oppo = (int) (Math.random() * 4);*/
        BATTLE_PARTY target = BATTLE_PARTY.getOpposite(user);

        PK pokeUser = null;
        PK pokeTarget = null;
        if (user == BATTLE_PARTY.PLAYER) {
            pokeUser = player;
            pokeTarget = opponent;
        } else if (user == BATTLE_PARTY.OPPONENT) {
            pokeUser = opponent;
            pokeTarget = player;
        }
            String move = pokeUser.getSkill()[input];

            /* Broadcast the text graphics */
            queueEvent(new TextEvent(pokeUser.getName() + "의\n" + db.GET_PM_SK_NAME(move) + "!", 0.5f));

            /* 스킬 사용 횟수 적용*/
            pokeUser.applyCNT(input);

            int damage = mechanics.calculateDamage(pokeUser, input, pokeTarget);

            int hpBefore = pokeTarget.getCurrentChHP();

        System.out.println(damage+","+hpBefore);
            //해당 데미지 입음
            pokeTarget.applyDamage(damage);
            /* Broadcast HP change */
            queueEvent(
                    new HPAnimationEvent(
                            BATTLE_PARTY.getOpposite(user),
                            hpBefore,
                            pokeTarget.getCurrentChHP(),
                            pokeTarget.getChStat()[2],
                            0.5f));

           /* if (mechanics.hasMessage()) {
                queueEvent(new TextEvent(mechanics.getMessage(), 0.5f));
            }*/
        game.setOnoff(true);
        if (!multi) {
            if (player.isFainted()) {
                //queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new FaintingAnimation()));
                boolean anyoneAlive = false;
                for (int i = 0; i < getPTrainer().getTeamSize(); i++) {
                    if (!getPTrainer().getPokemon(i).isFainted()) {
                        anyoneAlive = true;
                        break;
                    }
                }
                for (int i = 0; i < getOTrainer().getTeamSize(); i++) {
                    if (!getOTrainer().getPokemon(i).isFainted()) {
                        anyoneAlive = true;
                        break;
                    }
                }
                if (anyoneAlive) {
                    queueEvent(new TextEvent(player.getName() + " fainted!", true));
                    this.state = STATE.SELECT_NEW_POKEMON;
                } else {
                    queueEvent(new TextEvent("Unfortunately, you've lost...", true));
                    this.state = STATE.LOSE;
                }
            } else if (opponent.isFainted()) {
                //queueEvent(new AnimationBattleEvent(BATTLE_PARTY.OPPONENT, new FaintingAnimation()));
                queueEvent(new TextEvent("Congratulations! You Win!", true));
                this.state = STATE.WIN;
            }
        } else {
            if (getPTrainer().getPokemon(poket1).isFainted()) {
                //queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new FaintingAnimation()));
                queueEvent(new TextEvent(getPTrainer().getPokemon(poket1).getName() + " fainted!", 1));
                setChangecharacter(true);
                setPoket1(1);
                this.state = STATE.SELECT_NEW_POKEMON;
            }
            if (getOTrainer().getPokemon(poket2).isFainted()) {
                //queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new FaintingAnimation()));
                queueEvent(new TextEvent(getOTrainer().getPokemon(poket2).getName() + " fainted!", 1));
                setChangecharacter(false);
                setPoket2(1);
                this.state = STATE.SELECT_NEW_POKEMON;
            }
            if (getPTrainer().getPokemon(1).isFainted()) {
                queueEvent(new TextEvent("Unfortunately, you've lost...", true));
                this.state = STATE.LOSE;
            }
            if (getOTrainer().getPokemon(1).isFainted()) {
                queueEvent(new TextEvent("Congratulations! You Win!", true));
                this.state = STATE.WIN;
            }
        }
    }

    public Trainer getPTrainer() {
        return pTrainer;
    }

    public Trainer getOTrainer() {
        return oTrainer;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public void setEventPlayer(BattleEventPlayer player) {
        eventPlayer = player;
    }

    @Override
    public void queueEvent(BattleEvent event) {
        eventPlayer.queueEvent(event);
    }


    public PK getP_P() {
        return player;
    }

    public PK getO_P() {
        return opponent;
    }
}