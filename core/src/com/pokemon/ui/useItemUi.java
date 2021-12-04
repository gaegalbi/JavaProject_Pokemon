package com.pokemon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pokemon.battle.Battle;
import com.pokemon.controller.BattleScreenController;
import com.pokemon.db.db;
import com.pokemon.game.Pokemon;
import com.pokemon.inventory.Crafting;
import com.pokemon.inventory.Equipment;
import com.pokemon.inventory.Inventory;
import com.pokemon.inventory.Item;
import com.pokemon.model.Player;
import com.pokemon.screen.BattleScreen;
import com.pokemon.ui.AbstractUi;
import com.pokemon.ui.inventory.ImageUi;
import com.pokemon.ui.inventory.ItemTooltip;
import com.pokemon.util.SkinGenerator;

import static com.pokemon.controller.BattleScreenController.STATE.DEACTIVATED;
import static com.pokemon.inventory.Item.TYPE;
import static com.pokemon.ui.LoginUi.playerID;

public class useItemUi extends AbstractUi {
    private Stage stage;
    private Pokemon game;
    private Screen Screen;
    private ImageUi ui;
    private ImageUi craft;
    private AssetManager assetManager;
    private Skin skin;
    private Image selectedSlot;

    //툴팁
    private ItemTooltip tooltip;
    //창 이름
    private Label headers;
    private String headerStrs = "인벤토리";
    //버튼
    private TextureRegion[][] invbuttons92x28;
    //버튼 스타일
    private ImageButton.ImageButtonStyle enabled;
    private ImageButton.ImageButtonStyle disabled;
    private ImageButton useButtons;
    private Label useButtonLabels;
    //플레이어 객체
    private Player player;
    //인벤토리 가로 칸 수
    private static final int NUM_COLS = 6;
    //슬롯 사이즈
    private static final int SLOT_WIDTH = 30;
    private static final int SLOT_HEIGHT = 30;
    //장비창, 인벤창, 제작창 영역
    private static final Rectangle INVENTORY_AREA = new Rectangle(Gdx.graphics.getWidth() / 2 - 110, Gdx.graphics.getHeight() / 2 - 106, 220, 212);
    //이벤트 핸들링(드래그)
    private boolean dragging = false;
    //드래그 관련 좌표
    private int prevX, prevY;
    private int ax, ay;
    //아이템 선택 유무
    private boolean itemSelected = false;
    //현재 선택 아이템 객체
    private Item currentItem;
    private BattleScreenController battleScreenController;
    private Battle battle;

