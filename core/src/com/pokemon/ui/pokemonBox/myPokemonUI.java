package com.pokemon.ui.pokemonBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pokemon.db.db;
import com.pokemon.game.Pokemon;
import com.pokemon.model.Player;
import com.pokemon.screen.GameScreen;
import com.pokemon.ui.AbstractUi;
import com.pokemon.ui.rank.ImageUI;
import com.pokemon.util.SkinGenerator;

import static com.pokemon.ui.LoginUi.playerID;

public class myPokemonUI extends AbstractUi {
    private Stage stage;
    private Pokemon game;
    private GameScreen gameScreen;
    private Player player;
    private AssetManager assetManager;
    private Skin skin;
    private ImageUI ui;
    
    // 포켓몬 이름
    private String[] nameStr = {"가디","강챙이","고라파덕","고오스","거북왕","갸라도스"};
    private Label[] nameLabel;
    private Label[] LVLabel;

    // 포켓몬 이미지
    private Image[] myPokemon;
 //   private Image selectedSlot;
    private Image[] selectedSlot;


    //슬롯 사이즈
    private static final int SLOT_WIDTH = 30;
    private static final int SLOT_HEIGHT = 30;
    //이벤트 핸들링(드래그)
    private boolean dragging = false;
    //드래그 관련 좌표
    private int prevX, prevY;
    private int ax, ay;
    //아이템 선택 유무
    private boolean itemSelected = false;

    public myPokemonUI(GameScreen gameScreen, Pokemon game, Player player) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.player = player;
        stage = new Stage(new ScreenViewport());

        skin = SkinGenerator.generateSkin(assetManager);

        ui = new ImageUI(skin.getRegion("myPokemon_ui"), new Vector2(Gdx.graphics.getWidth() / 2 - 523 / 2, Gdx.graphics.getHeight() / 2 - 461 / 2), 523, 461);

        //폰트 및 폰트 색깔
        Label.LabelStyle[] labelColors = new Label.LabelStyle[]{
                new Label.LabelStyle(skin.getFont("font"), new Color(1, 212 / 255.f, 0, 1)), // yellow
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 0, 255, 1)), // blue
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 1, 60 / 255.f, 1)), // green
                new Label.LabelStyle(skin.getFont("font"), new Color(255 / 255.f, 255 / 255.f, 255 / 255.f, 1)), // white
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 0, 0, 1)), // black
        };

        // 몬스터 이름
        nameLabel = new Label[6];
        for (int i = 0; i < nameLabel.length; i++) {
            nameLabel[i] = new Label(nameStr[i], labelColors[4]);
            nameLabel[i].setTouchable(Touchable.disabled);
            nameLabel[i].setAlignment(Align.left);
        }

        // 레벨
        LVLabel = new Label[6];
        for (int i = 0; i < LVLabel.length; i++) {
            LVLabel[i] = new Label("LV.5", labelColors[1]);
            LVLabel[i].setTouchable(Touchable.disabled);
            LVLabel[i].setAlignment(Align.left);
        }

        // 포켓몬
        myPokemon = new Image[6];
        for (int i = 0; i < 6; i++) {
            myPokemon[i] = new Image(new Texture(Gdx.files.internal("pokemon/ball/" + nameStr[i] + ".png")));
            myPokemon[i].setOrigin(Align.center);
            myPokemon[i].setScale(0.75f);
        }

        //선택 슬롯
        selectedSlot = new Image[6];
        for (int i = 0; i < 6; i++) {
            selectedSlot[i] = new Image(new Texture(Gdx.files.internal("pokemon/ball/" + nameStr[i] + "-.png")));
            selectedSlot[i].setVisible(false);
            selectedSlot[i].setScale(0.75f);
        }

