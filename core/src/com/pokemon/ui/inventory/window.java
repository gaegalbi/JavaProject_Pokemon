package com.pokemon.ui.inventory;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pokemon.game.Pokemon;
import com.pokemon.inventory.Equipment;
import com.pokemon.inventory.Inventory;
import com.pokemon.inventory.Item;
import com.pokemon.model.Player;
import com.pokemon.screen.GameScreen;
import com.pokemon.ui.AbstractUi;
import com.pokemon.util.SkinGenerator;

import static com.pokemon.ui.LoginUi.playerID;

public class window extends AbstractUi {
    private Stage stage;
    private InventoryRenderer inventoryRenderer;
    private Pokemon game;
    private GameScreen gameScreen;
    private MovingImageUI ui;
    private AssetManager assetManager;
    private Skin skin;
    private Skin skin1;
    private Image selectedSlot;
    //툴팁
    private ItemTooltip tooltip;
    // headers
    private Label[] headers;
    private String[] headerStrs = {"상태창", "장비", "인벤토리"};
    //버튼 스타일
    private ImageButton.ImageButtonStyle enabled;
    private ImageButton.ImageButtonStyle disabled;
    private ImageButton[] invButtons;
    private Label[] invButtonLabels;
    //
    // 0 - hp, 1 - dmg, 2 - acc, 3 - exp, 4 - gold
    private Label[] stats;
    private Player player;
    public boolean inMenu;
    // dimensions to render the inventory at
    private static final int NUM_COLS = 6;

    // constants
    private static final int SLOT_WIDTH = 30;
    private static final int SLOT_HEIGHT = 30;

  /*  private static final Rectangle EQUIPS_AREA = new Rectangle(240, 165, 110, 75);
    private static final Rectangle INVENTORY_AREA = new Rectangle(396, 165, 160, 100);*/
  private static final Rectangle EQUIPS_AREA = new Rectangle(Gdx.graphics.getWidth() / 2 - 372 / 2, 100, 120, 100);
    private static final Rectangle INVENTORY_AREA = new Rectangle(Gdx.graphics.getWidth() / 2 - 372 / 2+120, 240, 180, 120);
    // event handling
    private boolean dragging = false;
    // to differentiate between dragging and clicking
    private int prevX, prevY;
    private boolean itemSelected = false;
    private Item currentItem;

    private boolean ended = false;

    public window(GameScreen gameScreen, Pokemon game, Player player) {

        this.game = game;
        this.gameScreen = gameScreen;
        this.player = player;
        stage = new Stage(new ScreenViewport());


        assetManager = new AssetManager();
        assetManager.load("texture/textures.atlas", TextureAtlas.class);
        assetManager.load("texture/texture.atlas", TextureAtlas.class);
        assetManager.load("texture/dialog.atlas", TextureAtlas.class);

        assetManager.finishLoading();

        skin = SkinGenerator.generateSkin_O(assetManager);

        TextureAtlas atlas = assetManager.get("texture/textures.atlas");
        TextureRegion[][] invbuttons92x28 = atlas.findRegion("inv_buttons").split(46, 14);

        skin1 = SkinGenerator.generateSkin_O2(assetManager);


        inventoryRenderer = new InventoryRenderer();
        inventoryRenderer.setSize(400, 300);
        inventoryRenderer.setModal(true);
        inventoryRenderer.setVisible(true);

        //inventoryRenderer.setMovable(true);
        inventoryRenderer.setPosition(Gdx.graphics.getWidth() / 2 - inventoryRenderer.getWidth() / 2, Gdx.graphics.getHeight() / 2 - inventoryRenderer.getHeight() / 2);


        //stage.addActor(inventoryRenderer);

        //인벤토리
        ui = new MovingImageUI(skin.getRegion("inv_ui"), new Vector2(Gdx.graphics.getWidth() / 2 - 372 / 2, Gdx.graphics.getHeight() / 2 - 212 / 2), new Vector2(100, 100), 225.f, 372, 212);

        //선택된 슬롯
        selectedSlot = new Image(skin.getRegion("selected_slot"));

        selectedSlot.setVisible(false);
        selectedSlot.setSize(29,29);

        //툴팁
        tooltip = new ItemTooltip(skin);
        tooltip.setPosition(90, 15);

        // Fonts and Colors
        Label.LabelStyle[] labelColors = new Label.LabelStyle[]{
                new Label.LabelStyle(game.font, new Color(1, 1, 1, 1)), // white
                new Label.LabelStyle(game.font, new Color(0, 1, 60 / 255.f, 1)), // green
                new Label.LabelStyle(game.font, new Color(1, 212 / 255.f, 0, 1)), // yellow
        };

        // create stats
        stats = new Label[3];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = new Label("", labelColors[0]);
            stats[i].setTouchable(Touchable.disabled);
            stats[i].setAlignment(Align.left);
        }
        stats[0].setStyle(labelColors[0]);
        stats[1].setStyle(labelColors[1]);
        stats[2].setStyle(labelColors[2]);




