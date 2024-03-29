package com.pokemon.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pokemon.battle.event.*;
import com.pokemon.db.db;
import com.pokemon.game.Pokemon;
import com.pokemon.inventory.Item;
import com.pokemon.model.PK;
import com.pokemon.model.Player;
import com.pokemon.screen.BattleScreen;
import com.pokemon.screen.GameScreen;
import com.pokemon.util.GifDecoder;
import com.pokemon.util.SkinGenerator;

import static com.pokemon.controller.SingleBattleScreenController.moveSelect;
import static com.pokemon.screen.BattleScreen.playerNum;
import static com.pokemon.ui.LoginUi.playerID;


public class SingleBattle implements BattleEventQueuer {
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
    private String pName;
    private String oName;
    private Trainer pTrainer;
    private Trainer oTrainer;
    private Animation<TextureRegion> P_T;
    private Animation<TextureRegion> O_T;

    private AssetManager assetManager;
    private BattleEventPlayer eventPlayer;
    private BattleMechanics mechanics;

    public static String OppoID;
    private String[] oppoKey;
    private String[] wildKey;
    private Skin skin;
    private Pokemon game;
    private BattleScreen battleScreen;
    private Item item;
    private boolean pokeball =false;
    private boolean capture = false;
    private int per;
    private int match;
    private int oppo;
    private boolean turn;
    private String[] userKey;
    private Player userPlayer;
    private boolean skill;
    private int input;
    private int uDamage;
    private int oDamage;
    public static boolean check;

   public SingleBattle(Pokemon game, BattleScreen battleScreen, Player userPlayer) {
       this.game = game;
       this.battleScreen = battleScreen;
       this.userPlayer=userPlayer;
       playerNum = 1;

       assetManager= new AssetManager();
       assetManager.load("ui/uipack.atlas", TextureAtlas.class);
       assetManager.load("font/han/gul.fnt", BitmapFont.class);
       assetManager.finishLoading();

       skin = SkinGenerator.generateSkin(assetManager);
       //현재 맵 정보를 바탕으로 포켓몬 가져옴
       wildKey = db.sP(GameScreen.getWorld().getBackground(),userPlayer);
       this.opponent = new PK(wildKey);

        //유저가 가진 모든 포켓몬 가져옴
       pTrainer = new Trainer(userPlayer,playerID);

       for (int i = 0; i < getPTrainer().getTeamSize(); i++) {
           if (!getPTrainer().getPokemon(i).isFainted()) {
                this.player = pTrainer.getPokemon(i);
               break;
           }
           playerNum++;
       }

       oTrainer = new Trainer(this.opponent);

       mechanics = new BattleMechanics();
       setSkill(mechanics.goesFirst(player, opponent));
       this.state = STATE.READY_TO_PROGRESS;
    }
    public boolean isSkill() {
        return skill;
    }
    public void setSkill(boolean skill) {
        this.skill = skill;
    }

    public void progress(int input) {
        if (state != STATE.READY_TO_PROGRESS) {
            return;
        }
        //상대 포켓몬 공격 랜덤 및 PP 적용
        oppo = (int)(Math.random()*4);
        if(opponent.getCurrent_SK_CNT()[oppo]<=0){
            while(opponent.getCurrent_SK_CNT()[oppo]<=0){
                oppo = (int)(Math.random()*4);
            }
        }

        if (mechanics.goesFirst(player, opponent)) {
            setSkill(true);
            playTurn(BATTLE_PARTY.PLAYER, input);
            if (state == STATE.READY_TO_PROGRESS) {
                playTurn(BATTLE_PARTY.OPPONENT, oppo);
            }
        } else {
            playTurn(BATTLE_PARTY.OPPONENT, oppo);
            setSkill(false);
            if (state == STATE.READY_TO_PROGRESS) {
                playTurn(BATTLE_PARTY.PLAYER, input);
            }
        }
    }

    public void beginBattle() {
        queueEvent(new HPAnimationEvent(
                BATTLE_PARTY.PLAYER,
                player.getCurrentChHP(),
                player.getCurrentChHP(),
                player.getChStat()[2],
                0.5f));
        //queueEvent(new PokeSpriteEvent(opponent.getSprite(), BATTLE_PARTY.OPPONENT));
        queueEvent(new TextEvent("가랏! "+player.getName()+"!", 1f));
        //queueEvent(new PokeSpriteEvent(player.getSprite(), BATTLE_PARTY.PLAYER));
    }

    public void chooseNewPokemon (PK pokemon){
       check = true; //교체시 Type 이펙트 변경
        this.player = pokemon;
        playerNum++;
        queueEvent(new HPAnimationEvent(
                BATTLE_PARTY.PLAYER,
                pokemon.getCurrentChHP(),
                pokemon.getCurrentChHP(),
                pokemon.getChStat()[2],
                0.5f));
        queueEvent(new NameChangeEvent(pokemon.getName(), BATTLE_PARTY.PLAYER));
        queueEvent(new TextEvent("가랏! "+pokemon.getName()+"!", 1f));
        this.state = STATE.READY_TO_PROGRESS;
    }
    public void attemptRun () {
        queueEvent(new TextEvent("도망치는데 성공했다..", false));
        this.state = STATE.RAN;
    }

    public void selectItem(){
        queueEvent(new TextEvent("무슨 아이템을 사용할까?", 0.5f));
        moveSelect.setVisible(false);
    }
    public void useItem(Item item){
       this.item = item;
        queueEvent(new TextEvent(item.getName() + "을 사용했다.", 0.5f));
    }

