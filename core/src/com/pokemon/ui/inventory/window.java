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
import com.pokemon.db.db;
import com.pokemon.game.Pokemon;
import com.pokemon.inventory.Equipment;
import com.pokemon.inventory.Inventory;
import com.pokemon.inventory.Item;
import com.pokemon.model.Player;
import com.pokemon.screen.GameScreen;
import com.pokemon.ui.AbstractUi;
import com.pokemon.util.SkinGenerator;

import static com.pokemon.inventory.Item.TYPE;
import static com.pokemon.ui.LoginUi.playerID;

public class window extends AbstractUi {
    private Stage stage;
    private InvenWindow invenWindow;

    private Pokemon game;
    private GameScreen gameScreen;
    private MovingImageUI ui;
    private AssetManager assetManager;
    private Skin skin;
    //private Skin skin1;
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
    //인벤토리 칸
    private static final int NUM_COLS = 6;

    //슬롯 사이즈
    private static final int SLOT_WIDTH = 30;
    private static final int SLOT_HEIGHT = 30;

   private static final Rectangle EQUIPS_AREA = new Rectangle(Gdx.graphics.getWidth() / 2 - 372 / 2, 130, 160, 120);
   private static final Rectangle INVENTORY_AREA = new Rectangle(Gdx.graphics.getWidth() / 2 - 372 / 2+120, 240, 180, 120);
    //이벤트 핸들링(드래그)
    private boolean dragging = false;
    //드래그 관련 좌표
    private int prevX, prevY;
    private int ax, ay;
    private boolean itemSelected = false;
    private Item currentItem;

    private boolean ended = false;

    static class InvenWindow extends Window{
        static WindowStyle windowStyle = new Window.WindowStyle(new BitmapFont(),Color.BLACK, new TextureRegionDrawable());
        public InvenWindow() {
                super("", windowStyle);
        }
    }

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
        TextureRegion[][] invbuttons92x28 = atlas.findRegion("inv_buttons").split(36, 14);

        //skin1 = SkinGenerator.generateSkin_O2(assetManager);

        //인벤 윈도우
        invenWindow = new InvenWindow();
        invenWindow.setSize(400, 300);
        invenWindow.setModal(true);
        invenWindow.setVisible(true);
        invenWindow.setPosition(Gdx.graphics.getWidth() / 2 - invenWindow .getWidth() / 2, Gdx.graphics.getHeight() / 2 - invenWindow .getHeight() / 2);

        //인벤토리 아이템 갯수

        //인벤토리
        ui = new MovingImageUI(skin.getRegion("inv_ui"), new Vector2(Gdx.graphics.getWidth() / 2 - 372 / 2, Gdx.graphics.getHeight() / 2 - 212 / 2), new Vector2(100, 100), 225.f, 372, 212);

        //선택 슬롯
        selectedSlot = new Image(skin.getRegion("selected_slot"));
        selectedSlot.setVisible(false);
        selectedSlot.setSize(30,30);

        //툴팁
        tooltip = new ItemTooltip(skin);