    public useItemUi(BattleScreenController battleScreenController,Battle battle, Screen Screen, Pokemon game, Player player) {
        this.game = game;
        this.Screen = Screen;
        this.player = player;
        this.battle = battle;
        this.battleScreenController = battleScreenController;
        stage = new Stage(new ScreenViewport());

        skin = SkinGenerator.generateSkin(assetManager);

        //ui = 인벤
        ui = new ImageUi(skin.getRegion("useitem_ui"), new Vector2(Gdx.graphics.getWidth() / 2 - 110, Gdx.graphics.getHeight() / 2 - 106), 220, 212);
        //버튼
        invbuttons92x28 = skin.getRegion("inv_buttons").split(50, 30);

        //선택 슬롯
        selectedSlot = new Image(skin.getRegion("selected_slot"));
        selectedSlot.setVisible(false);
        selectedSlot.setSize(30,30);

        //툴팁
        tooltip = new ItemTooltip(skin);

        headers= new Label(headerStrs,skin);
        headers.setTouchable(Touchable.disabled);
        headers.setAlignment(Align.left);

        //버튼 이미지
        enabled = new ImageButton.ImageButtonStyle();
        enabled.imageUp = new TextureRegionDrawable(invbuttons92x28[0][0]);
        enabled.imageDown = new TextureRegionDrawable(invbuttons92x28[1][0]);
        disabled = new ImageButton.ImageButtonStyle();
        disabled.imageUp = new TextureRegionDrawable(invbuttons92x28[2][0]);

        //인벤 버튼 생성
        String text = " 사용";
        useButtons = new ImageButton(disabled);
        useButtons.setTouchable(Touchable.disabled);
        useButtonLabels = new Label(text, skin);
        useButtonLabels.setSize(46, 14);
        useButtonLabels.setTouchable(Touchable.disabled);
        useButtonLabels.setAlignment(Align.center);

        //init
        stage.addActor(ui);
        stage.addActor(headers);

        stage.addActor(selectedSlot);
        stage.addActor(tooltip);

        //사용 버튼 추가
        stage.addActor(useButtons);
        stage.addActor(useButtonLabels);

        //아이템 추가
        addInventory();
        //이벤트 추가
        handleStageEvents();
        handleInvButtonEvents();
        handleInventoryEvents();
        Gdx.input.setInputProcessor(this.stage);
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

                //벡터 예전 위치
                prevX = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                prevY = (int) (item.actor.getY() + item.actor.getHeight() / 2);

                //아이템 이미지, 라벨 앞으로
                item.actor.toFront();
                item.count.toFront();

                selectedSlot.setVisible(false);
                player.inventory.removeItem(item.getIndex());

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
                    // 인벤토리 내에서 아이템끼리 이동
                    int hi = getHoveredIndex(ax, ay);
                    if (hi == -1)
                        player.inventory.addItemAtIndex(item, item.getIndex());
                    else {
                        if (!player.inventory.addItemAtIndex(item, hi)) {
                            Item swap = player.inventory.takeItem(hi);
                            //같은 아이템이면 갯수 합치기
                            if (swap.getKey().equals(item.getKey())) {
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
                //모든 창에 속하지 않는 곳을 클릭하면
                if(!INVENTORY_AREA.contains(x,y) ) {
                    unselectItem();
                    toggleInventoryButtons(true);
                }
                return true;
            }
        });
    }
    private void handleInvButtonEvents() {
        //사용 버튼 이벤트
        useButtons.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    // if (!game.player.settings.muteSfx) rm.buttonclick1.play(game.player.settings.sfxVolume);
                    if (currentItem != null) {
                        new Dialog("", skin) {
                            {
                                Label l = new Label("정말로 사용하시겠습니까?", skin);
                                pad(20, 20, 20, 20);
                                l.setAlignment(Align.center);
                                text(l);
                                getButtonTable().defaults().width(40);
                                getButtonTable().defaults().height(15);
                                button("Yes", "yes");
                                button("No", "no");
                                BattleScreen.useCheck=true;
                            }
                            @Override
                            protected void result(Object object) {
                                //if (!game.player.settings.muteSfx) rm.buttonclick2.play(game.player.settings.sfxVolume);
                                if (object.equals("yes")) {
                                        //현재 선택한 아이템 갯수 -1
                                        int current = player.inventory.items[currentItem.getIndex()].getCNT() - 1;
                                        //남은 갯수가 0보다 크면 갯수 저장
                                        if (current > 0) {
                                            player.inventory.items[currentItem.getIndex()].setCNT(current);
                                            player.inventory.items[currentItem.getIndex()].setCurrentCNT();
                                        }
                                        //0보다 작으면 객체 삭제
                                        else {
                                            player.inventory.items[currentItem.getIndex()].actor.remove();
                                            player.inventory.items[currentItem.getIndex()].count.remove();
                                            player.inventory.removeItem(currentItem.getIndex());
                                        }
                                    battle.useItem(currentItem);
                                    battle.progress(4);
                                    //DB데이터 연동
                                    db.ITEM_UPDATE(currentItem.getKey(), -1);
                                    //DB데이터 중 삭제할 데이터 삭제
                                    db.DELETE(); //아이템 갯수가 0이하면 삭제
                                    //아이템 선택 해제
                                    unselectItem();
                                    battleScreenController.endTurn();
                                    Gdx.input.setInputProcessor(battleScreenController);
                                    stage.dispose();
                                   
                                }
                            }
                        }.show(stage).getTitleLabel().setAlignment(Align.center);
                    }
            }
        });
    }

    //아이템 선택 해제
    private void unselectItem() {
        itemSelected = false;
        currentItem = null;
        selectedSlot.setVisible(false);
        toggleInventoryButtons(false);
        tooltip.hide();
    }
    //마우스 인벤창 위치
    private int getHoveredIndex(int x, int y) {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            int xx = i % NUM_COLS;
            int yy = i / NUM_COLS;
            if (x >= 300 + (xx * SLOT_WIDTH) && x < 300 + (xx * SLOT_WIDTH) + SLOT_WIDTH &&
                    y >= 245 - (yy * SLOT_HEIGHT) && y < 245 - (yy * SLOT_HEIGHT) + SLOT_HEIGHT) {
                return i;
            }
        }
        return -1;
    }
    //인벤 버튼 토글
    private void toggleInventoryButtons(boolean toggle) {
       if (toggle) {
           //몬스터볼과 소비아이템만 사용가능
           if(itemSelected&&(currentItem.getType()==7||currentItem.getType()==0)) {
               useButtons.setTouchable(Touchable.enabled);
               useButtons.setStyle(enabled);
           }else {
               useButtons.setTouchable(Touchable.disabled);
               useButtons.setStyle(disabled);
           }
            useButtonLabels.setText(" 사용");
        } else {
           useButtons.setTouchable(Touchable.disabled);
           useButtons.setStyle(disabled);
           useButtonLabels.setText(" 사용");
        }
    }

    //선택 박스 위치
    private void showSelectedSlot(Item item) {
        Vector2 pos = getCoords(item);
        selectedSlot.setPosition(pos.x, pos.y);
        selectedSlot.toFront();
        selectedSlot.setVisible(true);
    }

    //아이템별 선택 효과 위치
    private Vector2 getCoords(Item item) {
        Vector2 ret = new Vector2();
        int i = item.getIndex();
        int x = i % NUM_COLS;
        int y = i / NUM_COLS;
        ret.set(305 + (x * 32), 245 - (y * 32));
        return ret;
    }

    public void update() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        //라벨 위치
        headers.setPosition(w/3 - w/15 + 90, h/3 - h/18+ 188);

        //사용 버튼, 라벨
        useButtons.setPosition(w/2 -25, ui.getY() + 155);
        useButtonLabels.setPosition(useButtons.getX()-1, useButtons.getY() + 8);

        //아이템 위치, 라벨 업데이트
        if (!dragging) {
            for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
                Item item = player.inventory.getItem(i);
                int x = i % NUM_COLS;
                int y = i / NUM_COLS;
                if (item != null) {
                    //아이템 위치
                    item.actor.setSize(26,26);
                    item.actor.setPosition(ui.getX() +17+ (x * 32), ui.getY()*2 - ui.getY()/6 +1 - (y * 32));
                    item.count.setPosition(item.actor.getX()+23-item.getCurrentCNT(),item.actor.getY()+2);
                }
            }
        }
        stage.draw();
        stage.act();
    }
}