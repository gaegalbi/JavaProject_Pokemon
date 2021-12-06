package com.pokemon.ui.rank;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pokemon.db.db;
import com.pokemon.game.Pokemon;
import com.pokemon.model.Player;
import com.pokemon.screen.GameScreen;
import com.pokemon.ui.AbstractUi;
import com.pokemon.util.SkinGenerator;
import static com.pokemon.ui.LoginUi.playerID;

public class rankUI extends AbstractUi {
    private Pokemon game;
    private GameScreen gameScreen;
    private Player player;

    private Stage stage;
    private ImageUI ui;
    private AssetManager assetManager;
    private Skin skin;

    // 유저 이름, 랭크 점수
    private Label[] rankLabel;
    private Label[] IDLabel;
    private Label myRankLabel;


    public rankUI(GameScreen gameScreen, Pokemon game, Player player) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.player = player;
        stage = new Stage(new ScreenViewport());

        skin = SkinGenerator.generateSkin(assetManager);

        // ui 창
        ui = new ImageUI(skin.getRegion("rank_ui"), new Vector2(Gdx.graphics.getWidth() / 2 - 485 / 2, Gdx.graphics.getHeight() / 2 - 428 / 2), 485, 428);

        // 폰트 및 폰트 색깔
        Label.LabelStyle[] labelColors = new Label.LabelStyle[]{
                new Label.LabelStyle(skin.getFont("font"), new Color(1, 212 / 255.f, 0, 1)), // yellow
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 0, 255, 1)), // blue
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 1, 60 / 255.f, 1)), // green
                new Label.LabelStyle(skin.getFont("font"), new Color(255 / 255.f, 255 / 255.f, 255 / 255.f, 1)), // white
        };

        // ID라벨
        IDLabel = new Label[6];
        for (int i = 0; i < IDLabel.length; i++) {
            IDLabel[i] = new Label(" ", labelColors[3]);
            IDLabel[i].setTouchable(Touchable.disabled);
            IDLabel[i].setAlignment(Align.left);
        }

        // 랭크 라벨
        rankLabel = new Label[6];
        for (int i = 0; i < rankLabel.length; i++) {
            rankLabel[i] = new Label(" ", labelColors[0]);
            rankLabel[i].setTouchable(Touchable.disabled);
            rankLabel[i].setAlignment(Align.left);
        }

        myRankLabel = new Label(" ", labelColors[0]);
        myRankLabel.setSize(50, 14);
        myRankLabel.setTouchable(Touchable.disabled);
        myRankLabel.setAlignment(Align.right);

        //ui 및 슬롯, 버튼들 추가(init)
        stage.addActor(ui);
        for (Label label : IDLabel) stage.addActor(label);
        for (Label label : rankLabel) stage.addActor(label);
        stage.addActor(myRankLabel);

        updateText();
        Gdx.input.setInputProcessor(this.stage);
    }

    private void updateText() {
        for (int i = 1; i <= 5; i++) {
            if (db.rank_GET_U_ID(i) != null){
                IDLabel[i - 1].setText(db.rank_GET_U_ID(i));
                rankLabel[i - 1].setText(db.rank_GET_U_RANK(db.rank_GET_U_ID(i)));
            }
        }
        IDLabel[5].setText(playerID);
        rankLabel[5].setText(db.rank_GET_U_RANK(playerID));
        myRankLabel.setText(db.rank_GET_RANKINKG(playerID));
    }

    public void update() {
        float w =  ui.getX();
        float h =  ui.getY();

        for (int i = 0; i < 6; i++) {
            IDLabel[i].setPosition(w + 198, h  + 322 - i * 56);
            rankLabel[i].setPosition(w + 385, h + 322 - i * 56);
        }

        myRankLabel.setPosition(ui.getX() + 82, ui.getY() + 42);

        stage.draw();
        stage.act();
    }
}