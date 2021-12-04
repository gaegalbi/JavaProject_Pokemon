package com.pokemon.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pokemon.ui.LoginUi;

import java.sql.SQLException;
import java.sql.Statement;
import com.pokemon.db.db;
import java.util.HashMap;
import java.util.Map;

import static com.pokemon.db.db.con;
import static com.pokemon.db.db.rs;
import static com.pokemon.ui.LoginUi.playerID;

public class PK {
    private String name;
    private int LV;
    private int[] stat = new int[4]; //공, 방, 체, 스피드
    private String[] skill = new String[4]; // PM_SK_S_01, PM_SK_S_02, PM_SK_S_03, PM_SK_S_04
    private int[] SK_CNT = new int[4];
    private int[] current_SK_CNT = new int[4];
    private Animation<TextureRegion> image;
    private int currentHP;
    private int battleNum;
    private String type;
    //야생 포켓몬
    public PK(String key, Animation<TextureRegion> image){
        String sql = "SELECT PM_ID,PM_ATT,PM_DEF,PM_HP,PM_SPEED,PM_SK_S_01,PM_SK_S_02,PM_SK_S_03,PM_SK_S_04 FROM PM_INFO WHERE PM_ID ='"+key+"';";
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                this.name = rs.getString("PM_ID");
                this.LV = 5; //맵에서 유저 레벨에따라 랜덤으로 가져옴
                //System.out.println(battleNum);
                this.stat[0] = rs.getInt("PM_ATT");
                this.stat[1] = rs.getInt("PM_DEF");
                this.stat[2] = rs.getInt("PM_HP");
                this.stat[3] = rs.getInt("PM_SPEED");

                this.skill[0] = rs.getString("PM_SK_S_01");
                this.skill[1] = rs.getString("PM_SK_S_02");
                this.skill[2] = rs.getString("PM_SK_S_03");
                this.skill[3] = rs.getString("PM_SK_S_04");
            }
          /*  rs = stmt.executeQuery(sql);
            while(rs.next()) {
                this.LV = (int) (rs.getInt("PM_LV")*(((int)(Math.random()*12)+8)/10.0)); //=> 맵 정보 테이블에서 해당 맵의 LV 가져오기
            }*/
        }catch(SQLException e){
            System.out.println("SQLException" + e);
            e.printStackTrace();
        }
        this.image = image;
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
    public PK(String[] key,Animation<TextureRegion> image) {
        //key[0] = playerID;
        //key[1] = PM_ID
        //key[2] = PM_NUM (int)
        //String sql = "SELECT PM_ID,PM_ATT,PM_DEF,PM_HP,PM_LV,PM_BATTLE,PM_SK_S_01,PM_SK_S_02,PM_SK_S_03,PM_SK_S_04 FROM PM WHERE U_ID='"+key[0]+"' and PM_ID='"+key[1]+"' and PM_NUM='"+Integer.parseInt(key[2]) +"';";
        String sql = "SELECT PM_ID,PM_ATT,PM_DEF,PM_HP,PM_SPEED,PM_LV,PM_BATTLE,PM_SK_S_01,PM_SK_S_02,PM_SK_S_03,PM_SK_S_04 FROM PM WHERE U_ID='"+key[0]+"' and PM_BATTLE="+Integer.parseInt(key[1])+";";
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


    public int[] getSK_CNT(){return SK_CNT; }
    public int[] getCurrent_SK_CNT(){return current_SK_CNT; }
    public void setCurrent_SK_CNT(int cnt){
        for(int i=0;i< skill.length;i++)
            current_SK_CNT[i] += cnt;
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
    public int[] getStat(){ return stat;}
    public String[] getSkill(){ return skill;}

    public Animation<TextureRegion> getImage(){ return image;}
    /* 데미지 적용 */
    public void applyDamage(int amount) {
        currentHP -= amount;
        if (currentHP < 0) {
            currentHP = 0;
        }
    }
    public void applyHeal(int amount) {
        currentHP += amount;
        if (currentHP > getStat()[2]) {
            currentHP = getStat()[2];
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
        return currentHP <= 0;
    }
}
