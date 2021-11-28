package com.pokemon.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pokemon.util.SkinGenerator;

import java.sql.SQLException;
import java.sql.Statement;

import static com.pokemon.db.db.con;
import static com.pokemon.db.db.rs;

/**
 * An Item is held by an inventory slot and can be one of:
 * - potion (restores current hp)
 * - equip (several categories of equips)
 * - misc (some other useless thing)
 *
 * @author Ming Li
 */
public class Item {
    // id
    public String name;
    // name displayed on tooltip
    public String labelName;
    // for rendering onto tooltip
    private String info;
    private String property;
    private String effect;
    private int sell;

    // type of item
    /**
     * 0 - 몬스터볼
     * 1 - 재료
     * 2 - 머리
     * 3 - 장신구
     * 4 - 옷
     * 5 - 신발
     * 6 - 무기
     * 7 - 소비
     */
    public final static String[] TYPE= {"U_HEAD", "U_ACC","U_CLOTHES","U_SHOES", "U_WEAPON"};
    private int type;
    private String key;



    //인벤토리 인덱스
    private int index;
    //제작 인덱스
    private int cIndex;
    //아이템 갯수
    private int cnt;
    //장착 유무
    private boolean equipped = false;
    //제작창
    private boolean crafting = false;

   //이미지
    public Image actor;
    public Label count;


    public Item(String key) {
        AssetManager assetManager = new AssetManager();
        //assetManager.load("pokemon/Raichu.png",Texture.class);
        assetManager.load("texture/돌.png",Texture.class);
        assetManager.load("texture/풀.png",Texture.class);
        assetManager.load("texture/몬스터볼.png",Texture.class);
        assetManager.load("texture/수퍼볼.png",Texture.class);
        assetManager.load("texture/나무곡괭이.png",Texture.class);
        assetManager.load("texture/나무괭이.png",Texture.class);
        assetManager.finishLoading();

        Skin skin = SkinGenerator.generateSkin(assetManager);

        String sql = "SELECT ITEM_NAME, ITEM_INFO,ITEM_PROPERTY,ITEM_EFFECT, ITEM_TYPE,SELL FROM ITEM WHERE ITEM_ID ='"+key+"';";
        this.key = key;//ITEM_ID
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                this.name = rs.getString("ITEM_NAME"); //아이템 명
                this.info = rs.getString("ITEM_INFO"); //아이템 설명
                this.property = rs.getString("ITEM_PROPERTY"); //아이템 속성 ex) 나무, 노멀, 등
                this.effect = rs.getString("ITEM_EFFECT"); //아이템 효과 ex)채광, 제작, 공격력 증가등의 스킬기록 // 아이템 속성에 따라 증가시킴
                this.type = rs.getInt("ITEM_TYPE"); //아이템 종류 ex) 장비, 재료, 포켓몬볼
                this.sell = rs.getInt("SELL");
                actor = new Image(assetManager.get("texture/"+name+".png", Texture.class));
                count = new Label("",skin);
            }
        }catch(SQLException e){
            System.out.println("SQLException" + e);
            e.printStackTrace();
        }
    }

    public String getEffect(){
        return effect;
    }
    public String getKey(){return key;}
    public String getProperty(){
        return property;
    }
    public String getInfo(){
        return info;
    }
    public int getType(){
        return type;
    }
    public String getName(){return name;}
    //인벤토리 인덱스
    public int getIndex(){
        return index;
    }
    public void setIndex(int index){
       this.index = index;
    }
    //제작 인덱스
    public int getCIndex(){
        return cIndex;
    }
    public void setCIndex(int cIndex){
        this.cIndex = cIndex;
    }
    public int getSell(){return sell;}
    public boolean getEquipped(){
        return equipped;
    }
    public void setEquipped(boolean equipped){
        this.equipped = equipped;
    }

    public boolean getCrafting(){
        return crafting;
    }
    public void setCrafting(boolean crafting){
        this.crafting = crafting;
    }

    public void setCNT(int cnt){this.cnt = cnt;}
    public void setCurrentCNT(){this.count.setText(getCNT());}
    public int getCurrentCNT(){
        switch (count.getText().length){
            case 1:
                return 1;
            case 2:
                return 8;
            case 3:
                return 14;
            case 4:
                return 20;
            default: return 0;
        }
    }
    public int getCNT(){return cnt;}


    /*
     * Adjusts the stats/attributes of an Item based on a given level
     * Only called once per item's existence
     *
     * @param level
     */
/*    public void adjust(int level) {
        // max hp will be scaled by 5-7 parts of original item stat added on each level
        // dmg is scaled 4-6 parts of original per level
        int mhpSeed = mhp / MathUtils.random(5, 7);
        int dmgSeed = dmg / MathUtils.random(4, 6);
        // set initial enchant cost
        int enchantSeed = MathUtils.random(50, 100);
        int sellSeed = sell / MathUtils.random(10, 15);

        for (int i = 0; i < level - 1; i++) {
            mhp += mhpSeed;
            dmg += dmgSeed;
            sell += sellSeed;
        }
    }*/

    /*public String getFullDesc() {
        String ret = "";
        if (type == 0) {
            // percentage hp potions
            if (hp < 0) ret = desc + "\nRECOVER " + -hp + "% OF HP";
            // exp potions
            else if (exp > 0) ret = desc + "\nGIVES " + exp + "% EXP";
            else ret = desc + "\nHEALS FOR " + hp + " HP";
            ret += "\n\ndouble tap to consume";
        } else if (type == 1) {
            ret = desc;
        } else if (type >= 2 && type <= 9) {
            ret = desc + "\n";
            if (mhp != 0) ret += "+" + mhp + " HP\n";
            if (dmg != 0) ret += "+" + dmg + " DAMAGE\n";
            if (acc != 0) ret += "+" + acc + "% ACCURACY";
            if (bonusEnchantChance != 0) ret += "\n+" + bonusEnchantChance + "% BONUS ENCHANT CHANCE";
        } else if (type == 10) {
            ret = desc + "\n+" + eChance + "% ENCHANT CHANCE";
            ret += "\n\ndrag onto an equip to use";
        }
        // remove newline from end of string if there is one
        ret = ret.trim();
        return ret;
    }*/


/*
    public String getDialogName() {
        String ret = "";
        switch (rarity) {
            case 0:
                ret = "[COMMON] " + name;
                break;
            case 1:
                ret = "[RARE] " + name;
                break;
            case 2:
                ret = "[EPIC] " + name;
                break;
            case 3:
                ret = "[LEGENDARY] " + name;
                break;
        }
        return ret;
    }
*/

}
