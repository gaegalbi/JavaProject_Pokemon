package com.pokemon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SkinGenerator {
    public static TextureRegion inv_ui;
    public static TextureRegion skillList;
    public static TextureRegion inv_but;
    public static TextureRegion selectedSlot;
    public static TextureRegion backgroundM;
    public static TextureRegion useitem_ui;
    public static Texture backgroundS;
    public static Window.WindowStyle windowStyle;
    public static TextButton.TextButtonStyle textButtonStyle;

    private AssetManager assetManager;

    public static Skin generateSkin (AssetManager assetManager) {
        Skin skin = new Skin();
        assetManager = new AssetManager();
        assetManager.load("inven/inv_buttons1.png",Texture.class);
        assetManager.load("ui/uipack.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        //스킬창 UI
        skillList = new TextureRegion(new Texture(Gdx.files.internal("inven/background_s.png")));
        skin.add("skillList", skillList, TextureRegion.class);

        //스킬 이미지
        TextureRegion skill1 = new TextureRegion(new Texture(Gdx.files.internal("inven/채광.png")));
        TextureRegion skill2 = new TextureRegion(new Texture(Gdx.files.internal("inven/제작.png")));
        TextureRegion skill3 = new TextureRegion(new Texture(Gdx.files.internal("inven/공격력증가.png")));
        TextureRegion skill4 = new TextureRegion(new Texture(Gdx.files.internal("inven/방어력증가.png")));
        TextureRegion skill5 = new TextureRegion(new Texture(Gdx.files.internal("inven/체력증가.png")));
        TextureRegion skill6 = new TextureRegion(new Texture(Gdx.files.internal("inven/스피드증가.png")));
        skin.add("채광", skill1, TextureRegion.class);
        skin.add("제작", skill2, TextureRegion.class);
        skin.add("공격력 증가", skill3, TextureRegion.class);
        skin.add("방어력 증가", skill4, TextureRegion.class);
        skin.add("체력 증가", skill5, TextureRegion.class);
        skin.add("스피드 증가", skill6, TextureRegion.class);

        //인벤 폰트
        BitmapFont font = new BitmapFont(Gdx.files.internal("font/han/gul.fnt"));
        //font.setColor(Color.BLACK);
        skin.add("font", font);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        //labelStyle.font.setColor(Color.BLACK);
        skin.add("default", labelStyle);

        //useItem UI
        useitem_ui = new TextureRegion(new Texture(Gdx.files.internal("inven/useitem_ui.png")));
        skin.add("useitem_ui", useitem_ui, TextureRegion.class);

        //인벤 UI
        inv_ui = new TextureRegion(new Texture(Gdx.files.internal("inven/inventory_ui.png")));
        skin.add("inv_ui", inv_ui, TextureRegion.class);

        inv_but = new TextureRegion(new Texture(Gdx.files.internal("inven/inv_buttons1.png")));
        skin.add("inv_buttons",inv_but,TextureRegion.class);

        selectedSlot = new TextureRegion(new Texture(Gdx.files.internal("inven/selectedSlot.png")));
        skin.add("selected_slot", selectedSlot, TextureRegion.class);

        backgroundM = new TextureRegion(new Texture(Gdx.files.internal("inven/background_m.png")));
        skin.add("event_craft",backgroundM);

        backgroundS = new Texture(Gdx.files.internal("inven/background.png"));
        windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = skin.getFont("font");
        windowStyle.background = new TextureRegionDrawable(new TextureRegion(backgroundS));
        skin.add("default",windowStyle);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("font");
        //textButtonStyle.font.setColor(Color.BLACK);
        skin.add("default",textButtonStyle);
        /* ========================================= */

        // 랭크 UI
        TextureRegion rank_ui = new TextureRegion(new Texture(Gdx.files.internal("ui/rank.png")));
        skin.add("rank_ui", rank_ui, TextureRegion.class);

        // 포켓몬 UI
        TextureRegion myPokemon_ui = new TextureRegion(new Texture(Gdx.files.internal("ui/my_pokemon.png")));
        skin.add("myPokemon_ui", myPokemon_ui, TextureRegion.class);

        TextureAtlas uiAtlas = assetManager.get("ui/uipack.atlas");

        NinePatch buttonSquareBlue = new NinePatch(uiAtlas.findRegion("dialoguebox"), 10, 10, 5, 5);
        skin.add("dialoguebox", buttonSquareBlue);

        NinePatch optionbox = new NinePatch(uiAtlas.findRegion("optionbox"),6, 6, 6, 6);
        skin.add("optionbox", optionbox);

        NinePatch battleinfobox = new NinePatch(uiAtlas.findRegion("battleinfobox"),14, 14, 5, 8);
        battleinfobox.setPadLeft((int)battleinfobox.getTopHeight());
        skin.add("battleinfobox", battleinfobox);

        skin.add("arrow", uiAtlas.findRegion("arrow"), TextureRegion.class);
        skin.add("hpbar_side", uiAtlas.findRegion("hpbar_side"), TextureRegion.class);
        skin.add("hpbar_bar", uiAtlas.findRegion("hpbar_bar"), TextureRegion.class);
        skin.add("green", uiAtlas.findRegion("green"), TextureRegion.class);
        skin.add("yellow", uiAtlas.findRegion("yellow"), TextureRegion.class);
        skin.add("red", uiAtlas.findRegion("red"), TextureRegion.class);
        skin.add("background_hpbar", uiAtlas.findRegion("background_hpbar"), TextureRegion.class);


        BitmapFont smallFont = new BitmapFont(Gdx.files.internal("font/han/gul.fnt"));
        skin.add("small_letters_font", smallFont);


        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = skin.getFont("font");
        labelStyleSmall.fontColor = Color.BLACK;
        skin.add("smallLabel", labelStyleSmall);

        return skin;
    }
}