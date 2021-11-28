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

/**
 * Generates a Skin to slam on the UI. 
 * 
 * @author hydrozoa
 */
public class SkinGenerator {

	private static Skin dialogSkin;
	private AssetManager assetManager;

	public static Skin generateSkin(AssetManager assetManager) {
		Skin skin = new Skin();
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

		BitmapFont font = new BitmapFont(Gdx.files.internal("font/han/gul.fnt"));

		font.getData().setLineHeight(16f);
		skin.add("font", font);

		BitmapFont smallFont = assetManager.get("font/han/gul.fnt", BitmapFont.class);
		skin.add("small_letters_font", smallFont);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("font");
		skin.add("default", labelStyle);
		
		LabelStyle labelStyleSmall = new LabelStyle();
		labelStyleSmall.font = skin.getFont("small_letters_font");
		skin.add("smallLabel", labelStyleSmall);

		return skin;
	}

	public static Skin generateSkin_O(AssetManager assetManager) {
		Skin skin = new Skin();
		assetManager.load("texture/textures.atlas",TextureAtlas.class);
		assetManager.load("texture/texture.atlas",TextureAtlas.class);
		assetManager.load("texture/dialog.atlas",TextureAtlas.class);
		assetManager.load("inven/inv_buttons1.png",Texture.class);

		assetManager.finishLoading();

		TextureAtlas atlas = assetManager.get("texture/textures.atlas");

		BitmapFont font = new BitmapFont(Gdx.files.internal("font/han/gul.fnt"));
		skin.add("font", font);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("font");
		skin.add("default", labelStyle);


		//인벤창
		skin.add("inv_ui", atlas.findRegion("inv_ui"), TextureRegion.class);
		skin.add("selected_slot", atlas.findRegion("selected_slot"), TextureRegion.class);
		//skin.add("inv_buttons",atlas.findRegion("inv_buttons").split(46, 14));

		TextureRegion inv_but = new TextureRegion(new Texture(Gdx.files.internal("inven/inv_buttons1.png")));
		skin.add("inv_buttons",inv_but.split(50,30));

		//이벤트 창
		TextureRegion backgroundM = new TextureRegion(new Texture(Gdx.files.internal("inven/background_m.png")));
		skin.add("event_craft",backgroundM);
		Texture backgroundS = new Texture(Gdx.files.internal("inven/background.png"));

		Window.WindowStyle windowStyle = new Window.WindowStyle();
		windowStyle.titleFont = skin.getFont("font");
		windowStyle.background = new TextureRegionDrawable(new TextureRegion(backgroundS));
		skin.add("default",windowStyle);

		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = skin.getFont("font");
		skin.add("default",textButtonStyle);

		return skin;
	}

}
