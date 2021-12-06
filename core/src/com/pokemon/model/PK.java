package com.pokemon.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pokemon.db.db;
import com.pokemon.util.GifDecoder;

import java.sql.SQLException;
import java.sql.Statement;

import static com.pokemon.db.db.*;
import static com.pokemon.ui.LoginUi.playerID;

public class PK {
    private String name;
    private int LV;
    private int[] stat = new int[4]; //공, 방, 체, 스피드
    private int[] chStat = new int[4]; //공, 방, 체, 스피드
    private String[] skill = new String[4]; // PM_SK_S_01, PM_SK_S_02, PM_SK_S_03, PM_SK_S_04
    private int[] SK_CNT = new int[4];
    private int[] current_SK_CNT = new int[4];
    private Animation<TextureRegion> image;
    private int currentHP;
    private int battleNum;
    private boolean capture;
    private String type;
    private int EXP;
    private String RealName;


    private int currentChHP;
    private int skill_LV[];
    private Player player;

    //야생 포켓몬
    public PK(String key[]){
        String sql = "SELECT PM_ATT,PM_DEF,PM_HP,PM_SPEED,PM_SK_S_01,PM_SK_S_02,PM_SK_S_03,PM_SK_S_04 FROM PM_INFO WHERE PM_ID ='"+key[0]+"';";
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {

                //System.out.println(battleNum);
                this.chStat[0] = rs.getInt("PM_ATT");
                this.chStat[1] = rs.getInt("PM_DEF");
                this.chStat[2] = rs.getInt("PM_HP");
                this.chStat[3] = rs.getInt("PM_SPEED");

                this.skill[0] = rs.getString("PM_SK_S_01");
                this.skill[1] = rs.getString("PM_SK_S_02");
                this.skill[2] = rs.getString("PM_SK_S_03");
                this.skill[3] = rs.getString("PM_SK_S_04");
            }
            this.LV = Integer.parseInt(key[1]); //맵에서 유저 레벨에따라 랜덤으로 가져옴
        }catch(SQLException e){
            System.out.println("SQLException" + e);
            e.printStackTrace();
        }
        //상대포켓몬은 앞모습으로 받아옴
        this.RealName =sP(key[0]);
        this.name = key[0];
        this.image = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/" + RealName+".gif").read());

