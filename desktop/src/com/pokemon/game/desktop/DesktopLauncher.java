package com.pokemon.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pokemon.db.db;
import com.pokemon.game.Pokemon;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pokemon";
		config.width = 800;
		config.height = 480;
		config.vSyncEnabled = true;
		config.foregroundFPS = 200;
		db.DBC();
		new LwjglApplication(new Pokemon(), config);
	}
}