    private void playTurn(BATTLE_PARTY user,int input){
        PK pokeUser = null;
        PK pokeTarget = null;

        if (user == BATTLE_PARTY.PLAYER) {
            pokeUser = player;
            pokeTarget = opponent;
        } else if (user == BATTLE_PARTY.OPPONENT) {
            pokeUser = opponent;
            pokeTarget = player;
        }

        if(input==4) {
            this.input = input;
            int hpBefore = pokeUser.getCurrentChHP();
            //회복아이템
            if(item.getType()==7&&(item.getProperty().equals("HP1")||item.getProperty().equals("HP2")
                    ||item.getProperty().equals("HP3") ||item.getProperty().equals("HP4"))){
                pokeUser.applyHeal(db.ITEMEFFECT(item.getKey()));
                queueEvent(
                        new HPAnimationEvent(
                                user,
                                hpBefore,
                                pokeUser.getCurrentChHP(),
                                pokeUser.getChStat()[2],
                                1f));
            }
            //PP아이템
            if(item.getType()==7&&(item.getProperty().equals("PP1")||item.getProperty().equals("PP2"))){
                pokeUser.applyHealPP(db.ITEMEFFECT(item.getKey()));
            }
           if(item.getType()==0 && !pokeball){
               per = getO_P().getChStat()[2]/getO_P().getCurrentChHP(); //크게 남으면 남을수록
               match = (int)(Math.random()*10);
               if(per>match) capture = true;
               else capture = false;
               pokeball = true;
               if(capture){
                   db.CAPTURE(getO_P().getRealName());
               }
           }
        }else {
            //잡히지않아야 실행
            if(capture==false) {
                String move = pokeUser.getSkill()[input];

                /* Broadcast the text graphics */
                queueEvent(new TextEvent(pokeUser.getName() + "의\n" + db.GET_PM_SK_NAME(move) + "!", 0.5f));

                /* 스킬 사용 횟수 적용*/
                pokeUser.applyCNT(input);

                int damage = mechanics.calculateDamage(pokeUser, input, pokeTarget);
                if(pokeUser==getP_P()) {
                    this.uDamage = damage;
                }
                if(pokeUser==getO_P()) {
                    this.oDamage = damage;
                }
                int heal = mechanics.calculateHeal(pokeUser,input,damage);
                int selfDamage = mechanics.calculateSelfDamage(pokeUser,input,damage);

                int hpBefore = pokeTarget.getCurrentChHP();
                int hpHealBefore = pokeUser.getCurrentChHP();
                int hpSelfBefore = pokeUser.getCurrentChHP();
                //HP애니메이션 이상할 경우 duration
                if(heal>0) {
                    //해당 회복량을 회복함
                    pokeUser.applyHeal(heal);
                    queueEvent(
                            new HPAnimationEvent(
                                    user,
                                    hpHealBefore,
                                    pokeUser.getCurrentChHP(),
                                    pokeUser.getChStat()[2],
                                    0.8f));
                }
                if(damage>0) {
                    //버그 실제 currentHP보다 1작게 표시됨
                    //해당 데미지를 상대가 입음

                    pokeTarget.applyDamage(damage);
                    /* Broadcast HP change */
                    queueEvent(
                            new HPAnimationEvent(
                                    BATTLE_PARTY.getOpposite(user),
                                    hpBefore,
                                    pokeTarget.getCurrentChHP()+1,
                                    pokeTarget.getChStat()[2],
                                    0.8f));
                }
                if(selfDamage>0) {
                    //해당 데미지를 유저가 입음
                    pokeUser.applyDamage(selfDamage);
                    /* Broadcast HP change */
                    queueEvent(
                            new HPAnimationEvent(
                                    user,
                                    hpSelfBefore,
                                    pokeUser.getCurrentChHP(),
                                    pokeUser.getChStat()[2],
                                    0.8f));
                }


            }
        }
        game.setOnoff(true);
            if (player.isFainted()) {
                boolean anyoneAlive = false;
                for (int i = 0; i < getPTrainer().getTeamSize(); i++) {
                    if (!getPTrainer().getPokemon(i).isFainted()) {
                        anyoneAlive = true;
                        break;
                    }
                }
                if (anyoneAlive) {
                    queueEvent(new TextEvent(player.getName() + "은(는) 기절했다!", true));
                    db.PM_HP_UPDATE(player,playerNum);
                    this.state = STATE.SELECT_NEW_POKEMON;
                } else {
                    queueEvent(new TextEvent("배틀에서 패배했습니다..", true));
                    db.PM_HP_UPDATE(player,playerNum);
                    this.state = STATE.LOSE;
                }
            } else if (opponent.isFainted()) {
                queueEvent(new TextEvent("배틀에서 승리했습니다!", true));
                pokeUser.setEXP(pokeUser.getEXP()+10);
                if(pokeUser.getEXP() ==db.PM_EXP(pokeUser))
                    pokeUser.setLV(pokeUser.getLV()+1);
                db.PM_LV_UPDATE(pokeUser,playerNum);
                db.PM_EXP_UPDATE(pokeUser,playerNum);
                db.PM_HP_UPDATE(pokeUser,playerNum);
                this.state = STATE.WIN;
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
    public int getInput(){
       return input;
    }
    public int getUDamage(){
        return uDamage;
    }
    public int getODamage(){
        return oDamage;
    }

    public PK getP_P() {
        return player;
    }

    public PK getO_P() {
        return opponent;
    }

    public boolean getCapture(){
       return capture;
    }

    public boolean getPokeball(){
       return pokeball;
    }
    public void setPokeball(boolean pokeball){
       this.pokeball=pokeball;
    }
}