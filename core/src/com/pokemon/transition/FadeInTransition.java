package com.pokemon.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author hydrozoa
 */
public class FadeInTransition extends Transition {
	
	private Color color;
	private Texture white;

	public FadeInTransition(float duration, Color color, AssetManager assetManager) {
		super(duration, assetManager);
		this.color = color;
		white = assetManager.get("transitions/white.png", Texture.class);
	}

	@Override
	public void render(float delta, SpriteBatch batch) {	
		batch.begin();
		batch.setColor(color.r, color.g, color.b, (1-getProgress()));
		batch.draw(white, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}

}
