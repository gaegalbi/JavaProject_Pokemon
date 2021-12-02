package com.pokemon.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.pokemon.util.SkinGenerator;

/*
 * Displays generel stats about a Pokemon during a {@link Battle}.
 * 
 * @author hydrozoa
 */
public class StatusBox extends Table {
	
	private Label text;
	private Label lv;
	private Label hpText;
	private HPBar hpbar;


	protected Table uiContainer;
	
	public StatusBox(Skin skin) {
		super(skin);
		this.setBackground("battleinfobox");

		uiContainer = new Table();
		this.add(uiContainer).pad(0f).expand().fill();

		text = new Label("namenull", skin, "smallLabel");
		uiContainer.add(text).align(Align.left).padTop(0f);

		lv = new Label("lvnull", skin, "smallLabel");
		uiContainer.add(lv).align(Align.right).padTop(0f).row();

		hpbar = new HPBar(skin);
		uiContainer.add(hpbar).spaceTop(0f).expand().fill();

		hpText = new Label("NaN/NaN", skin, "smallLabel");
		uiContainer.row();
		uiContainer.add(hpText).expand().right();
	}
	
	public void setText(String newText) {
		text.setText(newText);
	}
	public void setLV(String newLV) {lv.setText(newLV);}
	public void setHPText(String newHP) {hpText.setText(newHP);}

	public HPBar getHPBar() {
		return hpbar;
	}

	public void setHPText(int hpProgress, int hpTotal) {hpText.setText(hpProgress + "/" + hpTotal);}
}