        headers = new Label[3];
        for (int i = 0; i < headers.length; i++) {
            headers[i] = new Label(headerStrs[i],skin);
           // headers[i].setSize(62, 4);
           // headers[i].setFontScale(0.5f);
            headers[i].setTouchable(Touchable.disabled);
            headers[i].setAlignment(Align.left);
        }

        enabled = new ImageButton.ImageButtonStyle();
        enabled.imageUp = new TextureRegionDrawable(invbuttons92x28[0][0]);
        enabled.imageDown = new TextureRegionDrawable(invbuttons92x28[1][0]);
        disabled = new ImageButton.ImageButtonStyle();
        disabled.imageUp = new TextureRegionDrawable(invbuttons92x28[2][0]);
        invButtons = new ImageButton[2];
        invButtonLabels = new Label[2];
        //판매 버튼
        String texts = "판매";
        invButtons[0] = new ImageButton(disabled);
        invButtons[0].setTouchable(Touchable.disabled);

        invButtonLabels[0] = new Label(texts, skin);
        //invButtonLabels[i] = new Label(texts[i],[0]]);
        //invButtonLabels[0].setFontScale(0.8f);
        invButtonLabels[0].setTouchable(Touchable.disabled);
        //invButtonLabels[i].setSize(46, 14);
        invButtonLabels[0].setAlignment(Align.center);
        //invButtonLabels[i].setAlignment(AdialogSkinlign.center);

        handleStageEvents();
        handleInvButtonEvents();
        handleInventoryEvents();
        updateText();


        stage.addActor(ui);
        for (int i = 0; i < headers.length; i++) stage.addActor(headers[i]);
        for (int i = 0; i < stats.length; i++) stage.addActor(stats[i]);
        stage.addActor(selectedSlot);
        stage.addActor(tooltip);

        stage.addActor(invButtons[0]);
        stage.addActor(invButtonLabels[0]);

