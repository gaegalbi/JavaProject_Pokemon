package com.pokemon.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ScreenUtils;
import com.pokemon.controller.PlayerController;
import com.pokemon.model.Player;
import com.pokemon.screen.BattleScreen;
import com.pokemon.screen.GameScreen;
import com.pokemon.screen.MainMenuScreen;

import java.util.HashMap;

public class Pokemon extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	private HashMap<String, String> accounts = new HashMap<>(); // 임시 로그인 기능용
	private String str;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
		//this.setScreen(new BattleScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	// 임시 로그인 기능용
	public void createAccount(String id,String password) {
		accounts.put(id, password);
	}

	// 임시 로그인 기능용
	public boolean loginValidate(String id,String password) {
		try {
			if (accounts.get(id).equals(password)) {
				return true;
			}
		} catch (NullPointerException ignored) {}

		return false;
	}
}
