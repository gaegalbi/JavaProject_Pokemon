package com.pokemon.inventory;

import java.sql.SQLException;
import java.sql.Statement;

import static com.pokemon.db.db.*;
import static com.pokemon.ui.LoginUi.playerID;

/**
 * An Inventory is a collection of Items arranged in a grid
 * This acts mainly as a collection class and functions to implement inventory management
 *
 * @author Ming Li
 */
public class Inventory {

    // inventory dimensions
    public static final int NUM_SLOTS = 24;

    private String info;
    private String name;
    private int cnt;

    public static Item[] items;

   public Inventory(String key) {
       items = new Item[NUM_SLOTS];
        int i = 1;
        while(i!=GET_MAX_INVEN(playerID)+1) {
            String sql = "SELECT ITEM_ID,ITEM_CNT from ( SELECT @ROWNUM := @ROWNUM + 1 AS RN,ITEM_ID,ITEM_CNT FROM INVEN WHERE (@ROWNUM:=0) = 0 AND U_ID ='" + key + "') as b where b.rn=" + i + ";";
            cnt=0;
            try {
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    this.name = rs.getString("ITEM_ID");
                    this.cnt = rs.getInt("ITEM_CNT");
                }
                if(cnt>0){
                    items[i - 1] = new Item(name);
                    if(items[i-1].getType()>=2 && items[i-1].getType()<=6 && cnt>=2){
                        while(cnt!=1){
                            i++;
                            items[i-1] = new Item(name);
                            items[i-1].setCNT(1);
                            cnt--;
                        }
                    }
                    else items[i-1].setCNT(cnt);
                }
            } catch (SQLException e) {
                System.out.println("SQLException" + e);
                e.printStackTrace();
            }
            i++;
        }
    }

    /**
     * Returns the index of the first empty slot in the inventory
     * Returns -1 if there are no free slots
     *
     * @return
     */
    public int getFirstFreeSlotIndex() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (items[i] == null) return i;
        }
        return -1;
    }

    /**
     * Returns the Item in the inventory at a given index
     * but does not remove the item from the inventory
     *
     * @param index
     * @return
     */
    public static Item getItem(int index) {
        return items[index];
    }

    /**
     * Returns whether or not a slot at an index is empty
     *
     * @param index
     * @return
     */
    public boolean isFreeSlot(int index) {
        return items[index] == null;
    }

    //아이템중에 장비 아이템을 제외하고 같은 아이템이 있는지 확인
    public int isSame(String id) {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (items[i] != null)
                if (items[i].getKey().equals(id))
                    return i;
        }
        return -1;
    }

    public void addItem(String key, int cnt) {
        for(int i=0;i<NUM_SLOTS;i++) {
            if (isSame(key) >= 0) {
                if (isSame(key) == i) {
                    items[i].setCNT(items[i].getCNT() + cnt);
                    items[i].setCurrentCNT();
                }
            }
           else {
                i = getFirstFreeSlotIndex();
                if (i != -1) {
                    items[i] = new Item(key);
                    items[i].setCNT(cnt);
                    items[i].setCurrentCNT();
                    items[i].setIndex(i);
                }
            }
        }
    }

    /**
     * Adds an Item at a specific index
     * Returns false if item cannot be added
     *
     * @param item
     * @param index
     * @return
     */
    public boolean addItemAtIndex(Item item, int index) {
        if (isFreeSlot(index)) {
            items[index] = item;
            item.setIndex(index);
            return true;
        }
        return false;
    }

    /**
     * Removes an Item from the inventory at a specific index
     *
     * @param index
     */
    public void removeItem(int index) {
        if (items[index] != null) items[index] = null;
    }

    /**
     * Removes an Item from the inventory and returns the Item
     *
     * @param index
     * @return
     */
    public Item takeItem(int index) {
        Item ret = null;
        if (items[index] != null) {
            ret = items[index];
            items[index] = null;
            return ret;
        }
        return null;
    }

    /**
     * Returns whether or not the inventory is full
     *
     * @return
     */
    public boolean isFull() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (items[i] == null) return false;
        }
        return true;
    }

    /**
     * Clears every item in the inventory
     */
    public void clear() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            removeItem(i);
        }
    }

}
