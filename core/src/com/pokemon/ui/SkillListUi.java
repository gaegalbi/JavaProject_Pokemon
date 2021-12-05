package com.pokemon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
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
import com.pokemon.ui.inventory.ImageUi;
import com.pokemon.util.SkinGenerator;

import static com.pokemon.ui.LoginUi.playerID;

public class SkillListUi extends AbstractUi{
    private Stage stage;
    private Pokemon game;
    private Screen gameScreen;
    private AssetManager assetManager;
    private Skin skin;
    private Player player;
    private ImageUi skillList;
    private Label headers;
    private Label[] skillName;
    private Label[] skillLV;
    private Label[] skillEXP_C;
    private Label[] skillEXP_M;
    private Label[] dash;
    private Image[] skillImage;
    private Image[] bar;
    private Image[] barEXP;



    public SkillListUi(Screen gameScreen, Pokemon game, Player player) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.player = player;
        stage = new Stage(new ScreenViewport());

        skin = SkinGenerator.generateSkin(assetManager);

        skillList = new ImageUi(skin.getRegion("skillList"),new Vector2(Gdx.graphics.getWidth() / 2-100, Gdx.graphics.getHeight() / 2-195),200,390);
        //폰트 및 폰트 색깔
        Label.LabelStyle[] labelColors = new Label.LabelStyle[]{
                new Label.LabelStyle(skin.getFont("font"), new Color(1, 212 / 255.f, 0, 1)), // yellow
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 0, 255, 1)), // white
                new Label.LabelStyle(skin.getFont("font"), new Color(0, 1, 60 / 255.f, 1)), // green
        };

        headers = new Label("스킬창",skin);
        headers.setTouchable(Touchable.disabled);
        headers.setAlignment(Align.left);

        skillName = new Label[6];
        skillLV = new Label[6];
        skillEXP_C = new Label[6];
        skillEXP_M= new Label[6];
        skillImage = new Image[6];
        bar = new Image[6];
        barEXP = new Image[6];
        int[] currentEXP = db.GET_SK_EXP(playerID);
        float barWidth;
        dash = new Label[6];
        //스킬 목록
        for(int i=0;i<player.skill.length;i++){
            skillName[i] = new Label(""+player.getSkillName(i),labelColors[0]);
            skillImage[i] = new Image(skin.getRegion(player.getSkillName(i)+""));
            skillImage[i].setSize(30,30);
            skillLV[i] = new Label("LV."+player.getSkillLV(i),labelColors[0]);
            skillEXP_C[i] = new Label(""+player.getSkillEXP(i),labelColors[0]);
            dash[i]=new Label("/",labelColors[0]);
            skillEXP_M[i] = new Label(""+ db.GET_SK_NEED_EXP(player.getSkillLV(i)),labelColors[0]);
            bar[i] = new Image(skin.getRegion("hpbar_bar"));
            barEXP[i] = new Image(skin.getRegion("green"));
            if(currentEXP[i]>0)
                barWidth = (float)db.GET_SK_NEED_EXP(player.getSkillLV(i))/ currentEXP[i];
            else
                barWidth = 0;
            barEXP[i].setSize(bar[i].getWidth()/barWidth,bar[i].getHeight()/2);
        }


        //스킬 상세 라벨 추가
        for(int i=0;i<player.skill.length;i++) {
            stage.addActor(skillName[i]);
            stage.addActor(skillLV[i]);
            stage.addActor(skillEXP_C[i]);
            stage.addActor(dash[i]);
            stage.addActor(skillEXP_M[i]);
            stage.addActor(skillImage[i]);
            stage.addActor(bar[i]);
            stage.addActor(barEXP[i]);
        }
        //스킬창 이미지 추가
        stage.addActor(skillList);
        //"스킬창" 추가
        stage.addActor(headers);

        Gdx.input.setInputProcessor(this.stage);
    }

    public void update() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        headers.setPosition(w/2-85,h/2+158);

        for(int i=0;i<player.skill.length;i++) {
            skillImage[i].setPosition(headers.getX(), headers.getY()-50-i*50);
            skillName[i].setPosition(headers.getX()+50, headers.getY()-30-i*50);
            skillLV[i].setPosition(headers.getX()+75 + (skillName[i].getText().length*10), headers.getY()-30-i*50);
            skillEXP_C[i].setPosition(headers.getX() + 110+(3-skillEXP_C[i].getText().length)*2,headers.getY()-45-i*50);
            dash[i].setPosition(headers.getX() + 113+(skillEXP_M[i].getX() -skillEXP_C[i].getX())/2,headers.getY()-45-i*50);
            skillEXP_M[i].setPosition(headers.getX() + 125+(skillEXP_C.length-1)*2, headers.getY()-45-i*50);
            bar[i].setPosition(headers.getX() + 50, headers.getY()-45-i*50);
            barEXP[i].setPosition(headers.getX() + 50, headers.getY()-43-i*50);

            skillName[i].toFront();
            skillLV[i].toFront();
            skillEXP_C[i].toFront();
            skillEXP_M[i].toFront();
            skillImage[i].toFront();
            bar[i].toFront();
            barEXP[i].toFront();
            dash[i].toFront();
        }

        stage.draw();
        stage.act();
    }
}
