package com.pokemon.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.pokemon.screen.MainMenuScreen;
//import com.pokemon.ui.inventory.InventoryUI;
import com.pokemon.ui.inventory.window;

import java.util.HashMap;

public class Pokemon extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
   // public InventoryUI inventoryUI;
    private HashMap<String, String> accounts = new HashMap<>(); // 임시 로그인 기능용

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		//this.setScreen(new window(this));
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
