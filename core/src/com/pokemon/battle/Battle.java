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

   public Battle(Pokemon game, BattleScreen battleScreen) {
       this.game = game;
       this.battleScreen = battleScreen;

       assetManager= new AssetManager();
       assetManager.load("ui/uipack.atlas", TextureAtlas.class);
       assetManager.load("font/han/gul.fnt", BitmapFont.class);
       assetManager.finishLoading();

       skin = SkinGenerator.generateSkin(assetManager);

       pName = db.sP(playerID,playerNum+1);


       P_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/"+pName +".gif").read());
       open= GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/open.gif").read());
       close= GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/close.gif").read());

       String[] userKey = {playerID, String.valueOf(playerNum+1)};
       this.player = new PK(userKey, P_T); //유저 포켓몬 가져오기

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


       mechanics = new BattleMechanics();
       this.state = STATE.READY_TO_PROGRESS;
    }
    public void progress(int input) {
        if (state != STATE.READY_TO_PROGRESS) {
            return;
        }
        //상대 포켓몬 공격 랜덤
        oppo = (int)(Math.random()*4);
       // System.out.println("동작 전 capture 값" + getO_P().isCapture());
        if (mechanics.goesFirst(player, opponent)) {
            playTurn(BATTLE_PARTY.PLAYER, input);
            System.out.println("포크볼" + pokeball);
            if (state == STATE.READY_TO_PROGRESS) {
                System.out.println("상대턴 시작해버림");
                playTurn(BATTLE_PARTY.OPPONENT, oppo); //여기서 랜덤값 넣기
            }
        } else {
            playTurn(BATTLE_PARTY.OPPONENT, oppo);//여기서 랜덤값
            if (state == STATE.READY_TO_PROGRESS) {
                playTurn(BATTLE_PARTY.PLAYER, input);
            }
        }
       // System.out.println("동작 후 capture 값" + getO_P().isCapture());
    }

    public void beginBattle() {
        //queueEvent(new PokeSpriteEvent(opponent.getSprite(), BATTLE_PARTY.OPPONENT));
        queueEvent(new TextEvent("가랏! "+player.getName()+"!", 1f));
        //queueEvent(new PokeSpriteEvent(player.getSprite(), BATTLE_PARTY.PLAYER));
    }

    public void chooseNewPokemon (PK pokemon){
        this.player = pokemon;
        queueEvent(new HPAnimationEvent(
                BATTLE_PARTY.PLAYER,
                pokemon.getCurrentHP(),
                pokemon.getCurrentHP(),
                pokemon.getStat()[2],
                0f));
        //queueEvent(new PokeSpriteEvent(pokemon.getSprite(), BATTLE_PARTY.PLAYER));
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
        if(input==4) {
            int hpBefore = pokeUser.getCurrentHP();
            if(item.getType()==7) {
                pokeUser.applyHeal(db.ITEMEFFECT(item.getKey()));
                queueEvent(
                        new HPAnimationEvent(
                                user,
                                hpBefore,
                                pokeUser.getCurrentHP(),
                                pokeUser.getStat()[2],
                                0.5f));
            }
           if(item.getType()==0 && !pokeball){
               per = getO_P().getStat()[2]/getO_P().getCurrentHP(); //크게 남으면 남을수록
               match = (int)(Math.random()*10);
               if(per>match) capture = true;
               else capture = false;

               pokeball = true;
            }
        }else {
            String move = pokeUser.getSkill()[input];
            /* Broadcast the text graphics */
            queueEvent(new TextEvent(pokeUser.getName() + "의\n" + db.GET_PM_SK_NAME(move) + "!", 0.5f));

            /* 스킬 사용 횟수 적용*/
            pokeUser.applyCNT(input);

            int damage = mechanics.calculateDamage(pokeUser, input, pokeTarget);

            int hpBefore = pokeTarget.getCurrentHP();

            //해당 데미지 입음
            pokeTarget.applyDamage(damage);
            /* Broadcast HP change */
            queueEvent(
                    new HPAnimationEvent(
                            BATTLE_PARTY.getOpposite(user),
                            hpBefore,
                            pokeTarget.getCurrentHP(),
                            pokeTarget.getStat()[2],
                            0.5f));

            if (mechanics.hasMessage()) {
                queueEvent(new TextEvent(mechanics.getMessage(), 0.5f));
            }

        }
            if (player.isFainted()) {
                //queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new FaintingAnimation()));
                boolean anyoneAlive = false;
                for (int i = 0; i < getPTrainer().getTeamSize(); i++) {
                    if (!getPTrainer().getPokemon(i).isFainted()) {
                        anyoneAlive = true;
                        break;
                    }
                }
                if (anyoneAlive) {
                    queueEvent(new TextEvent(player.getName() + "은(는) 기절했다!", true));
                    this.state = STATE.SELECT_NEW_POKEMON;
                } else {
                    queueEvent(new TextEvent("배틀에서 패배했습니다..", true));
                    this.state = STATE.LOSE;
                }
            } else if (opponent.isFainted()) {
                //queueEvent(new AnimationBattleEvent(BATTLE_PARTY.OPPONENT, new FaintingAnimation()));
                queueEvent(new TextEvent("배틀에서 승리했습니다!", true));
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