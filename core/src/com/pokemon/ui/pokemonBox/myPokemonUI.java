package com.pokemon.ui.pokemonBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.pokemon.game.Pokemon;
import com.pokemon.model.Player;
import com.pokemon.screen.GameScreen;
import com.pokemon.ui.AbstractUi;
import com.pokemon.ui.rank.ImageUI;
import com.pokemon.util.SkinGenerator;

public class myPokemonUI extends AbstractUi {
    private Stage stage;
    private Pokemon game;
    private GameScreen gameScreen;
    private Player player;
    private AssetManager assetManager;
    private Skin skin;
    private ImageUI ui;

    //버튼
    private TextureRegion[][] invbuttons92x28;

    //버튼 스타일
    private ImageButton.ImageButtonStyle enabled;
    private ImageButton.ImageButtonStyle disabled;
    private ImageButton invButtons;
    private Label invButtonLabels;


    //이벤트 핸들링(드래그)
    private boolean dragging = false;
    //드래그 관련 좌표
    private int prevX, prevY;
    private int ax, ay;
    //아이템 선택 유무
    private boolean itemSelected = false;
    //현재 선택 아이템 객체
    private Image currentItem;
    //현재 선택 아이템 이름
    private int selectNum;

    public static Label.LabelStyle[] labelColors;

    private pokemonData[] pokemon;

    public myPokemonUI(GameScreen gameScreen, Pokemon game, Player player) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.player = player;
        stage = new Stage(new ScreenViewport());

        skin = SkinGenerator.generateSkin(assetManager);

        ui = new ImageUI(skin.getRegion("myPokemon_ui"), new Vector2(Gdx.graphics.getWidth() / 2 - 523 / 2, Gdx.graphics.getHeight() / 2 - 461 / 2), 523, 461);

        //버튼
        invbuttons92x28 = skin.getRegion("inv_buttons").split(50, 30);

