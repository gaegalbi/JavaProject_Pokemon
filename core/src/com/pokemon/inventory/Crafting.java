package com.pokemon.inventory;

import com.badlogic.gdx.math.Vector2;
import com.pokemon.db.db;
import com.pokemon.ui.inventory.InventoryUi;

public class Crafting {
    public static final int NUM_SLOTS = 10;
    public Vector2[] positions;
    public Item[] crafts;
    public Crafting() {
        crafts = new Item[NUM_SLOTS];
        positions = new Vector2[NUM_SLOTS];
        for(int i=0;i<NUM_SLOTS-1;i++){
            if(i%3==0)
                positions[i] = new Vector2(603, 269+i/3-i/3*33); //1열
            if(i%3==1)
                positions[i] = new Vector2(635, 269+i/3-i/3*33); //2열
            if(i%3==2)
                positions[i] = new Vector2(667, 269+i/3-i/3*33); //3열
        }
        positions[9] = new Vector2( 635, 157); //결과
    }
    public Item getCraftAt(int index) {
        return crafts[index];
    }
    public boolean addCraft(Item craft) {
        if (crafts[craft.getCIndex()] == null) {
            crafts[craft.getCIndex()] = craft;
            craft.setCrafting(true);
            return true;
        }
        return false;
    }
    public Item removeCraft(int index) {
        Item ret = null;
        if (crafts[index] != null) {
            ret = crafts[index];
            crafts[index] = null;
            return ret;
        }
        return null;
    }

    public int getFirstFreeSlotIndex() {
        for (int i = 0; i < NUM_SLOTS-1; i++) {
            if (crafts[i] == null)
                return i;
        }
        return -1;
    }

    public Item takeItem(int index) {
        Item ret = null;
        if (crafts[index] != null) {
            ret = crafts[index];
            crafts[index] = null;
            return ret;
        }
        return null;
    }
    public boolean isFreeSlot(int index) {
        return crafts[index] == null;
    }
    public boolean isFull() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (crafts[i] == null) return false;
        }
        return true;
    }
    public Item craftCheck(){
        String[] index = new String[NUM_SLOTS-1];
        for(int i = 0;i<NUM_SLOTS-1;i++){
            if(crafts[i]==null)
                index[i]="null";
            else
                index[i]=crafts[i].getKey();
        }
                Item item = db.GET_CRAFT_RESULT(index[0], index[1], index[2], index[3], index[4], index[5], index[6], index[7], index[8]);
                if (item != null) {

                    for (int i = 0; i < NUM_SLOTS - 1; i++) {

                        if (crafts[i] != null) {
                            //DB데이터 연동
                            db.ITEM_UPDATE(crafts[i].getKey(), -1);

                            int current = crafts[i].getCNT() - 1;
                            if (current > 0) {
                                crafts[i].setCNT(current);
                                crafts[i].setCurrentCNT();
                            }
                            //0보다 작으면 객체 삭제
                            else {
                                crafts[i].actor.remove();
                                crafts[i].count.remove();
                                removeCraft(crafts[i].getCIndex());
                            }
                            //DB데이터 중 삭제할 데이터 삭제
                            db.DELETE(); //아이템 갯수가 0이하면 삭제
                        }
                    }
                    return item;
                }


        return null;
    }

    public boolean addItemAtIndex(Item item, int index) {
        if (isFreeSlot(index)) {
            crafts[index] = item;
            item.setCIndex(index);
            return true;
        }
        return false;
    }
}
