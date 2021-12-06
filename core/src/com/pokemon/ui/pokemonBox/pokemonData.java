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
import java.sql.SQLException;
import java.sql.Statement;

import static com.pokemon.db.db.con;
import static com.pokemon.db.db.rs;
import static com.pokemon.ui.LoginUi.playerID;


public class pokemonData {
    public int key;
    public String keyID;
    public String name;
    private int LV;
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

    public pokemonData(int key, String name) {
        this.key = key;

        String sql = "SELECT I.pm_name, P.PM_ID, P.PM_LV, P.PM_HP, P.PM_BATTLE, P.PM_currentHP FROM pm_info AS I, pm AS P WHERE I.PM_ID = P.PM_ID AND P.U_ID = '" + name + "' AND P.PM_BATTLE = " + key + ";";

        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                this.keyID = rs.getString("PM_ID");
                this.name = rs.getString("pm_name");
                this.LV = rs.getInt("PM_LV");
                this.HP = rs.getInt("PM_HP");
                this.myHP  = rs.getInt("PM_currentHP");

                int skill_LV[] = db.GET_SK_LV(name);
                HP += skill_LV[4]+10;

                pokemonCreat();
            }
        }catch(SQLException e){
            System.out.println("SQLException" + e);
            e.printStackTrace();
        }

    }

    public void pokemonCreat() {
        skin = SkinGenerator.generateSkin(assetManager);

        //라벨
        nameLabel = new Label(name, myPokemonUI.labelColors[4]);
        nameLabel.setTouchable(Touchable.disabled);
        nameLabel.setAlignment(Align.left);
        nameLabel.setScale(1.5f);

        LVLabel = new Label("LV : " + LV , myPokemonUI.labelColors[1]);
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

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getLV() {
        return LV;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLV(int LV) {
        this.LV = LV;
    }
}