package com.pokemon.battle;

import com.pokemon.model.PK;

import java.util.ArrayList;
import java.util.List;

public class Trainer {
	
	private List<PK> team;

	public Trainer(){}

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
