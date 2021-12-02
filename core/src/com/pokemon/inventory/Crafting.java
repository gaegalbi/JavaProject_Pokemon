package com.pokemon.inventory;

import com.badlogic.gdx.math.Vector2;

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
        if(crafts[0]!=null &&crafts[1]!=null &&crafts[2]!=null &&crafts[3]!=null &&crafts[4]!=null  &&crafts[5]!=null )
            if ((crafts[0].getKey().equals("ITEM_01")) &&
                    (crafts[1].getKey().equals("ITEM_01")) &&
                    (crafts[2].getKey().equals("ITEM_01")) &&
                    (crafts[3].getKey().equals("ITEM_01")) &&
                    (crafts[4].getKey().equals("ITEM_01")) &&
                    (crafts[5].getKey().equals("ITEM_01"))) {
                crafts[9] = new Item("ITEM_03");;
                return crafts[9];
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
