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
import com.pokemon.inventory.Crafting;
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
    private MovingImageUI craft;
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
    private ImageButton craftButton;
    private Label craftLabel;
    private Table table;
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

   private static final Rectangle EQUIPS_AREA = new Rectangle(Gdx.graphics.getWidth() / 2 - 372 / 2, 135, 150, 120);
   private static final Rectangle INVENTORY_AREA = new Rectangle(Gdx.graphics.getWidth() / 2 - 372 / 2+160, 135, 210, 212);
   private static final Rectangle CRAFT_AREA = new Rectangle(Gdx.graphics.getWidth() / 2 - 372 / 2+160 +210, 135, 124, 182);
    //이벤트 핸들링(드래그)
    private boolean dragging = false;
    //드래그 관련 좌표
    private int prevX, prevY;
    private int ax, ay;
    private boolean itemSelected = false;
    private Item currentItem;
    private Item craftResult;

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

        //TextureAtlas atlas = assetManager.get("texture/textures.atlas");
        //TextureRegion[][] invbuttons92x28 = atlas.findRegion("inv_buttons").split(50, 50);
        TextureRegion inv_but = new TextureRegion(new Texture(Gdx.files.internal("inven/inv_buttons1.png")));
        TextureRegion[][] invbuttons92x28 = inv_but.split(50, 30);

        //skin1 = SkinGenerator.generateSkin_O2(assetManager);

        //인벤 윈도우
      /*  invenWindow = new InvenWindow();
        invenWindow.setSize(400, 300);
        invenWindow.setModal(true);
        invenWindow.setVisible(true);
        invenWindow.setPosition(Gdx.graphics.getWidth() / 2 - invenWindow .getWidth() / 2, Gdx.graphics.getHeight() / 2 - invenWindow .getHeight() / 2);
*/
        //인벤토리 아이템 갯수

        //인벤토리  (w, h Gdx.graphics.getWidth()/2로 변환)
        ui = new MovingImageUI(skin.getRegion("inv_ui"), new Vector2(Gdx.graphics.getWidth() / 2 - 372 / 2, Gdx.graphics.getHeight() / 2 - 212 / 2), new Vector2(100, 100), 225.f, 372, 212);

        craft = new MovingImageUI(skin.getRegion("event_craft"), new Vector2(ui.getX()+ui.getWidth(), ui.getY()), new Vector2(100, 100), 225.f, 124, 182);
        craft.setVisible(false);
        stage.addActor(craft);
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
        invButtons= new ImageButton[2];

        craftButton = new ImageButton(disabled);
        craftButton.setTouchable(Touchable.disabled);
        craftLabel = new Label("제작",skin);
        craftLabel.setTouchable(Touchable.disabled);
        craftLabel.setAlignment(Align.center);
        craftButton.setSize(25,25);
        craftLabel.setSize(25,25);

        invButtonLabels = new Label[2];
        //판매 버튼
        String texts[] = {" 제작"," 버리기"};

        for(int i=0;i<texts.length;i++) {
            invButtons[i] = new ImageButton(disabled);
            invButtons[i].setTouchable(Touchable.disabled);
            invButtonLabels[i] = new Label(texts[i], skin);
            invButtonLabels[i].setSize(46, 14);
            invButtonLabels[i].setTouchable(Touchable.disabled);
            invButtonLabels[i].setAlignment(Align.center);
        }

        //ui 및 슬롯, 버튼들 추가(init)
        stage.addActor(ui);
        for (int i = 0; i < headers.length; i++) stage.addActor(headers[i]);
        for (int i = 0; i < stats.length; i++) stage.addActor(stats[i]);
        stage.addActor(selectedSlot);
        stage.addActor(tooltip);
        for (int i = 0; i < texts.length; i++){
            stage.addActor(invButtons[i]);
            stage.addActor(invButtonLabels[i]);
        }
        stage.addActor(craftButton);
        stage.addActor(craftLabel);
        craftButton.setVisible(false);
        craftLabel.setVisible(false);

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
                item.count.remove();
            }
        }
      /*  for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
            Item item = player.equips.getEquipAt(i);
            if (item != null) {
                item.actor.remove();
            }
        }*/
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
        craftResult = player.crafts.getCraftAt(9);
        if(craftResult!= null){
            addInventoryEvent(craftResult);
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

                //벡터 예전 위치
                prevX = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                prevY = (int) (item.actor.getY() + item.actor.getHeight() / 2);

                item.actor.toFront();
                item.count.toFront();

                selectedSlot.setVisible(false);
                if (!item.getEquipped()) player.inventory.removeItem(item.getIndex());
                else player.equips.removeEquip(item.getType() - 2);

                if (item.getCrafting()) {
                    player.crafts.removeCraft(item.getCIndex());
                }

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
                // 벡터 새 위치
                ax = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                ay = (int) (item.actor.getY() + item.actor.getHeight() / 2);

                //if (!game.player.settings.muteSfx) rm.invselectclick.play(game.player.settings.sfxVolume);
                if(item.getCrafting()){
                    int hc = getCHoveredIndex(ax, ay);
                    int hi = getHoveredIndex(ax, ay);
                    if (hi == -1 && hc == -1) {
                        if(!(CRAFT_AREA.contains(ax,ay) && INVENTORY_AREA.contains(ax,ay) && EQUIPS_AREA.contains(ax,ay))) {
                            item.actor.remove();
                            item.count.remove();
                            db.UPDATE(item.getKey(), -item.getCNT());
                            db.DELETE();
                        }
                        else player.crafts.addCraft(item);
                    }
                    else if(hc==9)player.crafts.addCraft(item);
                    else {
                        if(CRAFT_AREA.contains(ax,ay)){
                            if (!player.crafts.addItemAtIndex(item, hc)) {
                                Item swap = player.crafts.takeItem(hc);
                                //같은 아이템이면 합치기
                                if (swap.getKey() == item.getKey()) {
                                    swap.actor.remove();
                                    swap.count.remove();
                                    item.setCNT(swap.getCNT() + item.getCNT());
                                    item.setCurrentCNT();
                                    player.crafts.addItemAtIndex(item, hc);
                                } else {
                                    player.crafts.addItemAtIndex(swap, item.getCIndex());
                                    player.crafts.addItemAtIndex(item, hc);
                                }
                            }
                        }else if(INVENTORY_AREA.contains(ax,ay)) {
                                item.setCrafting(false);
                                player.crafts.removeCraft(9);
                                System.out.println("9번 삭제");
                                if (!player.inventory.addItemAtIndex(item, hi)) {
                                    Item swap = player.inventory.takeItem(hi);
                                    //같은 아이템이면 합치기
                                    if(swap.getKey()==item.getKey()){
                                        swap.actor.remove();
                                        swap.count.remove();
                                        item.setCNT(swap.getCNT()+item.getCNT());
                                        item.setCurrentCNT();
                                        player.inventory.addItemAtIndex(item, hi);
                                    }
                                    else {
                                        player.inventory.addItemAtIndex(swap, item.getIndex());
                                        player.inventory.addItemAtIndex(item, hi);
                                    }
                                }
                        }
                    }
                }
                else if (item.getEquipped()) {
                    int hi = getHoveredIndex(ax, ay);
                    if (hi == -1)
                        player.equips.addEquip(item);
                    else {
                        if (player.inventory.isFreeSlot(hi)) {
                            player.inventory.addItemAtIndex(item, hi);
                            item.setEquipped(false);
                            player.unequip(item);
                        } else {
                            //장비를 빈 인덱스에 넣음
                            player.inventory.addItemAtIndex(item, player.inventory.getFirstFreeSlotIndex());
                            item.setEquipped(false);
                            player.unequip(item);
                        }
                        db.UPDATE_EQ(TYPE[item.getType()-2],null);
                    }
                } else {
                    // 장비칸으로 이동시
                    if (EQUIPS_AREA.contains(ax, ay)) {
                        System.out.println("장비영역");
                        if (item.getType() >= 2 && item.getType() <= 6) {
                            item.setEquipped(true);
                            player.equip(item);
                            // 같은 부위 장비 스왚
                            if (!player.equips.addEquip(item)) {
                                Item swap = player.equips.removeEquip(item.getType() - 2);
                                swap.setEquipped(false);
                                player.unequip(swap);
                                player.equips.addEquip(item);
                                player.inventory.addItemAtIndex(swap, item.getIndex());
                            }
                            db.UPDATE_EQ(TYPE[item.getType()-2],item.getKey());
                        } else
                            player.inventory.addItemAtIndex(item, item.getIndex());
                    }
                    // 인벤토리 내에서 아이템끼리 이동
                    else if(INVENTORY_AREA.contains(ax,ay)){
                        int hi = getHoveredIndex(ax, ay);
                        if (hi == -1)
                            player.inventory.addItemAtIndex(item, item.getIndex());
                        else {
                            if (!player.inventory.addItemAtIndex(item, hi)) {
                                Item swap = player.inventory.takeItem(hi);
                                //같은 아이템이면 합치기
                                if (swap.getKey() == item.getKey()) {
                                    swap.actor.remove();
                                    swap.count.remove();
                                    item.setCNT(swap.getCNT() + item.getCNT());
                                    item.setCurrentCNT();
                                    player.inventory.addItemAtIndex(item, hi);
                                } else {
                                    player.inventory.addItemAtIndex(swap, item.getIndex());
                                    player.inventory.addItemAtIndex(item, hi);
                                }
                            }
                        }
                    }
                    //제작 영역
                    else if(CRAFT_AREA.contains(ax,ay)){
                        //제작창이 가득 찼으면 인벤토리에 다시 넣음
                        if(player.crafts.isFull())
                            player.inventory.addItemAtIndex(item, item.getIndex());
                        int hc = getCHoveredIndex(ax, ay);
                        //제작창 9번은 결과물만 들어갈 수 있음
                        if (hc == -1 || hc==9)
                            player.inventory.addItemAtIndex(item, item.getIndex());
                        else {
                            if (!player.crafts.addItemAtIndex(item, hc)) {
                                Item swap = player.crafts.takeItem(hc);
                                if (swap.getKey() == item.getKey()) {
                                    swap.actor.remove();
                                    swap.count.remove();
                                    item.setCNT(swap.getCNT() + item.getCNT());
                                    item.setCurrentCNT();
                                    player.crafts.addItemAtIndex(item, hc);
                                } else {
                                    player.inventory.addItemAtIndex(swap, player.inventory.getFirstFreeSlotIndex());
                                    swap.setCrafting(false);
                                    player.crafts.addItemAtIndex(item, hc);
                                }
                            }
                            item.setCrafting(true);
                        }
                    }
                    //모든 영역 밖이면 전부 버리기
                    else {
                        item.actor.remove();
                        item.count.remove();
                        db.UPDATE(item.getKey(), -item.getCNT());
                        db.DELETE();
                    }
                }
            }
        });
        //더블 클릭시 절반
        item.actor.addListener(new ClickListener(){
            public void splitCNT(Item item1, Item item2){
                int tmp = item1.getCNT();
                    item2.setCNT(tmp / 2);
                    item1.setCNT(tmp - item2.getCNT());
                    db.UPDATE_CNT(item2.getKey(),-(item2.getCNT()+item1.getCNT()));
                    item1.setCurrentCNT();
                    item2.setCurrentCNT();
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentItem = item;
                if (getTapCount() == 2 && item.getCNT() >= 2 && !player.inventory.isFull()) {
                    Item splitItem = new Item(currentItem.getKey());
                    splitCNT(currentItem, splitItem);
                    player.inventory.addItemAtIndex(splitItem, player.inventory.getFirstFreeSlotIndex());
                    splitItem.setIndex(player.inventory.getFirstFreeSlotIndex());

                    db.UPDATE_CNT(splitItem.getKey(), splitItem.getCNT());
                    db.UPDATE_CNT(currentItem.getKey(), currentItem.getCNT());

                    //이미지 지우고 전부 새로 불러오기
                    removeInventoryActors();
                    addInventory();

                    splitItem.actor.toFront();
                    splitItem.count.toFront();

                    item.actor.toFront();
                    item.count.toFront();

                    tooltip.toFront();

                    handleStageEvents();
                    handleInvButtonEvents();
                    handleInventoryEvents();

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
                    if (selectedSlot.isVisible()) {
                        unselectItem();
                        toggleInventoryButtons(true);
                    }
                    else {
                        //if (!game.player.settings.muteSfx) rm.invselectclick.play(game.player.settings.sfxVolume);
                        itemSelected = true;
                        currentItem = item;
                        showSelectedSlot(item);
                        toggleInventoryButtons(true);
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
        //인벤토리 밖 클릭시 선택해제
        stage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!(EQUIPS_AREA.contains(x,y) || INVENTORY_AREA.contains(x,y) || CRAFT_AREA.contains(x,y))) {
                    if(craft.isVisible()) {
                        for(int i=0;i<Crafting.NUM_SLOTS;i++){
                            if(player.crafts.getCraftAt(i)!=null) {
                                player.inventory.addItem(player.crafts.getCraftAt(i).getKey(), player.crafts.getCraftAt(i).getCNT());
                                player.crafts.getCraftAt(i).setCrafting(false);
                                player.crafts.getCraftAt(i).actor.remove();
                                player.crafts.getCraftAt(i).count.remove();
                                player.crafts.removeCraft(i);
                                //이미지 지우고 전부 새로 불러오기
                                removeInventoryActors();
                                addInventory();
                                //이벤트 생성
                                handleInventoryEvents();
                            }
                        }
                        craft.setVisible(false);
                        craftButton.setVisible(false);
                        craftLabel.setVisible(false);
                    }
                    unselectItem();
                    toggleInventoryButtons(true);
                }
                return true;
            }
        });
    }
    private void handleInvButtonEvents() {
        invButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // if (!game.player.settings.muteSfx) rm.buttonclick1.play(game.player.settings.sfxVolume);
                craft.setVisible(true);
                craftButton.setVisible(true);
                craftLabel.setVisible(true);
                craftButton.setTouchable(Touchable.enabled);
                craftButton.setStyle(enabled);
            }
        });
        // 버리기
        invButtons[1].addListener(new ClickListener() {
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
                                   // player.addGold(currentItem.getSell());
                                    if (currentItem.getEquipped()) {
                                        player.equips.getEquipAt(currentItem.getType() - 2).actor.remove();
                                        player.equips.removeEquip(currentItem.getType() - 2);
                                        db.UPDATE_EQ(TYPE[currentItem.getType() - 2], null);
                                    } else {
                                        int current = player.inventory.items[currentItem.getIndex()].getCNT() - 1;
                                        if (current > 0) {
                                            player.inventory.items[currentItem.getIndex()].setCNT(current);
                                            player.inventory.items[currentItem.getIndex()].setCurrentCNT();
                                        }
                                        else {
                                            player.inventory.items[currentItem.getIndex()].actor.remove();
                                            player.inventory.items[currentItem.getIndex()].count.remove();
                                            player.inventory.removeItem(currentItem.getIndex());
                                        }
                                    }
                                    db.UPDATE(currentItem.getKey(), -1);
                                    db.DELETE(); //아이템 갯수가 0이하면 삭제
                                    unselectItem();
                                }
                            }
                        }.show(stage).getTitleLabel().setAlignment(Align.center);
                    }
            }
        });
        //제작 결과 생성
        craftButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                   if (craftResult != null) {
                       stage.addActor(craftResult.actor);
                       stage.addActor(craftResult.count);
                       craftResult.setCIndex(9);
                       craftResult.setCrafting(true);
                       craftResult.setCNT(1);
                       craftResult.setCurrentCNT();
                       //핸들 이벤트
                       handleInventoryEvents();
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
    private int getHoveredIndex(int x, int y) {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            int xx = i % NUM_COLS;
            int yy = i / NUM_COLS;
            if (x >= 384 + (xx * SLOT_WIDTH) && x < 384 + (xx * SLOT_WIDTH) + SLOT_WIDTH &&
                    y >= 249 - (yy * SLOT_HEIGHT) && y < 249 - (yy * SLOT_HEIGHT) + SLOT_HEIGHT) {
                return i;
            }
        }
        return -1;
    }
    private int getCHoveredIndex(int x, int y) {
        for (int i = 0; i < Crafting.NUM_SLOTS; i++) {
            int xx = i % (NUM_COLS/2);
            int yy = i / (NUM_COLS/2);
            if(i==9)
                if (x >= 635 && x < 635 + SLOT_WIDTH && y >= 157 && y < 157 + SLOT_HEIGHT)
                    return i;
            if (x >= 603 + (xx * SLOT_WIDTH+2) && x < 603 + (xx * SLOT_WIDTH+2) + SLOT_WIDTH &&
                    y >= 269 - (yy * SLOT_HEIGHT+3) && y < 269 - (yy * SLOT_HEIGHT+2) + SLOT_HEIGHT)
                return i;
        }
        return -1;
    }
    private void updateText() {
        stats[0].setText("LV. " + player.getLV() +"  "+ playerID);
        stats[1].setText("RANK: " + player.getRANK());
        stats[2].setText("EXP: " + player.getEXP() + " / " + player.getMaxEXP());
    }


    private void toggleInventoryButtons(boolean toggle) {
       if (toggle) {
           if(itemSelected) {
               invButtons[0].setTouchable(Touchable.disabled);
               invButtons[0].setStyle(disabled);
               invButtons[1].setTouchable(Touchable.enabled);
               invButtons[1].setStyle(enabled);
           }else {
               invButtons[0].setTouchable(Touchable.enabled);
               invButtons[0].setStyle(enabled);
           }
            //invButtonLabels[0].setText(" " + currentItem.getSell() + "G에\n 판매");
            invButtonLabels[0].setText(" 제작");
            invButtonLabels[1].setText(" 버리기");
        } else {
           for(int i=0;i<invButtons.length;i++) {
               invButtons[i].setTouchable(Touchable.disabled);
               invButtons[i].setStyle(disabled);
           }
            invButtonLabels[0].setText(" 제작");
            invButtonLabels[1].setText(" 버리기");
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
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        //라벨 위치
        headers[0].setPosition(w /3 - w/15 + 14, h/3 - h/18 + 188);
        headers[1].setPosition(w/3 - w/15 +14, h/3 - h/18 + 108);
        headers[2].setPosition(w/3 - w/15 + 165, h/3 - h/18+ 188);
        stats[0].setPosition(w/3 - w/15 + 14, h/3 - h/18 + 175);
        stats[1].setPosition(w/3 - w/15 + 14, h/3 - h/18+ 160);
        stats[2].setPosition(w/3 - w/15 + 14, h/3 - h/18 + 145);

        //인벤 버튼, 라벨
        for(int i=0;i<invButtons.length;i++) {
            invButtons[i].setPosition(w/2 +w/35 + i*55, ui.getY() + 155);
            invButtonLabels[i].setPosition(invButtons[i].getX()-1, invButtons[i].getY() + 8);
            //invButtonLabels[i].toFront();
        }
        //제작 버튼, 라벨
        craftButton.setPosition(ui.getWidth()/2+w/2+16 ,ui.getY()+24);
        craftLabel.setPosition(craftButton.getX(), craftButton.getY());

        //제작 토글
        if(player.crafts.getCraftAt(9)==null) {
            craftResult = player.crafts.craftCheck();
        }

        if (!dragging) {
            for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
                Item item = player.inventory.getItem(i);
                int x = i % NUM_COLS;
                int y = i / NUM_COLS;
                if (item != null) {
                    //아이템 위치
                    item.actor.setSize(26,26);
                    item.actor.setPosition(ui.getX()*2 -ui.getX()/5-2 + (x * 32), ui.getY()*2 - ui.getY()/6 +1 - (y * 32));
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
            for (int i = 0; i < Crafting.NUM_SLOTS; i++) {
                Item item = player.crafts.getCraftAt(i);
                float x = player.crafts.positions[i].x;
                float y = player.crafts.positions[i].y;
                if (player.crafts.getCraftAt(i) != null) {
                    player.crafts.getCraftAt(i).actor.setSize(26,26);
                    player.crafts.getCraftAt(i).actor.setPosition(x, y);
                    player.crafts.getCraftAt(i).count.setPosition(item.actor.getX()+23-item.getCurrentCNT(),item.actor.getY()+2);
                }

            }
        }
        stage.draw();
        stage.act();
    }
}