package com.pokemon.inventory;

import com.badlogic.gdx.math.Vector2;

import java.sql.SQLException;
import java.sql.Statement;

import static com.pokemon.db.db.con;
import static com.pokemon.db.db.rs;
import static com.pokemon.ui.LoginUi.playerID;

/**
 * The collection of equips that the player has equipped
 *
 * @author Ming Li
 */
public class Equipment {
    /**
     * 2 - 머리  => 0
     * 3 - 장신구
     * 4 - 옷
     * 5 - 신발
     * 6 - 무기 => 4
     */

    public static final int NUM_SLOTS = 5;
    private String[] name = new String[NUM_SLOTS];
    public Item[] equips;
    // stores the positions of equip slots relative to inventory
    public Vector2[] positions;

    public Equipment(String key) {
        equips = new Item[NUM_SLOTS];
        positions = new Vector2[NUM_SLOTS];
           String sql = "SELECT U_HEAD,U_ACC,U_CLOTHES,U_SHOES,U_WEAPON FROM USER WHERE U_ID ='"+playerID+"';";
            try {
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    this.name[0] = rs.getString("U_HEAD");
                    this.name[1] = rs.getString("U_ACC");
                    this.name[2] = rs.getString("U_CLOTHES");
                    this.name[3] = rs.getString("U_SHOES");
                    this.name[4] = rs.getString("U_WEAPON");
                }
                if(name[0]!=null)equips[0]=new Item(name[0]);
                if(name[1]!=null)equips[1]=new Item(name[1]);
                if(name[2]!=null) equips[2]=new Item(name[2]);
                if(name[3]!=null) equips[3]=new Item(name[3]);
                if(name[4]!=null) equips[4]=new Item(name[4]);
            } catch (SQLException e) {
                System.out.println("SQLException" + e);
                e.printStackTrace();
            }
        positions[0] = new Vector2(81, 81); //머리
        positions[1] = new Vector2(18, 81); //장신구
        positions[2] = new Vector2(81, 50); // 몸
        positions[3] = new Vector2(81, 18); //신발
        positions[4] = new Vector2(50, 50); //무기
    }

    /**
     * The player equips an item and it gets placed into the correct slot
     * Returns false if cannot be equipped
     *
     * @param equip
     * @return
     */
    public boolean addEquip(Item equip) {
        if (equips[equip.getType() - 2] == null) {
            equips[equip.getType() - 2] = equip;
            equip.setEquipped(true);
            return true;
        }
        return false;
    }

    /**
     * Removes an equip at an index and returns the Item
     *
     * @param index
     * @return
     */
    public Item removeEquip(int index) {
        Item ret = null;
        if (equips[index] != null) {
            ret = equips[index];
            equips[index] = null;
            return ret;
        }
        return null;
    }

    /**
     * Returns the equip from a specific index but does not remove it
     *
     * @param index
     * @return
     */
    public Item getEquipAt(int index) {
        return equips[index];
    }

}
