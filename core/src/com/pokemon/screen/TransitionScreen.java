package com.pokemon.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pokemon.game.Pokemon;
import com.pokemon.transition.Transition;
import com.pokemon.util.Action;

/**
 * Used for transitions between screens.
 * 
 * @author hydrozoa 
 */
public class TransitionScreen implements Screen {
	final Pokemon game;

	private GameScreen gameScreen;
	private Transition outTransition;
	private Transition inTransition;
	
	private Action action;

	private SpriteBatch batch;
	private Viewport viewport;
	
	private TRANSITION_STATE state;
	
	private enum TRANSITION_STATE {
		OUT,
		IN
	}
	
	public TransitionScreen(Pokemon game, GameScreen gameScreen) {
		this.game = game;
		this.gameScreen = gameScreen;
		batch = new SpriteBatch();
		viewport = new ScreenViewport();
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void render(float delta) {
		if (state == TRANSITION_STATE.OUT) {
			outTransition.update(delta);
			if (outTransition.isFinished()) {
				action.action();
				state = TRANSITION_STATE.IN;
				return;
			}
		} else if (state == TRANSITION_STATE.IN) {
			inTransition.update(delta);
			if (inTransition.isFinished()) {
				gameScreen.setTransition(false);
				game.setScreen(gameScreen);
			}
		}

		if (state == TRANSITION_STATE.OUT) {
			gameScreen.render(delta);
			
			viewport.apply();
			outTransition.render(delta, batch);
		} else if (state == TRANSITION_STATE.IN) {
			gameScreen.render(delta);
			
			viewport.apply();
			inTransition.render(delta, batch);
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		gameScreen.resize(width, height);
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void show() {
		
	}
	
	public void startTransition(Transition out, Transition in, Action action) {
		this.outTransition = out;
		this.inTransition = in;
		this.action = action;
		this.state = TRANSITION_STATE.OUT;
		gameScreen.setTransition(true);
		game.setScreen(this);
	}
}
