package com.pokemon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;

public class SkinGenerator {

	public static TextureRegion inv_ui;
	public static TextureRegion skillList;
	public static TextureRegion inv_but;
	public static TextureRegion selectedSlot;
	public static TextureRegion backgroundM;
	public static Texture backgroundS;
	public static Window.WindowStyle windowStyle;
	public static TextButton.TextButtonStyle textButtonStyle;

	private AssetManager assetManager;

	public static Skin generateSkin(AssetManager assetManager) {
		Skin skin = new Skin();
		assetManager = new AssetManager();
		assetManager.load("inven/inv_buttons1.png",Texture.class);
		assetManager.load("ui/uipack.atlas",TextureAtlas.class);
		assetManager.finishLoading();

		//스킬창 UI
		skillList = new TextureRegion(new Texture(Gdx.files.internal("inven/background_s.png")));
		skin.add("skillList", skillList, TextureRegion.class);

		//인벤 폰트
		BitmapFont font = new BitmapFont(Gdx.files.internal("font/han/gul.fnt"));
		skin.add("font", font);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("font");
		skin.add("default", labelStyle);

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
		skin.add("default",textButtonStyle);
		/* ========================================= */

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

		skin.add("font", font);

		BitmapFont smallFont = new BitmapFont(Gdx.files.internal("font/han/gul.fnt"));
		skin.add("small_letters_font", smallFont);


		LabelStyle labelStyleSmall = new LabelStyle();
		labelStyleSmall.font = skin.getFont("small_letters_font");
		skin.add("smallLabel", labelStyleSmall);

		return skin;
	}

}
