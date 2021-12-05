package com.pokemon.battle.event;

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
