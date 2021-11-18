
package com.pokemon.battle;

import com.badlogic.gdx.math.MathUtils;
import com.pokemon.model.PK;
import com.pokemon.db.db;

public class BattleMechanics {
	
	private String message = "";

	private double critical(PK user, PK target) {
		String tType = target.getType();
		switch (user.getType()){
			case "물":
				if(tType.equals("물"))
					return 0.8;
				else if(tType.equals("불"))
					return 2;
				else if(tType.equals("전기"))
					return 1;
				else if(tType.equals("풀"))
					return 0.8;
				else
					return 1;
			case "불":
				if(tType.equals("불"))
					return 0.8;
				else if(tType.equals("풀"))
					return 2;
				else if(tType.equals("전기"))
					return 1;
				else if(tType.equals("물"))
					return 0.8;
				else
					return 1;
			case "풀":
				if(tType.equals("풀"))
					return 0.8;
				else if(tType.equals("전기"))
					return 2;
				else if(tType.equals("물"))
					return 1;
				else if(tType.equals("불"))
					return 0.8;
				else
					return 1;
			case "전기":
				if(tType.equals("전기"))
					return 0.8;
				else if(tType.equals("물"))
					return 2;
				else if(tType.equals("불"))
					return 1;
				else if(tType.equals("풀"))
					return 0.8;
				else
					return 1;
			default:
				return 1; //노말은 1
			//물 => 불 => 풀 => 전기  / 노말은 1고정
		}
	}

	public boolean goesFirst(PK player, PK opponent) {
		if (player.getStat()[3] > opponent.getStat()[3]) {
			return true;
		} else if (opponent.getStat()[3] > player.getStat()[3]) {
			return false;
		} else {
			return MathUtils.randomBoolean();
		}
	}

	public int calculateDamage(PK user,int SK_NUM, PK target) {
		message = "";
		float attack = user.getStat()[0]; //공격력
		float defence = user.getStat()[1]; //방어력
		
		double critical = critical(user, target);
		
		int level = user.getLV();
		float base = (int)(db.GET_PM_DA(user.getSkill()[SK_NUM]) * user.getStat()[0]);

		if (critical==2)
			message = "A critical hit!";

		int damage = (int) ((  (2f*level+10f)/250f   *   (float)attack/defence   * base + 2   ) * critical);
		return damage;
	}
	
	public boolean hasMessage() {
		return !message.isEmpty();
	}
	
	public String getMessage() {
		return message;
	}
}

