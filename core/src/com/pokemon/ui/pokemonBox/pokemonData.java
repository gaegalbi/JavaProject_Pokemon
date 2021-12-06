package com.pokemon.ui.pokemonBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.pokemon.db.db;
import com.pokemon.util.SkinGenerator;

import java.security.Key;

import static com.pokemon.ui.LoginUi.playerID;


public class pokemonData {
    public static String[] nameStr = {"메가니움","강챙이","고라파덕","딱구리","거북왕", "리자몽"};

    private int key;
    public String name;
    private String LV;
    private int HP;
    private int myHP;
    public Image myPokemon;
    public Image selectedSlot;
    public Label nameLabel;
    public Label LVLabel;
    public Image bar;
    public Image barEXP_green;
    public Image barEXP_yellow;
    public Image barEXP_red;
    public int barColor;

    private AssetManager assetManager;
    private Skin skin;

    private int[] HPs = {100,120,140,160,180,200};
    private int[] myHPs = {100,100,80,80,60,0};

    public pokemonData(int key) {
//        String sql = "SELECT ITEM_NAME, ITEM_INFO,ITEM_PROPERTY,ITEM_EFFECT, ITEM_TYPE FROM ITEM WHERE ITEM_ID ='"+key+"';";
//
//        try {
//            Statement stmt = con.createStatement();
//            rs = stmt.executeQuery(sql);
//            while (rs.next()) {
//                this.name = rs.getString("ITEM_NAME"); //몬스터 명
//                this.LV = rs.getString("ITEM_INFO"); //몬스터 레벨
//                this.HP = rs.getString("ITEM_PROPERTY"); //HP
//                this.myHP = rs.getString("ITEM_EFFECT"); //나의 현재 HP
//
//                selectedSlot = new Image(new Texture(Gdx.files.internal("pokemon/ball/" + name + "-.png")));
//                myPokemon = new Image(new Texture(Gdx.files.internal("pokemon/ball/" + name + ".png")));
//            }
//        }catch(SQLException e){
//            System.out.println("SQLException" + e);
//            e.printStackTrace();
//        }

        this.key = key;
        this.name = nameStr[key];
        this.LV = "LV.5";
        this.HP = 100;
        this.myHP = 50;
        pokemonCreat();
    }

    public void pokemonCreat() {
        skin = SkinGenerator.generateSkin(assetManager);

        //라벨
        nameLabel = new Label(name, myPokemonUI.labelColors[4]);
        nameLabel.setTouchable(Touchable.disabled);
        nameLabel.setAlignment(Align.left);
        nameLabel.setScale(1.5f);

        LVLabel = new Label(LV, myPokemonUI.labelColors[1]);
        LVLabel.setTouchable(Touchable.disabled);
        LVLabel.setAlignment(Align.left);
        LVLabel.setScale(1.2f);

        // 포켓몬 사진
        myPokemon = new Image(new Texture(Gdx.files.internal("pokemon/ball/" + name + ".png")));
        myPokemon.setOrigin(Align.center);
        myPokemon.setScale(0.75f);

        selectedSlot = new Image(new Texture(Gdx.files.internal("pokemon/ball/" + name + "-.png")));
        selectedSlot.setOrigin(Align.center);
        selectedSlot.setVisible(false);
        selectedSlot.setScale(0.75f);

        bar = new Image(skin.getRegion("hpbar_bar"));
        bar.setScale(1.4f);

        barEXP_green = new Image(skin.getRegion("green"));
        barEXP_green.setScale(1.4f);
        barEXP_yellow = new Image(skin.getRegion("yellow"));
        barEXP_yellow.setScale(1.4f);
        barEXP_red = new Image(skin.getRegion("red"));
        barEXP_red.setScale(1.4f);

        float barWidth;

        if (HP > 0)
            barWidth = (float)myHP/ HP;
        else
            barWidth = 0;

        if (barWidth > 0.7){
            barColor = 1;
            barEXP_green.setSize(bar.getWidth()*barWidth - 2,bar.getHeight()/2);
        }
        else if  (barWidth > 0.35){
            barColor = 2;
            barEXP_yellow.setSize(bar.getWidth()*barWidth - 2,bar.getHeight()/2);
        }
        else {
            barColor = 3;
            barEXP_red.setSize(bar.getWidth()*barWidth,bar.getHeight()/2);
        }
    }

    public void pokemonDelete() {
        nameLabel.remove();
        LVLabel.remove();
        myPokemon.remove();
        selectedSlot.remove();
        bar.remove();
        barEXP_green.remove();
        barEXP_yellow.remove();
        barEXP_red.remove();
    }

    public String getNameStr(int num) {
        return nameStr[num];
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getLV() {
        return LV;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLV(String LV) {
        this.LV = LV;
    }
}