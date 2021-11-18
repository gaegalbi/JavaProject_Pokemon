package com.pokemon.battle.event;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pokemon.battle.Battle;

public abstract class BattleEvent {
	private com.pokemon.battle.event.BattleEventPlayer player;
	
	public void begin(com.pokemon.battle.event.BattleEventPlayer player) {
		this.player = player;
	}
	
	public abstract void update(float delta);
	
	public abstract boolean finished();
	
	protected com.pokemon.battle.event.BattleEventPlayer getPlayer() {
		return player;
	}
}
