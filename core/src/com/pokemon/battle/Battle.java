package com.pokemon.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pokemon.model.PK;
import com.pokemon.db.db;
import com.pokemon.util.GifDecoder;

import java.sql.SQLException;
import java.sql.Statement;

import static com.pokemon.db.db.con;
import static com.pokemon.db.db.rs;
import static com.pokemon.screen.BattleScreen.playerNum;
import static com.pokemon.ui.LoginUi.playerID;

public class Battle {
    public enum STATE {
        READY_TO_PROGRESS,
        SELECT_NEW_POKEMON,
        RAN,
        WIN,
        LOSE,
        ;
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

    private AssetManager assetManager;

    public static String OppoID;

    private String[] oppoKey;
    private String wildKey;

   // private BattleMechanics mechanics;
/*    public Battle(Trainer player, Trainer oTrainer) {
        this.pTrainer = player;
        this.oTrainer = oTrainer;

        this.player = player.getPokemon(0);
        this.opponent = oTrainer.getPokemon(0);
        mechanics = new BattleMechanics();
        this.state = STATE.READY_TO_PROGRESS;
    }*/
   public Battle(boolean multi) {
        assetManager = new AssetManager();
        assetManager.load("battle/battlepack.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        pName = db.sP(playerID,playerNum+1);

       P_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/"+pName +".gif").read());
       O_T = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/" + pName+".gif").read());

        String[] userKey = {playerID, String.valueOf(playerNum+1)};
        this.player = new PK(userKey, P_T); //유저 포켓몬 가져오기
        if(!multi) {
            String sql = "SELECT PM_ID FROM MAP_INFO WHERE LIVE = 'MAP01' ORDER BY RAND() LIMIT 1;"; //MAP_INFO 테이블에서 해당 맵의 랜덤 포켓몬 한개 가져오기
            String PM_ID = null;
            try {
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next()) {
                    PM_ID = rs.getString("PM_ID");
                }
            }catch(SQLException e){};
            wildKey = PM_ID;
           //this.opponent = new PK(wildKey, O_T); //야생 포켓몬
           this.opponent = new PK("PM_02", O_T); //야생 포켓몬
        }
        else {
            oppoKey = new String[]{OppoID, String.valueOf(playerNum)};
            this.opponent = new PK(oppoKey, O_T); //상대 포켓몬
        }
    }

    public PK getP_P() {
        return player;
    }

    public PK getO_P() {
        return opponent;
    }
}