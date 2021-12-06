package com.pokemon.game;

import com.badlogic.gdx.Gdx;

public class Sound {
    public static com.badlogic.gdx.audio.Sound hitBlock = Gdx.audio.newSound(Gdx.files.internal("sound/hitBlock.ogg"));
    public static com.badlogic.gdx.audio.Sound getStone = Gdx.audio.newSound(Gdx.files.internal("sound/getStone.ogg"));
    public static com.badlogic.gdx.audio.Sound getWood = Gdx.audio.newSound(Gdx.files.internal("sound/getWood.ogg"));
    public static com.badlogic.gdx.audio.Sound getGrass = Gdx.audio.newSound(Gdx.files.internal("sound/getGrass.ogg"));
}
