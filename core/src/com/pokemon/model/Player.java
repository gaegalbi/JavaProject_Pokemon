package com.pokemon.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.pokemon.inventory.Crafting;
import com.pokemon.inventory.Equipment;
import com.pokemon.inventory.Inventory;
import com.pokemon.db.db;
import com.pokemon.inventory.Item;
import com.pokemon.util.AnimationSet;

import static com.pokemon.game.Settings.*;
import java.sql.SQLException;
import java.sql.Statement;

import static com.pokemon.db.db.con;
import static com.pokemon.db.db.rs;
import static com.pokemon.game.Settings.SCALE;
import static com.pokemon.game.Settings.TILE_SIZE;
import static com.pokemon.ui.LoginUi.playerID;

public class Player extends Rectangle implements RenderHelper, Comparable<RenderHelper> {
    private AnimationSet<TextureRegion> animations;
    private DIRECTION facing;
    private PLAYER_STATE state;
    private final float playerSizeX = TILE_SIZE;
    private final float playerSizeY = TILE_SIZE*1.5f;

    public float getSizeX() {
        return playerSizeX * SCALE;
    }

    public float getSizeY() {
        return playerSizeY * SCALE;
    }

    @Override
    public String getName() {
        return "Player";
    }

    private float ANIM_TIME = 0.6f;
    private float animTimer;

    // inventory and equips
    public Inventory inventory;
    public Equipment equips;
    public Crafting crafts;

    private int LV;
    private int EXP;
    //private int gold;
    private int RANK;
    public String skill[];
    private String skillName[] = {"채집","제작","공격력 증가","방어력 증가","체력 증가", "스피드 증가"};
    private int skill_LV[];
    private int skill_EXP[];

    public Player(int x, int y, AnimationSet<TextureRegion> animations) {
        inventory = new Inventory(playerID);
        equips = new Equipment(playerID);
        crafts = new Crafting();
        skill = db.GET_SK(playerID);
        skill_LV = db.GET_SK_LV(playerID);
        skill_EXP = db.GET_SK_EXP(playerID);

        String sql = "SELECT U_LV,U_EXP,U_RANK FROM USER WHERE U_ID ='"+playerID+"';";
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                this.LV = rs.getInt("U_LV");
                this.EXP = rs.getInt("U_EXP");
                this.RANK = rs.getInt("U_RANK");
                //this.GOLD = rs.getInt("U_GOLD");

            }
        }catch(SQLException e){
            System.out.println("SQLException" + e);
            e.printStackTrace();
        }

        this.width = 30;
        this.height = 24;
        this.x = x;
        this.y = y;
        this.animations = animations;
        this.state = PLAYER_STATE.STANDING;
        this.facing = DIRECTION.SOUTH;
    }

    public void update(float delta) {
        if (state == PLAYER_STATE.WALKING) {
            animTimer += delta;
            if (animTimer > ANIM_TIME) {
                animTimer = 0f;
            }
        }
    }

    @Override
    public Rectangle setX(float x) {
        return super.setX(x*SCALED_TILE_SIZE);
    }

    @Override
    public Rectangle setY(float y) {
        return super.setY(y*SCALED_TILE_SIZE);
    }

    @Override
    public int compareTo(RenderHelper o) {
        return (int) (y-o.getY());
    }

    public String getSkillName(int i) {
        return skillName[i];
    }

    public int getSkillLV(int i) {return skill_LV[i];
    }

    public int getSkillEXP(int i) {return skill_EXP[i];
    }

    public enum PLAYER_STATE {
        WALKING,
        STANDING
    }

    public TextureRegion getSprites() {
        if (state == PLAYER_STATE.WALKING) {
            return animations.getWalking(facing).getKeyFrame(animTimer);
        }
        return animations.getStanding(facing);
    }

    public void equip(Item item) {
        //아이템 효과에 따라 스킬 레벨 증가
        for(int i=0;i<6;i++){
            if(skill[i].equals(item.getEffect())){
                skill_LV[i] +=db.GET_E_V(item.getProperty());
            }
        }
    }

    public void unequip(Item item) {
        for(int i=0;i<6;i++){
            if(skill[i].equals(item.getEffect())){
                skill_LV[i] -=db.GET_E_V(item.getProperty());
            }
        }
    }
    //public int getGold(){return gold;}
    //public int setGold(int gold){return this.gold = gold;}
    public int getLV(){
        return LV;
    }
    public int getRANK(){return RANK;}
    public int getEXP(){
        return EXP;
    }
    public int getMaxEXP(){
        return db.GET_MAX_EXP(LV);
    }
    public int getSKLV(int index){return skill_LV[index];}
    public void setSKLV(int index, int count){skill_LV[index] = count;}

    public int getSKEXP(int index){return skill_EXP[index];}
    public void setSKEXP(int index,int count){ skill_EXP[index] = count;}


    public void finishMove() {
        state = PLAYER_STATE.STANDING;
    }

    public void setFacing(DIRECTION facing) {
        this.facing = facing;
    }

    public void setState(PLAYER_STATE state) {
        this.state = state;
    }

    public DIRECTION getFacing() {
        return facing;
    }

    public PLAYER_STATE getState() {
        return state;
    }
}
