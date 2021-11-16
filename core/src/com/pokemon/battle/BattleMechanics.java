/*
package com.pokemon.battle;

import com.badlogic.gdx.math.MathUtils;
import com.pokemon.model.PK;

public class BattleMechanics {
	
	private String message = "";

	public boolean goesFirst(PK player, PK opponent) {
		if (player.getStat()[3] > opponent.getStat()[3]) {
			return true;
		} else if (opponent.getStat()[3] > player.getStat()[3]) {
			return false;
		} else {
			return MathUtils.randomBoolean();
		}
	}

	public int calculateDamage(Move move, PK user, PK target) {
		message = "";
		
		float attack = 0f;
		if (move.getCategory() == MOVE_CATEGORY.PHYSICAL) {
			attack = user.getStat(STAT.ATTACK);
		} else {
			attack = user.getStat(STAT.SPECIAL_ATTACK);
		}
		
		float defence = 0f;
		if (move.getCategory() == MOVE_CATEGORY.PHYSICAL) {
			defence = target.getStat(STAT.DEFENCE);
		} else {
			defence = target.getStat(STAT.SPECIAL_DEFENCE);
		}
		
		boolean isCritical = criticalHit(move, user, target);
		
		int level = user.getLevel();
		float base = move.getPower();
		float modifier = MathUtils.random(0.85f, 1.00f);
		if (isCritical) {
			modifier = modifier * 2f;
			message = "A critical hit!";
		}
		
		int damage = (int) ((  (2f*level+10f)/250f   *   (float)attack/defence   * base + 2   ) * modifier);
		
		return damage;
	}
	
	public boolean hasMessage() {
		return !message.isEmpty();
	}
	
	public String getMessage() {
		return message;
	}
}
*/