        //폰트 및 폰트 색깔
        labelColors = new Label.LabelStyle[]{
                new Label.LabelStyle(skin.getFont("font"), new Color(1, 212 / 255.f, 0, 1)), // yellow
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 0, 255, 1)), // blue
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 1, 60 / 255.f, 1)), // green
                new Label.LabelStyle(skin.getFont("font"), new Color(255 / 255.f, 255 / 255.f, 255 / 255.f, 1)), // white
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 0, 0, 1)), // black
        };

        pokemon = new pokemonData[6];

        //버튼 이미지
        enabled = new ImageButton.ImageButtonStyle();
        enabled.imageUp = new TextureRegionDrawable(invbuttons92x28[0][0]);
        enabled.imageDown = new TextureRegionDrawable(invbuttons92x28[1][0]);
        disabled = new ImageButton.ImageButtonStyle();
        disabled.imageUp = new TextureRegionDrawable(invbuttons92x28[2][0]);

        // 버튼
        invButtons = new ImageButton(disabled);
        invButtons.setTouchable(Touchable.disabled);
        invButtonLabels = new Label("방출", skin);
        invButtonLabels.setSize(55, 20);
        invButtonLabels.setTouchable(Touchable.disabled);
        invButtonLabels.setAlignment(Align.center);

        //ui 및 슬롯, 버튼들 추가(init)
        stage.addActor(ui);
        stage.addActor(invButtons);
        stage.addActor(invButtonLabels);

        Gdx.input.setInputProcessor(this.stage);
        addPokemon();
        handleInventoryEvents();
        handleInvButtonEvents();
    }

    private void addPokemon() {
        for (int i = 0; i < 6; i++) {
            pokemon[i] = new pokemonData(i);
            if (pokemon[i] != null) {
                stage.addActor(pokemon[i].nameLabel);
                stage.addActor(pokemon[i].LVLabel);
                stage.addActor(pokemon[i].myPokemon);
                stage.addActor(pokemon[i].selectedSlot);
                stage.addActor(pokemon[i].bar);

                if (pokemon[i].barColor == 1)
                    stage.addActor(pokemon[i].barEXP_green);
                if (pokemon[i].barColor == 2)
                    stage.addActor(pokemon[i].barEXP_yellow);
                if (pokemon[i].barColor == 3)
                    stage.addActor(pokemon[i].barEXP_red);
            }
        }
    }

    private void handleInventoryEvents() {
        for (int i = 0; i < 6; i++) {
            addInventoryEvent(pokemon[i].myPokemon);
            pokemon[i].myPokemon.setName(i + "");
        }
    }

    private void handleInvButtonEvents() {
        // 버리기 버튼 이벤트
        invButtons.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentItem != null) {
                    new Dialog("", skin) {
                        {
                            Label l = new Label("정말로 방출하시겠습니까?", skin);
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
                            if (object.equals("yes")) {
                                unselectItem();
                                updatePokemon(selectNum);
                            }
                        }
                    }.show(stage).getTitleLabel().setAlignment(Align.center);
                }
            }
        });
    }

    //인벤 버튼 토글
    private void updatePokemon(int selectNum) {
        pokemonData temp = pokemon[selectNum];

        int i;
        for (i = selectNum; i < 5; i++) {
            pokemon[i] = pokemon[i+1];
        }
        pokemon[i] = temp;
        pokemon[i].pokemonDelete();
        pokemon[i] = null;
    }

    private void addInventoryEvent (final Image item){
        item.clearListeners();
        //드래그
        item.addListener(new DragListener() {
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                dragging = true;
                unselectItem();

                //벡터 예전 위치
                prevX = (int) (item.getX() + item.getWidth() / 2);
                prevY = (int) (item.getY() + item.getHeight() / 2);

                //포켓몬 이미지
                selectNum = getCHoveredIndex(prevX,prevY);
                pokemon[selectNum].selectedSlot.toFront();

                //포켓몬 선택으로 변경
                pokemon[selectNum].selectedSlot.setVisible(true);
                item.setVisible(false);
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                pokemon[selectNum].selectedSlot.moveBy(x -item.getWidth() / 2, y - item.getHeight() / 2);
                item.moveBy( x -item.getWidth() / 2, y - item.getHeight() / 2);
              //  System.out.println("얘를 가짐" + pokemon[selectNum].getName());
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                dragging = false;
                pokemon[selectNum].selectedSlot.setVisible(false);
                item.setVisible(true);

                // 벡터 새 위치
                ax = (int) (item.getX() + item.getWidth() / 2);
                ay = (int) (item.getY() + item.getHeight() / 2);

                int h = getCHoveredIndex(ax,ay);
           //     System.out.println(h);


                if (h != -1){
                    pokemonData swap = pokemon[h];
                    pokemon[h] = pokemon[selectNum];
                    pokemon[selectNum] = swap;
                }
//                for(int i=0;i<6;i++){
//                    System.out.println(i + "번째 "+pokemon[i].getName());
//                }
            }
        });

        //터치
        item.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //원래 위치
                prevX = (int) (item.getX() + item.getWidth() / 2);
                prevY = (int) (item.getY() + item.getHeight() / 2);

                selectNum = getCHoveredIndex(prevX,prevY);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //새 위치
                ax = (int) (item.getX() + item.getWidth() / 2);
                ay = (int) (item.getY() + item.getHeight() / 2);


                if (prevX == ax && prevY == ay) {
                    itemSelected = true;
                    currentItem = item;
                    toggleInventoryButtons(true);
                    selectItem(selectNum);
                    showSelectedSlot(selectNum);
                }