        if (!inMenu) {
            // reset the stage position after actions
            stage.addAction(Actions.moveTo(0, 0));
            Gdx.input.setInputProcessor(this.stage);
            //renderHealthBars = true;
        }
        addInventory();
        addEquips();
    }

    /*public void init(boolean inMenu, Stage s) {
        this.inMenu = inMenu;
        this.gameScreen = gameScreen;
        if (inMenu) this.stage = s;

        stage.addActor(ui);
       // stage.addActor(exitButton);
        for (int i = 0; i < headers.length; i++) stage.addActor(headers[i]);
        for (int i = 0; i < stats.length; i++) stage.addActor(stats[i]);
        stage.addActor(selectedSlot);
        stage.addActor(tooltip);

            stage.addActor(invButtons[i]);
            stage.addActor(invButtonLabels[i]);


        if (!inMenu) {
            // reset the stage position after actions
            stage.addAction(Actions.moveTo(0, 0));
            Gdx.input.setInputProcessor(this.stage);
            //renderHealthBars = true;
        }
    }*/

    private void addInventory() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            Item item = player.inventory.getItem(i);
            if (item != null) {
                stage.addActor(item.actor);
            }
        }
    }
    private void addEquips() {
        for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
            Item item = player.equips.getEquipAt(i);
            if (item != null) {
                stage.addActor(item.actor);
            }
        }
    }
    private void removeInventoryActors() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            Item item = player.inventory.getItem(i);
            if (item != null) {
                item.actor.remove();
            }
        }
        for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
            Item item = player.equips.getEquipAt(i);
            if (item != null) {
                item.actor.remove();
            }
        }
    }

    private void handleInventoryEvents() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            final Item item = player.inventory.getItem(i);
            if (item != null) {
                addInventoryEvent(item);
            }
        }
        for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
            final Item equip = player.equips.getEquipAt(i);
            if (equip != null) {
                addInventoryEvent(equip);
            }
        }
    }

    private void addInventoryEvent(final Item item) {
        item.actor.clearListeners();
        item.actor.addListener(new DragListener() {

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {

                // can't allow dragging equips off while in game
                if (inMenu || !item.getEquipped()) {
                    //if (!game.player.settings.muteSfx) rm.invselectclick.play(game.player.settings.sfxVolume);
                    dragging = true;
                    tooltip.hide();
                    unselectItem();

                    // original positions
                    prevX = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                    prevY = (int) (item.actor.getY() + item.actor.getHeight() / 2);
                    System.out.println(prevX + " " +prevY);
                    item.actor.toFront();
                    selectedSlot.setVisible(false);
                    if (!item.getEquipped()) player.inventory.removeItem(item.getIndex());
                    else player.equips.removeEquip(item.getType() - 2);
                }
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                item.actor.moveBy(x - item.actor.getWidth() / 2, y - item.actor.getHeight() / 2);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                dragging = false;

                selectedSlot.setVisible(false);
                // origin positions
                int ax = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                int ay = (int) (item.actor.getY() + item.actor.getHeight() / 2);
                System.out.println(ax + " " +ay);
                //if (!game.player.settings.muteSfx) rm.invselectclick.play(game.player.settings.sfxVolume);

                if (item.getEquipped() && inMenu) {
                    if (INVENTORY_AREA.contains(ax, ay)) {
                        int hi = getHoveredIndex(ax, ay);
                        if (hi == -1)
                            player.equips.addEquip(item);
                        else {
                            if (player.inventory.isFreeSlot(hi)) {
                                player.inventory.addItemAtIndex(item, hi);
                                item.setEquipped(false);
                                player.unequip(item);
                                updateText();
                            } else {
                                player.equips.addEquip(item);
                            }
                        }
                    } else {
                        player.equips.addEquip(item);
                    }
                } else {
                    // dropping into equips slots
                    if (EQUIPS_AREA.contains(ax, ay)) {
                        if (item.getType() >= 2 && item.getType() <= 6 && inMenu) {
                            item.setEquipped(true);
                            // item.equipped = true;
                            player.equip(item);
                            updateText();
                            if (!player.equips.addEquip(item)) {
                                // replace the equip with the item of same type
                                Item swap = player.equips.removeEquip(item.getType() - 2);
                                swap.setEquipped(false);
                                player.unequip(swap);
                                player.equips.addEquip(item);
                                player.inventory.addItemAtIndex(swap, item.getIndex());
                                updateText();
                            }
                        } else {
                            player.inventory.addItemAtIndex(item, item.getIndex());
                        }
                    }
                    // dropping into inventory slots
                    else {
                        int hi = getHoveredIndex(ax, ay);

                        if (hi == -1)
                            player.inventory.addItemAtIndex(item, item.getIndex());
                        else {
                            // if dropped into an occupied slot, swap item positions
                            if (!player.inventory.addItemAtIndex(item, hi)) {
                                Item eq = player.inventory.getItem(hi);
                                // dragging an enchant scroll onto an equip
                                if (eq.getType() >= 2 && eq.getType() <= 6) {
                                    // applyEnchantBonus(eq, item);
                                } else {
                                    Item swap = player.inventory.takeItem(hi);
                                    player.inventory.addItemAtIndex(swap, item.getIndex());
                                    player.inventory.addItemAtIndex(item, hi);
                                }
                            }
                        }
                    }
                }
                //if (inMenu) game.save.save();
            }

        });
        item.actor.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // original positions
                prevX = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                prevY = (int) (item.actor.getY() + item.actor.getHeight() / 2);

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // new positions
                int ax = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                int ay = (int) (item.actor.getY() + item.actor.getHeight() / 2);
                // a true click and not a drag
                if (prevX == ax && prevY == ay) {
                    // item selected
                    if (selectedSlot.isVisible()) {
                        unselectItem();
                    }
                    else {
                        //if (!game.player.settings.muteSfx) rm.invselectclick.play(game.player.settings.sfxVolume);
                        itemSelected = true;
                        currentItem = item;
                        showSelectedSlot(item);
                        if (inMenu) toggleInventoryButtons(true);
                        tooltip.toFront();
                        Vector2 tpos = getCoords(item);
                        // make sure items at the bottom don't get covered by the tooltip
                        if (tpos.y <= 31)
                            tooltip.show(item, tpos.x + 8, tpos.y + tooltip.getHeight() / 2);
                        else
                            tooltip.show(item, tpos.x + 8, tpos.y - tooltip.getHeight());
                    }
                }
            }

        });

        //더블클릭
        item.actor.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getTapCount() == 2) {
                    tooltip.setVisible(false);
                    // consuming potions
                    if (item.getType() == 0) {
                        itemSelected = true;
                        currentItem = item;
                        //consume();
                    }
                    // equip items with double click
                    else if (item.getType() >= 2 && item.getType() <= 6 && inMenu) {
                        unselectItem();
                        selectedSlot.setVisible(false);
                        if (!item.getEquipped()) {
                            item.setEquipped(true);
                            player.equip(item);
                            player.inventory.removeItem(item.getIndex());
                            updateText();
                            if (!player.equips.addEquip(item)) {
                                // replace the equip with the item of same type
                                Item swap = player.equips.removeEquip(item.getType() - 2);
                                swap.setEquipped(false);
                                player.unequip(swap);
                                player.equips.addEquip(item);
                                player.inventory.addItemAtIndex(swap, item.getIndex());
                                updateText();
                            }
                        }
                        // double clicking an equipped item unequips it and places it
                        // in the first open slot if it exists
                        else {
                            player.equips.removeEquip(item.getType() - 2);
                            if (!player.inventory.addItem(item)) {
                                player.equips.addEquip(item);
                            } else {
                                item.setEquipped(false);
                                player.unequip(item);
                            }
                            updateText();
                        }
                        //if (inMenu) game.save.save();
                    }
                }
            }

        });

    }
    private void handleStageEvents() {
        ui.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (itemSelected) {
                    unselectItem();
                }
                return true;
            }
        });
    }
    private void handleInvButtonEvents() {

        // sell
        invButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // if (!game.player.settings.muteSfx) rm.buttonclick1.play(game.player.settings.sfxVolume);
                if (currentItem != null) {
                    new Dialog("Sell", skin1) {
                        {
                            Label l = new Label("Are you sure you want\nto sell " + currentItem.labelName + "?", skin1);
                            l.setFontScale(0.5f);
                            l.setAlignment(Align.center);
                            text(l);
                            getButtonTable().defaults().width(40);
                            getButtonTable().defaults().height(15);
                            button("Yes", "yes");
                            button("No", "no");
                        }

                        @Override
                        protected void result(Object object) {
                            //if (!game.player.settings.muteSfx) rm.buttonclick2.play(game.player.settings.sfxVolume);
                            if (object.equals("yes")) {
                                player.addGold(currentItem.getSell());
                                player.inventory.items[currentItem.getIndex()].actor.remove();
                                player.inventory.removeItem(currentItem.getIndex());
                                unselectItem();
                                updateText();
                                //game.save.save();
                            }
                        }

                    }.show(stage).getTitleLabel().setAlignment(Align.center);
                }
            }
        });
    }

    /* 포션
     * Handles consuming potions

    private void consume() {
        new Dialog("Consume", rm.dialogSkin) {
            {
                Label l = new Label("Heal for " +
                        (currentItem.hp < 0 ? (int) ((-currentItem.hp / 100f) * player.getMaxHp()) : currentItem.hp)
                        + " HP\nusing this potion?", rm.dialogSkin);
                if (currentItem.exp > 0) {
                    l.setText("Gain " + (int) ((currentItem.exp / 100f) * player.getMaxExp()) + " EXP\nfrom this potion?");
                }
                l.setFontScale(0.5f);
                l.setAlignment(Align.center);
                text(l);
                getButtonTable().defaults().width(40);
                getButtonTable().defaults().height(15);
                button("Yes", "yes");
                button("No", "no");
            }

            @Override
            protected void result(Object object) {
                //if (!game.player.settings.muteSfx) rm.buttonclick2.play(game.player.settings.sfxVolume);
                if (object.equals("yes")) {
                    if (currentItem.hp < 0) player.percentagePotion(-currentItem.hp);
                    else if (currentItem.exp > 0) player.addExp((int) ((currentItem.exp / 100f) * player.getMaxExp()));
                    else player.potion(currentItem.hp);
                    player.inventory.items[currentItem.index].actor.remove();
                    player.inventory.removeItem(currentItem.index);
                    unselectItem();
                    updateText();
                    if (inMenu) game.save.save();
                }
            }

        }.show(stage).getTitleLabel().setAlignment(Align.center);
    }
    */


    private void unselectItem() {
        itemSelected = false;
        currentItem = null;
        selectedSlot.setVisible(false);
        toggleInventoryButtons(false);
        tooltip.hide();
    }

    //드래그 위치
    //private static final Rectangle INVENTORY_AREA = new Rectangle(Gdx.graphics.getWidth() / 2 - 372 / 2+120, 240, 180, 120);
    //private static final Rectangle INVENTORY_AREA = new Rectangle(396, 165, 160, 100);*/
    private int getHoveredIndex(int x, int y) {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            int xx = i % NUM_COLS;
            int yy = i / NUM_COLS;
            if (x >= Gdx.graphics.getWidth() / 2 - 372 / 2+180 + (xx * SLOT_WIDTH) && x < Gdx.graphics.getWidth() / 2 - 372 / 2+180 + (xx * SLOT_WIDTH) + SLOT_WIDTH &&
                    y >= 240 - (yy * SLOT_HEIGHT) && y < 240 - (yy * SLOT_HEIGHT) + SLOT_HEIGHT) {
                return i;
            }
        }
        // outside of inventory range
        return -1;
    }
    private void updateText() {
        // update all text

        stats[0].setText("LV. " + player.getLV() +"  "+ playerID);
        //stats[0].setText("HP: " + player.getHp() + "/" + player.getMaxHp());
        //stats[1].setText("DAMAGE: " + player.getMinDamage() + "-" + player.getMaxDamage());
        //stats[2].setText("ACCURACY: " + player.getAccuracy() + "%");
        stats[1].setText("EXP: " + player.getEXP() + " / " + player.getMaxEXP());
        stats[2].setText("GOLD: " + player.getGold() + " G");
    }

    private void toggleInventoryButtons(boolean toggle) {
        if (toggle) {
            if (!currentItem.getEquipped()) {
                invButtons[0].setTouchable(Touchable.enabled);
                if (currentItem.getType() < 2) {
                    invButtons[0].setTouchable(Touchable.disabled);
                    invButtons[0].setStyle(disabled);
                }
                invButtons[0].setStyle(enabled);
                // add enchant cost of item to button
                if (currentItem.getType() >= 2 && currentItem.getType() <= 6)
                   // invButtonLabels[0].setText("ENCHANT FOR\n" + currentItem.enchantCost + " g");
                // add sell value of item to button
                invButtonLabels[1].setText("SELL FOR\n" + currentItem.getSell() + " g");
            }
        } else {
            invButtons[0].setTouchable(Touchable.disabled);
            invButtons[0].setStyle(disabled);
            //invButtonLabels[0].setText("ENCHANT");
            invButtonLabels[0].setText("판매");
        }
    }
    //선택 박스 위치
    private void showSelectedSlot(Item item) {
        Vector2 pos = getCoords(item);
        selectedSlot.setPosition(item.actor.getX()-1, item.actor.getY()-1);
        selectedSlot.toFront();
        selectedSlot.setVisible(true);
    }
    //아이템클릭 효과 위치
    private Vector2 getCoords(Item item) {
        Vector2 ret = new Vector2();
        if (item.getEquipped()) {
            ret.set(7 + (player.equips.positions[item.getType() - 2].x - 2),
                    7 + (player.equips.positions[item.getType() - 2].y - 2));
        }
        else {
            int i = item.getIndex();
            int x = i % NUM_COLS;
            int y = i / NUM_COLS;
            ret.set(381 + (x * 30), 246 - (y * 30));
        }
        return ret;
    }


    public void update() {


        /*
        //if (!inMenu) ui.update(delta);
        if (ended && ui.getX() == 200) {
            // next();
        } else {
            // update all positions
            // exitButton.setPosition(ui.getX() + 181, ui.getY() + 101);

         */
            headers[0].setPosition(ui.getX() + 14, ui.getY() + 188);
            headers[1].setPosition(ui.getX() + 14, ui.getY() + 108);
            headers[2].setPosition(ui.getX() + 165, ui.getY() + 188);
            stats[0].setPosition(ui.getX() + 14, ui.getY() + 175);
            stats[1].setPosition(ui.getX() + 14, ui.getY() + 160);
            stats[2].setPosition(ui.getX() + 14, ui.getY() + 145);

            invButtons[0].setPosition(ui.getX() + 238 , ui.getY() + 150);
            invButtonLabels[0].setPosition(invButtons[0].getX() +13, ui.getY() + 151);


            if (!dragging) {
                // update inventory positions
                for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
                    Item item = player.inventory.getItem(i);
                    int x = i % NUM_COLS;
                    int y = i / NUM_COLS;
                    if (item != null) {
                        //아이템 위치
                        item.actor.setSize(26,26);
                        item.actor.setPosition(ui.getX() + 169 + (x * 32), ui.getY() + (113 - (y * 32)));
                    }
                }
                // update equips positions
                for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
                    float x = player.equips.positions[i].x;
                    float y = player.equips.positions[i].y;
                    if (player.equips.getEquipAt(i) != null) {
                        player.equips.getEquipAt(i).actor.setPosition(ui.getX() + x, ui.getY() + y);
                    }
                }
            }
        stage.draw();
        stage.act();
        //}

    }
}