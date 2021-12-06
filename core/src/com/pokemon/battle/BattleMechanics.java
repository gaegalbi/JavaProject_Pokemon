
package com.pokemon.battle;

import com.badlogic.gdx.math.MathUtils;
import com.pokemon.db.db;
import com.pokemon.model.PK;

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
					return 5;
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
				return 0.8; //노말은 0.8
			//물 => 불 => 풀 => 전기  / 노말은 1고정
		}
	}

	public boolean goesFirst(PK player, PK opponent) {
		if (player.getChStat()[3] > opponent.getChStat()[3]) {
			return true;
		} else if (opponent.getChStat()[3] > player.getChStat()[3]) {
			return false;
		} else {
			return MathUtils.randomBoolean();
		}
	}

	public int calculateDamage( PK user, int SK_NUM, PK target) {
		float attack = user.getChStat()[0];//공격력
		float defence = target.getChStat()[1]; //방어력

		float speed = user.getChStat()[3] - target.getChStat()[3];

		if(speed <=0)
			speed = 1;
		
		double critical = critical(user, target);

		int level = user.getLV()-target.getLV();//레벨차

		if(level<=0)
			level=0;

		float skillDamage = 0;

		String name = db.GET_PM_SK_NAME(user.getSkill()[SK_NUM]);
		if(name.equals("마지막수단")){
			int i = user.getSK_CNT()[SK_NUM] - user.getCurrent_SK_CNT()[SK_NUM];
			skillDamage = (float) (db.GET_PM_DA(user.getSkill()[SK_NUM])*(0.3*i));
		}else
			skillDamage = (float) db.GET_PM_DA(user.getSkill()[SK_NUM]);

		float base = skillDamage * user.getChStat()[0];

		if(base>0) {
			int damage = (int) (((2f * level + 10f) / 250f * (float) (attack+speed/2) / defence * base + 2) * critical);
			return damage;
		}
		return 0;
	}

	public int calculateSelfDamage(PK user,int SK_NUM,int damage) {
		String name = db.GET_PM_SK_NAME(user.getSkill()[SK_NUM]);
		if(name.equals("이판사판태클"))
			return (int)(damage*0.4);
		if(name.equals("볼트태클"))
			return (int)(damage*0.4);
		if(name.equals("와일드볼트"))
			return (int)(damage*0.2);
		if(name.equals("기가임팩트"))
			return (int)(damage*0.4);
		if(name.equals("파괴광선"))
			return (int)(damage*0.4);
		if(name.equals("하이드로캐논"))
			return (int)(damage*0.4);
		if(name.equals("불사르기"))
			return (int)(damage*0.4);
		if(name.equals("블러스트번"))
			return (int)(damage*0.4);
		if(name.equals("대폭발"))
			return (int)(damage*0.6);
		return 0;
	}

	public int calculateHeal(PK user,int SK_NUM,int damage) {
		String name = db.GET_PM_SK_NAME(user.getSkill()[SK_NUM]);
		if(name.equals("HP회복"))
			return (int)(user.getChStat()[2]*0.5);
		if(name.equals("광합성"))
			return (int)(user.getChStat()[2]*0.8);
		if(name.equals("메가드레인"))
			return (int)(damage*0.5);
		if(name.equals("기가드레인"))
			return (int)(damage*0.8);
		if(name.equals("충전"))
			return (int)(user.getChStat()[2]*0.5);

		return 0;
	}

}