//                for(int i=0;i<6;i++){
//                    System.out.println(i + "번째 "+pokemon[i].getName());
//                }
            }
        });
    }

    //마우스 포켓몬 위치
    private int getCHoveredIndex(int x, int y) {
        if (y >= 270 && y <= 440){
            if (x >= 185 && x <= 315)
                return 0;
            if (x >= 335 && x <= 465)
                return 1;
            if (x >= 485 && x <= 625)
                return 2;
        }
        else if (y >= 80 && y <= 250){
            if (x >= 185 && x <= 315)
                return 3;
            if (x >= 335 && x <= 465)
                return 4;
            if (x >= 485 && x <= 625)
                return 5;
        }
        return -1;
    }

    //포켓몬 선택 모두 해제
    private void unselectItem() {
        itemSelected = false;
        currentItem = null;
        for (int i = 0; i < 6 && pokemon[i] != null; i++) {
            pokemon[i].selectedSlot.setVisible(false);
            pokemon[i].myPokemon.setVisible(true);
        }
        toggleInventoryButtons(false);
    }

    //포켓몬 선택
    private void selectItem(int num) {
        itemSelected = false;
        for (int i = 0; i < 6 && pokemon[i] != null; i++) {
            pokemon[i].selectedSlot.setVisible(false);
            pokemon[i].myPokemon.setVisible(true);
        }
        pokemon[num].selectedSlot.setVisible(true);
    }

    //선택 포켓몬 위치
    private void showSelectedSlot(int num) {
        pokemon[num].selectedSlot.toFront();
        pokemon[num].selectedSlot.setVisible(true);
        pokemon[num].myPokemon.setVisible(false);
    }

    //방출 버튼 토글
    private void toggleInventoryButtons(boolean toggle) {
        if (toggle) {
            //아이템 선택시 제작 비활성화, 버리기 활성화
            if(itemSelected) {
                invButtons.setTouchable(Touchable.enabled);
                invButtons.setStyle(enabled);
            }
            invButtonLabels.setText("방출");
        } else {
            invButtons.setTouchable(Touchable.disabled);
            invButtons.setStyle(disabled);
            invButtonLabels.setText("방출");
        }
    }


    public void update() {
        float w =  ui.getX();
        float h =  ui.getY();

        if(!dragging){
            for (int i = 0; i < 6; i++) {
                if (pokemon[i] != null){
                    if (i < 3){
                        pokemon[i].nameLabel.setPosition(w + i * 153 + 97, h + 412);
                        pokemon[i].LVLabel.setPosition(w + i * 153 + 45, h + 380);
                        pokemon[i].myPokemon.setPosition(w + i * 153 + 30, h + 245);
                        pokemon[i].selectedSlot.setPosition(w + i * 153 + 30, h + 245);
                        pokemon[i].bar.setPosition(w + i * 153 + 95, h + 394);
                        if (pokemon[i].barColor == 1)
                            pokemon[i].barEXP_green.setPosition(w + i * 153 + 96, h + 397);
                        if (pokemon[i].barColor == 2)
                            pokemon[i].barEXP_yellow.setPosition(w + i * 153 + 96, h + 397);
                        if (pokemon[i].barColor == 3)
                            pokemon[i].barEXP_red.setPosition(w + i * 153 + 96, h + 397);
                    }
                    else{
                        pokemon[i].nameLabel.setPosition(w + (i - 3) * 153 + 97, h + 222);
                        pokemon[i].LVLabel.setPosition(w + (i - 3) * 153 + 45, h + 190);
                        pokemon[i].myPokemon.setPosition(w + (i - 3) * 153 + 30, h + 55);
                        pokemon[i].selectedSlot.setPosition(w + (i - 3) * 153 + 30, h + 55);
                        pokemon[i].bar.setPosition(w + (i - 3) * 153 + 95, h + 205);
                        if (pokemon[i].barColor == 1)
                            pokemon[i].barEXP_green.setPosition(w + (i - 3) * 153 + 96, h + 208);
                        if (pokemon[i].barColor == 2)
                            pokemon[i].barEXP_yellow.setPosition(w + (i - 3) * 153 + 96, h + 208);
                        if (pokemon[i].barColor == 3)
                            pokemon[i].barEXP_red.setPosition(w + (i - 3) * 153 + 96, h + 208);
                    }
                }
            }
        }

        invButtons.setPosition(w + 400, h + 20);
        invButtonLabels.setPosition(invButtons.getX() - 3, invButtons.getY() + 6);

        stage.draw();
        stage.act();
    }
}
