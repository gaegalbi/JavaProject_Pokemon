package com.pokemon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

		/*//불러온거
		TextureAtlas atlas = assetManager.get("texture/textures.atlas");
		TextureAtlas dialog = assetManager.get("texture/dialog.atlas");*/



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
		//인벤토리
		skin.add("inven", "inven/inven.jpg", TextureRegion.class);
		skin.add("exit_up", "inven/exit.jpg", TextureRegion.class);
		skin.add("exit_down", "inven/exit1.jpg", TextureRegion.class);


		//dialog

		/*
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/pkmnrsi.ttf"));
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/han/NanumBarunGothic.ttf"));
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/han/NanumSquareRoundEB.ttf"));
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/han/gulim.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 13;
		parameter.color = new Color(96f/255f, 96f/255f, 96f/255f, 1f);
		parameter.shadowColor = new Color(208f/255f, 208f/255f, 200f/255f, 1f);
		parameter.shadowOffsetX = 1;
		parameter.shadowOffsetY = 1;
		parameter.characters = "!  \"  #  $  %  &  '  (  )  *  +  ,  -  .  /  0  1  2  3  4  5  6  7  8  9  :  ;  <  =  >  ?  @  A  B  C  D  E  F  G  H  I  J  K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z  [  \\  ]  ^  _  `  a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z  {  |  }  ~  \u2190  \u2191  \u2192  \u2193  \u2640  \u2642";
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		*/

		BitmapFont font = new BitmapFont(Gdx.files.internal("font/han/gul.fnt"));

		font.getData().setLineHeight(16f);
		skin.add("font", font);
		
		//BitmapFont smallFont = assetManager.get("font/small_letters_font.fnt", BitmapFont.class);
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

		/*
		atlas = assetManager.get("texture/texture.atlas", TextureAtlas.class);
		Skin skin = new Skin(atlas);
		skin.add("grass", atlas.findRegion("grass"), TextureRegion.class);
		skin.add("stone", atlas.findRegion("stone"), TextureRegion.class);
		*/
		assetManager.load("texture/textures.atlas",TextureAtlas.class);
		assetManager.load("texture/texture.atlas",TextureAtlas.class);
		assetManager.load("texture/dialog.atlas",TextureAtlas.class);

		//assetManager.load("inven/inven.jpg", TextureRegion.class);
		assetManager.finishLoading();
		TextureAtlas atlas = assetManager.get("texture/textures.atlas");

		BitmapFont font = new BitmapFont(Gdx.files.internal("font/han/gul.fnt"));
		skin.add("font", font);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("font");
		skin.add("default", labelStyle);


		skin.add("inv_ui", atlas.findRegion("inv_ui"), TextureRegion.class);
		skin.add("selected_slot", atlas.findRegion("selected_slot"), TextureRegion.class);


		skin.add("inv_buttons",atlas.findRegion("inv_buttons").split(46, 14));
		//skin.add("grass", atlas.findRegion("grass"), TextureRegion.class);
		//skin.add("stone", atlas.findRegion("stone"), TextureRegion.class);
		return skin;
	}

	public static Skin generateSkin_O2(AssetManager assetManager) {

		JsonReader jsonReader = new JsonReader();

		assetManager.load("texture/dialog.atlas", TextureAtlas.class);
		assetManager.finishLoading();

		dialogSkin = new Skin(assetManager.get("texture/dialog.atlas", TextureAtlas.class));
		//dialogSkin.add("default-font", pixel10);
		//dialogSkin.load(Gdx.files.internal("texture/dialog.json"));

		return dialogSkin;
	}

}
