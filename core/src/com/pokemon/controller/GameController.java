package com.pokemon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.pokemon.game.Pokemon;
import com.pokemon.model.Portal;
import com.pokemon.screen.GameScreen;
import com.pokemon.screen.MainMenuScreen;

public class GameController {
    private Pokemon game;

    public GameController(Pokemon game) {
        this.game = game;

    }

    public void update() {
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            game.setScreen(new MainMenuScreen(game));
//        }
    }
}
