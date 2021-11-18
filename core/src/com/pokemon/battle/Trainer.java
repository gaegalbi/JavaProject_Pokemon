package com.pokemon.battle;

import com.pokemon.model.PK;

import java.util.ArrayList;
import java.util.List;

public class Trainer {
	
	private List<PK> team;
	
	public Trainer(PK pokemon) {
		team = new ArrayList<PK>();
		team.add(pokemon);
	}
	
	public boolean addPokemon(PK pokemon) {
		if (team.size() >= 6) {
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