        this.currentChHP = chStat[2];
        this.type = db.GET_PMType(name);
        /* 스킬 횟수 넣기 */
        this.SK_CNT[0] = db.GET_PM_SK_CNT(this.skill[0]);
        this.SK_CNT[1] = db.GET_PM_SK_CNT(this.skill[1]);
        this.SK_CNT[2] = db.GET_PM_SK_CNT(this.skill[2]);
        this.SK_CNT[3] = db.GET_PM_SK_CNT(this.skill[3]);
        this.current_SK_CNT[0] = db.GET_PM_SK_CNT(this.skill[0]);
        this.current_SK_CNT[1] = db.GET_PM_SK_CNT(this.skill[1]);
        this.current_SK_CNT[2] = db.GET_PM_SK_CNT(this.skill[2]);
        this.current_SK_CNT[3] = db.GET_PM_SK_CNT(this.skill[3]);
    }
    //상대 포켓몬인데
    public PK(String[] key,Animation<TextureRegion> image) {
        skill = db.GET_SK(key[0]);
        skill_LV = db.GET_SK_LV(key[0]);
        String sql = "SELECT PM_ID,PM_ATT,PM_DEF,PM_HP,PM_currentHP,PM_SPEED,PM_LV,PM_BATTLE,PM_SK_S_01,PM_SK_S_02,PM_SK_S_03,PM_SK_S_04 FROM PM WHERE U_ID='"+key[0]+"' and PM_BATTLE="+Integer.parseInt(key[1])+";";
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                this.name = rs.getString("PM_ID");
                this.LV = rs.getInt("PM_LV");
                this.battleNum = rs.getInt("PM_BATTLE");

                //System.out.println(battleNum);
                this.stat[0] = rs.getInt("PM_ATT");
                this.stat[1] = rs.getInt("PM_DEF");
                this.stat[2] = rs.getInt("PM_HP");
                this.currentChHP = rs.getInt("PM_currentHP");
                this.stat[3] = rs.getInt("PM_SPEED");

                this.skill[0] = rs.getString("PM_SK_S_01");
                this.skill[1] = rs.getString("PM_SK_S_02");
                this.skill[2] = rs.getString("PM_SK_S_03");
                this.skill[3] = rs.getString("PM_SK_S_04");

            }
        }catch(SQLException e){
            System.out.println("SQLException" + e);
            e.printStackTrace();
        }
        this.image = image;
        //스킬로 변경된 스텟
        for(int i=0;i<stat.length;i++){
            if(i==2)
                chStat[i]= stat[i]+skill_LV[i+2]+10; //체력
            else
                chStat[i]= stat[i]+ skill_LV[i+2]*5;
        }
        this.currentHP = stat[2];
        this.type = db.GET_PMType(name);
        /* 스킬 횟수 넣기 */
        this.SK_CNT[0] = db.GET_PM_SK_CNT(this.skill[0]);
        this.SK_CNT[1] = db.GET_PM_SK_CNT(this.skill[1]);
        this.SK_CNT[2] = db.GET_PM_SK_CNT(this.skill[2]);
        this.SK_CNT[3] = db.GET_PM_SK_CNT(this.skill[3]);
        this.current_SK_CNT[0] = db.GET_PM_SK_CNT(this.skill[0]);
        this.current_SK_CNT[1] = db.GET_PM_SK_CNT(this.skill[1]);
        this.current_SK_CNT[2] = db.GET_PM_SK_CNT(this.skill[2]);
        this.current_SK_CNT[3] = db.GET_PM_SK_CNT(this.skill[3]);
    }
        //이미지 필요없게
        public PK(Player player, String[] key) {
            String sql = "SELECT PM_ID,PM_ATT,PM_DEF,PM_HP,PM_currentHP,PM_SPEED,PM_LV,PM_EXP,PM_BATTLE,PM_SK_S_01,PM_SK_S_02,PM_SK_S_03,PM_SK_S_04 FROM PM WHERE U_ID='"+key[0]+"' and PM_BATTLE="+Integer.parseInt(key[1])+";";
            try {
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next()) {
                    this.name = rs.getString("PM_ID");
                    this.LV = rs.getInt("PM_LV");
                    this.battleNum = rs.getInt("PM_BATTLE");
                    this.EXP = rs.getInt("PM_EXP");
                    this.stat[0] = rs.getInt("PM_ATT");
                    this.stat[1] = rs.getInt("PM_DEF");
                    this.stat[2] = rs.getInt("PM_HP");
                    this.currentChHP = rs.getInt("PM_currentHP");
                    this.stat[3] = rs.getInt("PM_SPEED");

                    this.skill[0] = rs.getString("PM_SK_S_01");
                    this.skill[1] = rs.getString("PM_SK_S_02");
                    this.skill[2] = rs.getString("PM_SK_S_03");
                    this.skill[3] = rs.getString("PM_SK_S_04");


                }

            }catch(SQLException e){
                System.out.println("SQLException" + e);
                e.printStackTrace();
            }
            this.RealName =sP(key[0],Integer.parseInt(key[1]));
            this.image = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/back/" + RealName+".gif").read());
            //스킬로 변경된 스텟
            for(int i=0;i<stat.length;i++){
                if(i==2)
                    chStat[i]= stat[i]+ player.getSKLV(i+2)+10; //체력
                else
                    chStat[i]= stat[i]+ player.getSKLV(i+2)*5;

            }
            this.currentHP = stat[2];
            this.type = db.GET_PMType(name);
            /* 스킬 횟수 넣기 */
            this.SK_CNT[0] = db.GET_PM_SK_CNT(this.skill[0]);
            this.SK_CNT[1] = db.GET_PM_SK_CNT(this.skill[1]);
            this.SK_CNT[2] = db.GET_PM_SK_CNT(this.skill[2]);
            this.SK_CNT[3] = db.GET_PM_SK_CNT(this.skill[3]);
            this.current_SK_CNT[0] = db.GET_PM_SK_CNT(this.skill[0]);
            this.current_SK_CNT[1] = db.GET_PM_SK_CNT(this.skill[1]);
            this.current_SK_CNT[2] = db.GET_PM_SK_CNT(this.skill[2]);
            this.current_SK_CNT[3] = db.GET_PM_SK_CNT(this.skill[3]);
        }

    //유저 포켓몬
    public PK(Player player, String[] key, Animation<TextureRegion> image) {
        String sql = "SELECT PM_ID,PM_ATT,PM_DEF,PM_HP,PM_currentHP,PM_SPEED,PM_LV,PM_EXP,PM_BATTLE,PM_SK_S_01,PM_SK_S_02,PM_SK_S_03,PM_SK_S_04 FROM PM WHERE U_ID='"+key[0]+"' and PM_BATTLE="+Integer.parseInt(key[1])+";";
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
               this.name = rs.getString("PM_ID");
               this.LV = rs.getInt("PM_LV");
               this.battleNum = rs.getInt("PM_BATTLE");
                this.EXP = rs.getInt("PM_EXP");
               this.stat[0] = rs.getInt("PM_ATT");
               this.stat[1] = rs.getInt("PM_DEF");
               this.stat[2] = rs.getInt("PM_HP");
               this.currentChHP = rs.getInt("PM_currentHP");
               this.stat[3] = rs.getInt("PM_SPEED");

               this.skill[0] = rs.getString("PM_SK_S_01");
               this.skill[1] = rs.getString("PM_SK_S_02");
               this.skill[2] = rs.getString("PM_SK_S_03");
               this.skill[3] = rs.getString("PM_SK_S_04");
            }

        }catch(SQLException e){
            System.out.println("SQLException" + e);
            e.printStackTrace();
        }
        this.image = image;
        //스킬로 변경된 스텟
        for(int i=0;i<stat.length;i++){
            if(i==2)
                chStat[i]= stat[i]+ player.getSKLV(i+2)+10; //체력
            else
                chStat[i]= stat[i]+ player.getSKLV(i+2)*5;

        }
        this.currentHP = stat[2];
        this.type = db.GET_PMType(name);
        /* 스킬 횟수 넣기 */
        this.SK_CNT[0] = db.GET_PM_SK_CNT(this.skill[0]);
        this.SK_CNT[1] = db.GET_PM_SK_CNT(this.skill[1]);
        this.SK_CNT[2] = db.GET_PM_SK_CNT(this.skill[2]);
        this.SK_CNT[3] = db.GET_PM_SK_CNT(this.skill[3]);
        this.current_SK_CNT[0] = db.GET_PM_SK_CNT(this.skill[0]);
        this.current_SK_CNT[1] = db.GET_PM_SK_CNT(this.skill[1]);
        this.current_SK_CNT[2] = db.GET_PM_SK_CNT(this.skill[2]);
        this.current_SK_CNT[3] = db.GET_PM_SK_CNT(this.skill[3]);
    }
    public int getCurrentHP(){ return currentHP;}
    public int getCurrentChHP(){ return currentChHP;}
    public void setCurrentChHP(int hp){ this.currentChHP = hp;}

    public int getEXP(){
        return EXP;
    }
    public void setEXP(int EXP){
        this.EXP = EXP;
    }

    public int[] getSK_CNT(){return SK_CNT; }
    public int[] getCurrent_SK_CNT(){return current_SK_CNT; }
    public void setCurrent_SK_CNT(int index,int cnt){
        this.current_SK_CNT[index] = cnt;
        }

    public String getName(){
        String conName = null;
        try {
            String sql = "Select PM_NAME FROM PM_INFO WHERE PM_ID='" +name+"';";
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                conName = rs.getString("PM_NAME");
            }
        }catch(SQLException e){}
        return conName;
    }
    public int getLV(){ return LV;}
    public void setLV(int LV){ this.LV=LV;}
    public int[] Stat(){ return stat;}
    public int[] getChStat(){ return chStat;}
    public String[] getSkill(){ return skill;}

    public Animation<TextureRegion> getImage(){ return image;}
    /* 데미지 적용 */
    public void applyDamage(int amount) {
        currentChHP -= amount;
        if (currentChHP < 0) {
            currentChHP = 0;
        }
        System.out.println(currentChHP);
    }
    public void applyHeal(int amount) {
        currentChHP += amount;
        if (currentChHP > getChStat()[2]) {
            currentChHP = getChStat()[2];
        }
    }
    public void applyHealPP(int amount) {
        for(int i=0;i< skill.length;i++){
            current_SK_CNT[i] +=amount;
            if (current_SK_CNT[i] > SK_CNT[i]) {
                current_SK_CNT[i] = SK_CNT[i];
            }
        }
    }
    /* 스킬 사용 횟수 적용 */
    public void applyCNT(int num) {
        current_SK_CNT[num] -= 1;
    }

    public String getType(){
        return type;
    }
    public boolean isFainted() {
        return currentChHP == 0;
    }
    public boolean isCapture() {
        return capture;
    }
    public void setCapture(boolean capture) {
        this.capture = capture;
    }
}
