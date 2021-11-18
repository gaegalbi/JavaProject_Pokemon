package com.pokemon.battle.event;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.Texture;
import com.pokemon.battle.BATTLE_PARTY;
import com.pokemon.ui.DialogueBox;
import com.pokemon.ui.StatusBox;

/**
 * @author hydrozoa
 */
public interface BattleEventPlayer {
	
	//public void playBattleAnimation(BattleAnimation animation, BATTLE_PARTY party);
	
	public void setPokemonSprite(Texture region, BATTLE_PARTY party);
	
	public DialogueBox getDialogueBox();
	
	public StatusBox getStatusBox(BATTLE_PARTY party);
	
	//public BattleAnimation getBattleAnimation();
	
	public TweenManager getTweenManager();
	
	public void queueEvent(BattleEvent event);
}
