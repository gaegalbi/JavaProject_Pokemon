package com.pokemon.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.pokemon.battle.event.*;
import com.pokemon.game.Pokemon;
import com.pokemon.inventory.Item;
import com.pokemon.model.PK;
import com.pokemon.db.db;
import com.pokemon.model.Player;
import com.pokemon.screen.BattleRenderer;
import com.pokemon.screen.BattleScreen;
import com.pokemon.screen.EventQueueRenderer;
import com.pokemon.ui.AbstractUi;
import com.pokemon.ui.DialogueBox;
import com.pokemon.ui.MoveSelectBox;
import com.pokemon.util.GifDecoder;
import com.pokemon.util.SkinGenerator;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;

import static com.pokemon.controller.BattleScreenController.moveSelect;
import static com.pokemon.db.db.con;
import static com.pokemon.db.db.rs;

import static com.pokemon.screen.BattleScreen.playerNum;;
import static com.pokemon.ui.LoginUi.playerID;

public class Battle implements BattleEventQueuer {
    public enum STATE {
        READY_TO_PROGRESS,
        SELECT_NEW_POKEMON,
        RAN,
        WIN,
        LOSE,
        USEITEM,
    }
    private STATE state;
    private PK player;
    private PK opponent;
    private String pName;
    private String oName;
    private Trainer pTrainer;
    private Trainer oTrainer;
   //private Texture P_T;
   //private Texture O_T;
    private Animation<TextureRegion> P_T;
    private Animation<TextureRegion> O_T;
    private Animation<TextureRegion> open;
    private Animation<TextureRegion> close;


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

   public Battle(Pokemon game, BattleScreen battleScreen,Player userPlayer) {
       this.game = game;
       this.battleScreen = battleScreen;
       this.userPlayer=userPlayer;

       assetManager= new AssetManager();
       assetManager.load("ui/uipack.atlas", TextureAtlas.class);
       assetManager.load("font/han/gul.fnt", BitmapFont.class);
       assetManager.finishLoading();

       skin = SkinGenerator.generateSkin(assetManager);



       open= GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/open.gif").read());
       close= GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/close.gif").read());
       pName = db.sP(playerID,playerNum);

       userKey = new String[]{playerID, String.valueOf(playerNum)};

       P_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/"+pName +".gif").read());

       this.player = new PK(userPlayer,userKey, P_T); //유저 포켓몬 가져오기

       if(player.getCurrentHP()<=0){
           userKey = new String[]{playerID, String.valueOf(playerNum+=1)};
           pName = db.sP(playerID,playerNum);
           P_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/"+pName +".gif").read());
           this.player = new PK(userPlayer,userKey, P_T); //유저 포켓몬 가져오기
       }


  /*
           String sql = "SELECT PM_ID FROM MAP_INFO WHERE LIVE = 'MAP01' ORDER BY RAND() LIMIT 1;"; //MAP_INFO 테이블에서 해당 맵의 랜덤 포켓몬 한개 가져오기
           String PM_ID = null;
           try {
               Statement stmt = con.createStatement();
               rs = stmt.executeQuery(sql);
               while(rs.next()) {
                   PM_ID = rs.getString("PM_ID");
               }
           }catch(SQLException e){};*/

           //일단 이상해풀로 가져옴
           wildKey = db.sP("PM_02");

           O_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/" + wildKey+".gif").read());
           //this.opponent = new PK(wildKey, O_T); //야생 포켓몬
           this.opponent = new PK("PM_02", O_T); //야생 포켓몬

         /*   oppoKey = new String[]{OppoID, String.valueOf(playerNum)};
            this.opponent = new PK(oppoKey, O_T); //상대 포켓몬*/

       pTrainer = new Trainer(this.player);
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
        this.player = pokemon;
        queueEvent(new HPAnimationEvent(
                BATTLE_PARTY.PLAYER,
                pokemon.getCurrentChHP(),
                pokemon.getCurrentChHP(),
                pokemon.getChStat()[2],
                0.5f));
        queueEvent(new NameChangeEvent(pokemon.getName(), BATTLE_PARTY.PLAYER));
        queueEvent(new TextEvent("가랏! " + pokemon.getName() + "!"));
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
                                    1f));
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
                                    pokeTarget.getCurrentChHP(),
                                    pokeTarget.getChStat()[2],
                                    1f));
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
                                    1f));
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
                    db.PM_HP_UPDATE(pokeUser,playerNum);
                    this.state = STATE.SELECT_NEW_POKEMON;
                } else {
                    queueEvent(new TextEvent("배틀에서 패배했습니다..", true));
                    db.PM_HP_UPDATE(pokeUser,playerNum);
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