        // Fonts and Colors
        Label.LabelStyle[] labelColors = new Label.LabelStyle[]{
                new Label.LabelStyle(skin.getFont("font"), new Color(1, 212 / 255.f, 0, 1)), // yellow
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 0, 255, 1)), // white
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 1, 60 / 255.f, 1)), // green
        };
        // create stats
        stats = new Label[3];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = new Label("", labelColors[0]);
            stats[i].setTouchable(Touchable.disabled);
            stats[i].setAlignment(Align.left);
            stats[i].setStyle(labelColors[i]);
        }


        headers = new Label[3];
        for (int i = 0; i < headers.length; i++) {
            headers[i] = new Label(headerStrs[i],skin);
            headers[i].setTouchable(Touchable.disabled);
            headers[i].setAlignment(Align.left);
        }
        //버튼 이미지
        enabled = new ImageButton.ImageButtonStyle();
        enabled.imageUp = new TextureRegionDrawable(invbuttons92x28[0][0]);
        enabled.imageDown = new TextureRegionDrawable(invbuttons92x28[1][0]);
        disabled = new ImageButton.ImageButtonStyle();
        disabled.imageUp = new TextureRegionDrawable(invbuttons92x28[2][0]);
        invButtons = new ImageButton[2];
        invButtonLabels = new Label[2];
        //판매 버튼
        String texts = " 버리기";
        //String texts = " 판매";
        invButtons[0] = new ImageButton(disabled);
        invButtons[0].setSize(90,100);
        invButtons[0].getImage().setFillParent(true);
        invButtons[0].setTouchable(Touchable.disabled);

        invButtonLabels[0] = new Label(texts, skin);
        invButtonLabels[0].setSize(46, 14);
        invButtonLabels[0].setTouchable(Touchable.disabled);
        invButtonLabels[0].setAlignment(Align.center);

        //ui 및 슬롯, 버튼들 추가(init)
        stage.addActor(ui);
        for (int i = 0; i < headers.length; i++) stage.addActor(headers[i]);
        for (int i = 0; i < stats.length; i++) stage.addActor(stats[i]);
        stage.addActor(selectedSlot);
        stage.addActor(tooltip);
        stage.addActor(invButtons[0]);
        stage.addActor(invButtonLabels[0]);

        Gdx.input.setInputProcessor(this.stage);
        //아이템 추가
        addInventory();
        addEquips();

        //이벤트 추가
        handleStageEvents();
        handleInvButtonEvents();
        handleInventoryEvents();
        updateText();
    }

    private void addInventory() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            Item item = player.inventory.getItem(i);
            if (item != null) {
                stage.addActor(item.actor);
                //장비가 아닐때만 라벨 추가
                if(!(item.getType()>=2 && item.getType()<=6)) {
                    stage.addActor(item.count);
                    item.setCurrentCNT();
                }
                item.setIndex(i); //아이템 인덱스값 설정
            }
        }
    }
    private void addEquips() {
        for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
            Item item = player.equips.getEquipAt(i);
            if (item != null) {
                stage.addActor(item.actor);
                item.setEquipped(true);
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
                //if (!game.player.settings.muteSfx) rm.invselectclick.play(game.player.settings.sfxVolume);
                dragging = true;
                tooltip.hide();
                unselectItem();

                // original positions
                prevX = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                prevY = (int) (item.actor.getY() + item.actor.getHeight() / 2);

                item.actor.toFront();
                item.count.toFront();
                selectedSlot.setVisible(false);
                if (!item.getEquipped()) player.inventory.removeItem(item.getIndex());
                else player.equips.removeEquip(item.getType() - 2);
            }
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                item.actor.moveBy(x - item.actor.getWidth() / 2, y - item.actor.getHeight() / 2);
                item.count.moveBy(x - item.actor.getWidth() / 2, y - item.actor.getHeight() / 2);
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                dragging = false;
                selectedSlot.setVisible(false);

                // origin positions
                ax = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                ay = (int) (item.actor.getY() + item.actor.getHeight() / 2);
                System.out.println(ax + "   " + ay);
                //if (!game.player.settings.muteSfx) rm.invselectclick.play(game.player.settings.sfxVolume);
                if (item.getEquipped()) {
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
                            //장비를 빈 인덱스에 넣음
                            hi = 0;
                            while (!player.inventory.isFreeSlot(hi)) hi++;
                            player.inventory.addItemAtIndex(item, hi);
                            item.setEquipped(false);
                            player.unequip(item);
                            updateText();
                        }
                        db.UPDATE_EQ(TYPE[item.getType()-2],null);
                    }
                } else {
                    // 장비칸으로 이동시
                    if (EQUIPS_AREA.contains(ax, ay)) {
                        if (item.getType() >= 2 && item.getType() <= 6) {
                            item.setEquipped(true);
                            player.equip(item);
                            updateText();
                            if (!player.equips.addEquip(item)) {
                                // 같은 종류 아이템 스왚
                                Item swap = player.equips.removeEquip(item.getType() - 2);
                                swap.setEquipped(false);
                                player.unequip(swap);
                                player.equips.addEquip(item);
                                player.inventory.addItemAtIndex(swap, item.getIndex());
                                updateText();
                            }
                            db.UPDATE_EQ(TYPE[item.getType()-2],item.getKey());
                        } else
                            player.inventory.addItemAtIndex(item, item.getIndex());
                    }
                    // 인벤토리 내에서 아이템끼리 이동
                    else {
                        int hi = getHoveredIndex(ax, ay);
                        if (hi == -1)
                            player.inventory.addItemAtIndex(item, item.getIndex());
                        else {
                            if (!player.inventory.addItemAtIndex(item, hi)) {
                                Item swap = player.inventory.takeItem(hi);
                                player.inventory.addItemAtIndex(swap, item.getIndex());
                                player.inventory.addItemAtIndex(item, hi);
                            }
                        }
                    }
                }

            }
        });
        //터치
        item.actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //원래 위치
                prevX = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                prevY = (int) (item.actor.getY() + item.actor.getHeight() / 2);
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //새 위치
                ax = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                ay = (int) (item.actor.getY() + item.actor.getHeight() / 2);

                if (prevX == ax && prevY == ay) {
                    if (selectedSlot.isVisible())
                        unselectItem();
                    else {
                        //if (!game.player.settings.muteSfx) rm.invselectclick.play(game.player.settings.sfxVolume);
                        itemSelected = true;
                        currentItem = item;
                        showSelectedSlot(item);
                        toggleInventoryButtons(true); //판매 활성화
                        tooltip.toFront();
                        //툴팁 위치
                        if (currentItem.getEquipped())
                            tooltip.show(item, ax + 16, ay - tooltip.getHeight());
                        else
                            tooltip.show(item, ax + 16, ay - tooltip.getHeight() * 2);
                    }
                }
            }
        });
    }

    private void handleStageEvents() {
        ui.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (itemSelected)
                    unselectItem();
                return true;
            }
        });
    }
    private void handleInvButtonEvents() {
        // 판매
        invButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    // if (!game.player.settings.muteSfx) rm.buttonclick1.play(game.player.settings.sfxVolume);
                    if (currentItem != null) {
                        new Dialog("", skin) {
                            {
                                //Label l = new Label("정말로 판매하시겠습니까?", skin);
                                Label l = new Label("정말로 버리시겠습니까?", skin);
                                pad(20, 20, 20, 20);
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
                                    if (currentItem.getEquipped()) {
                                        player.equips.getEquipAt(currentItem.getType() - 2).actor.remove();
                                        player.equips.removeEquip(currentItem.getType() - 2);
                                        db.UPDATE_EQ(TYPE[currentItem.getType() - 2], null);
                                    } else {
                                        int current = player.inventory.items[currentItem.getIndex()].getCNT() - 1;
                                        if (current > 0) {
                                            player.inventory.items[currentItem.getIndex()].setCNT(current);
                                            player.inventory.items[currentItem.getIndex()].setCurrentCNT();
                                        } else {
                                            player.inventory.items[currentItem.getIndex()].actor.remove();
                                            player.inventory.items[currentItem.getIndex()].count.remove();
                                            player.inventory.removeItem(currentItem.getIndex());
                                        }
                                    }
                                    db.UPDATE("GOLD", currentItem.getSell());
                                    if(db.COMPARE_CNT(currentItem.getKey(), 1))
                                        db.UPDATE_CNT(currentItem.getKey(), 1);
                                    else
                                        System.out.println("버리려는 아이템 갯수가 너무 많습니다.");

                                    unselectItem();
                                    updateText();
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
        stats[0].setText("LV. " + player.getLV() +"  "+ playerID);
        stats[1].setText("RANK: " + player.getRANK());
        stats[2].setText("EXP: " + player.getEXP() + " / " + player.getMaxEXP());
    }


    private void toggleInventoryButtons(boolean toggle) {
        if (toggle) {
            invButtons[0].setTouchable(Touchable.enabled);
            invButtons[0].setStyle(enabled);
            //invButtonLabels[0].setText(" " + currentItem.getSell() + "G에\n 판매");
            invButtonLabels[0].setText(" 버리기");
        } else {
            invButtons[0].setTouchable(Touchable.disabled);
            invButtons[0].setStyle(disabled);
            //invButtonLabels[0].setText(" 판매");
            invButtonLabels[0].setText(" 버리기");

        }
    }

    //선택 박스 위치
    private void showSelectedSlot(Item item) {
        Vector2 pos = getCoords(item);
        selectedSlot.setPosition(item.actor.getX()-2, item.actor.getY()-2);
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
            ret.set(381 + (x * 32), 246 - (y * 32));
        }
        return ret;
    }


    public void update() {
        //라벨 위치
        headers[0].setPosition(ui.getX() + 14, ui.getY() + 188);
        headers[1].setPosition(ui.getX() + 14, ui.getY() + 108);
        headers[2].setPosition(ui.getX() + 165, ui.getY() + 188);
        stats[0].setPosition(ui.getX() + 14, ui.getY() + 175);
        stats[1].setPosition(ui.getX() + 14, ui.getY() + 160);
        stats[2].setPosition(ui.getX() + 14, ui.getY() + 145);

        invButtons[0].setPosition(ui.getX() + 190 , ui.getY()+80);
        invButtonLabels[0].setPosition(invButtons[0].getX() +45, ui.getY() + 165);

        if (!dragging) {
            for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
                Item item = player.inventory.getItem(i);
                int x = i % NUM_COLS;
                int y = i / NUM_COLS;
                if (item != null) {
                    //아이템 위치
                    item.actor.setSize(26,26);
                    item.actor.setPosition(ui.getX() + 169 + (x * 32), ui.getY() + (113 - (y * 32)));
                    item.count.setPosition(item.actor.getX()+23-item.getCurrentCNT(),item.actor.getY()+2);

                }
            }
            for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
                Item item = player.equips.getEquipAt(i);
                float x = player.equips.positions[i].x;
                float y = player.equips.positions[i].y;
                if (player.equips.getEquipAt(i) != null) {
                    item.actor.setSize(26,26);
                    player.equips.getEquipAt(i).actor.setPosition(ui.getX() + x, ui.getY() + y);
                }
            }
        }
        stage.draw();
        stage.act();
    }
}