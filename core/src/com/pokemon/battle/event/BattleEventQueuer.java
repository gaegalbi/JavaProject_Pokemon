package com.pokemon.battle.event;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * @author hydrozoa
 */
public interface BattleEventQueuer {
	public void queueEvent(BattleEvent event);
	
}