//        selectedSlot = new Image(new Texture(Gdx.files.internal("selectedSlot.png")));
//        selectedSlot.setVisible(false);
//        selectedSlot.setSize(30,30);

        //ui 및 슬롯, 버튼들 추가(init)
        stage.addActor(ui);
       // stage.addActor(selectedSlot);
        for (int i = 0; i < nameLabel.length; i++) stage.addActor(nameLabel[i]);
        for (int i = 0; i < LVLabel.length; i++) stage.addActor(LVLabel[i]);
        for (int i = 0; i < 6; i++) {
            stage.addActor(myPokemon[i]);
            stage.addActor(selectedSlot[i]);
        }

        Gdx.input.setInputProcessor(this.stage);
        handleInventoryEvents();
       // updateText();
    }

    private void updateText() {
        for (int i = 1; i <= 5; i++) {
            if (db.rank_GET_U_ID(i) != null){
                nameLabel[i - 1].setText(db.rank_GET_U_ID(i));
                LVLabel[i - 1].setText(db.rank_GET_U_RANK(db.rank_GET_U_ID(i)));
            }
        }
        nameLabel[5].setText(playerID);
        LVLabel[5].setText(db.rank_GET_U_RANK(playerID));
    }


    private void handleInventoryEvents() {
        for (int i = 0; i < myPokemon.length; i++) {
            addInventoryEvent(myPokemon[i]);
            myPokemon[i].setName(i + "");
        }
    }

    private void addInventoryEvent (final Image item){
        item.clearListeners();
//        item.addListener(new DragListener() {
//            @Override
//            public void dragStart(InputEvent event, float x, float y, int pointer) {
//                dragging = true;
//                unselectItem();
//
//                //벡터 예전 위치
//                prevX = (int) (item.getX() + item.getWidth() / 2);
//                prevY = (int) (item.getY() + item.getHeight() / 2);
//
//                //아이템 이미지, 라벨 앞으로
//                item.toFront();
//
//                selectedSlot.setVisible(false);
//            }
//
//            @Override
//            public void drag(InputEvent event, float x, float y, int pointer) {
//                item.moveBy(x - item.getWidth() / 2, y - item.getHeight() / 2);
//                item.moveBy(x - item.getWidth() / 2, y - item.getHeight() / 2);
//            }
//        });

        //터치
        item.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("x = " + x + " y = " + y);
                //원래 위치
                prevX = (int) (item.getX() + item.getWidth() / 2);
                prevY = (int) (item.getY() + item.getHeight() / 2);
                System.out.println("prevX = " + prevX + " prevY = " + prevY);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //새 위치
                ax = (int) (item.getX() + item.getWidth() / 2);
                ay = (int) (item.getY() + item.getHeight() / 2);

                int uix = (int) item.getX() + 20;
                int uiy = (int) item.getY() + 20;
                int num  = Integer.parseInt(item.getName());

                if (prevX == ax && prevY == ay) {
                    showSelectedSlot(item, uix, uiy, num);
                    unselectItem(num);
                }
            }
        });
    }

    //아이템 선택 해제
    private void unselectItem(int num) {
        itemSelected = false;
        for (int i = 0; i < selectedSlot.length; i++) {
            selectedSlot[i].setVisible(false);
            myPokemon[i].setVisible(true);
        }
        selectedSlot[num].setVisible(true);
    }

    //선택 박스 위치
    private void showSelectedSlot(Image item, int x, int y, int num) {
        selectedSlot[num].setPosition(x, y);
        selectedSlot[num].toFront();
        selectedSlot[num].setVisible(true);
        myPokemon[num].setVisible(false);
    }

    public void update() {
        nameLabel[0].setPosition(ui.getX() + 93, ui.getY() + 420);
        nameLabel[1].setPosition(ui.getX() + 246, ui.getY() + 420);
        nameLabel[2].setPosition(ui.getX() + 401, ui.getY() + 420);
        nameLabel[3].setPosition(ui.getX() + 93, ui.getY() + 230);
        nameLabel[4].setPosition(ui.getX() + 246, ui.getY() + 230);
        nameLabel[5].setPosition(ui.getX() + 401, ui.getY() + 230);

        LVLabel[0].setPosition(ui.getX() + 45, ui.getY() + 380);
        LVLabel[1].setPosition(ui.getX() + 198, ui.getY() + 380);
        LVLabel[2].setPosition(ui.getX() + 353, ui.getY() + 380);
        LVLabel[3].setPosition(ui.getX() + 45, ui.getY() + 190);
        LVLabel[4].setPosition(ui.getX() + 198, ui.getY() + 190);
        LVLabel[5].setPosition(ui.getX() + 353, ui.getY() + 190);

        myPokemon[0].setPosition(ui.getX() + 30, ui.getY() + 245);
        myPokemon[1].setPosition(ui.getX() + 183, ui.getY() + 245);
        myPokemon[2].setPosition(ui.getX() + 336, ui.getY() + 245);
        myPokemon[3].setPosition(ui.getX() + 30, ui.getY() + 55);
        myPokemon[4].setPosition(ui.getX() + 183, ui.getY() + 55);
        myPokemon[5].setPosition(ui.getX() + 336, ui.getY() + 55);

        stage.draw();
        stage.act();
    }
}
