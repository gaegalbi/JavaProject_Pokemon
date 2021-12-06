package com.pokemon.battle;

import com.pokemon.db.db;
import com.pokemon.model.PK;
import com.pokemon.model.Player;

import java.util.ArrayList;
import java.util.List;

import static com.pokemon.ui.LoginUi.playerID;

public class Trainer {
	
	private List<PK> team;

	public Trainer(Player player,String id){
		team = new ArrayList<PK>();
		String[] key;
		//이거 i 최대값을 db의 배틀넘 최대값 받아오기
		for(int i=1;i<=db.PM_COUNT();i++) {
			key = new String[]{id, String.valueOf(i)};
			PK pokemon = new PK(player,key);
			if(pokemon!=null)
				team.add(new PK(player, key));
			else
				break;
		}
	}

	public Trainer(PK pokemon) {
		team = new ArrayList<PK>();
		team.add(pokemon);
	}

	public Trainer(PK pokemon,PK pokemon2) {
		team = new ArrayList<PK>();
		team.add(pokemon);
		team.add(pokemon2);
	}

	public boolean addPokemon(PK pokemon) {
		if (team.size() >= 6) {
			return false;
		} else {
			team.add(pokemon);
			return true;
		}
	}

	public boolean addPokemon2(PK pokemon) {
		if (team.size() >= 3) {
			return false;
		} else {
			team.add(pokemon);
			return true;
		}
	}
	
	public PK getPokemon(int index) {
		return team.get(index);
	}
	
	public int getTeamSize() {
		return team.size();
	}
}
