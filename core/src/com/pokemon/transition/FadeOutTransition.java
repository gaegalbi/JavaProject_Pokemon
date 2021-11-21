package com.pokemon.transition;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author hydrozoa
 */
public class FadeOutTransition extends Transition {
	
	private Color color;
	private Texture white;

	public FadeOutTransition(float duration, Color color, TweenManager tweenManager, AssetManager assetManager) {
		super(duration, tweenManager, assetManager);
		this.color = color;
		white = assetManager.get("transitions/white.png", Texture.class);
	}

	@Override
	public void render(float delta, SpriteBatch batch) {
		batch.begin();
		batch.setColor(color.r, color.g, color.b, getProgress());
		batch.draw(white, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}
}
