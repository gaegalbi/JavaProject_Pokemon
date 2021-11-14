package com.pokemon.model;

import com.badlogic.gdx.graphics.Texture;
import com.pokemon.ui.LoginUi;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static com.pokemon.db.db.con;
import static com.pokemon.db.db.rs;
import static com.pokemon.ui.LoginUi.playerID;

public class PK {
    private String name;
    private int LV;
    private int[] stat = new int[3]; //공, 방, 체
    private String[] skill = new String[4]; // PM_SK_S_01, PM_SK_S_02, PM_SK_S_03, PM_SK_S_04
    private Texture image;
    private int currentHP;
    private int battleNum;

    public PK(String key,Texture image){
        String sql = "SELECT PM_ID,PM_ATT,PM_DEF,PM_HP,PM_SK_S_01,PM_SK_S_02,PM_SK_S_03,PM_SK_S_04 FROM PM_INFO WHERE PM_ID ='"+key+"';";
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                this.name = rs.getString("PM_ID");
                this.LV = 5;
                //System.out.println(battleNum);
                this.stat[0] = rs.getInt("PM_ATT");
                this.stat[1] = rs.getInt("PM_DEF");
                this.stat[2] = rs.getInt("PM_HP");

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
    }

    //유저 포켓몬
    public PK(String[] key,Texture image) {
        //key[0] = playerID;
        //key[1] = PM_ID
        //key[2] = PM_NUM (int)
        //String sql = "SELECT PM_ID,PM_ATT,PM_DEF,PM_HP,PM_LV,PM_BATTLE,PM_SK_S_01,PM_SK_S_02,PM_SK_S_03,PM_SK_S_04 FROM PM WHERE U_ID='"+key[0]+"' and PM_ID='"+key[1]+"' and PM_NUM='"+Integer.parseInt(key[2]) +"';";
        String sql = "SELECT PM_ID,PM_ATT,PM_DEF,PM_HP,PM_LV,PM_BATTLE,PM_SK_S_01,PM_SK_S_02,PM_SK_S_03,PM_SK_S_04 FROM PM WHERE U_ID='"+key[0]+"' and PM_BATTLE="+Integer.parseInt(key[1])+";";
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
    }
    public int getCurrentHP(){ return currentHP;}

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
}
