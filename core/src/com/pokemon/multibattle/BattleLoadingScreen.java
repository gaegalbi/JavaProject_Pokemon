package com.pokemon.multibattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.pokemon.game.Pokemon;
import com.pokemon.model.DIRECTION;
import com.pokemon.screen.GameScreen;
import com.pokemon.screen.TransitionScreen;
import com.pokemon.transition.FadeInTransition;
import com.pokemon.transition.FadeOutTransition;
import com.pokemon.util.Action;
import com.pokemon.util.GifDecoder;

import static com.pokemon.screen.GameScreen.getAssetManager;
import static com.pokemon.screen.GameScreen.getTweenManager;

public class BattleLoadingScreen implements Screen {
    final Pokemon game;
    private final GameScreen gameScreen;
    Animation<TextureRegion> clock;
    private Texture background;
    private float elapsed;
    private SpriteBatch batch;
    private BattleClient bc;
    private TransitionScreen transitionScreen;
    int a;

    public BattleLoadingScreen(Pokemon game, GameScreen gameScreen) {
        bc = new BattleClient(game);
        this.game = game;
        this.gameScreen = gameScreen;
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("multibattle/loding.png"));
        clock = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("multibattle/loding4.gif").read());
    }

    @Override
    public void show() {

    }
    public void startLoading(){
        game.setScreen(new Loading(game,bc,gameScreen));
        dispose();
    }

    public void select(){
       game.setScreen(new PokemonSelectScreen(game,bc,gameScreen));
        dispose();
    }
    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(background,0,0);
        batch.draw(clock.getKeyFrame(elapsed), 140f, 50f);
        if(bc.getA() == 50){
            select();
        }
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameScreen.getTransitionScreen().startTransition(
                    new FadeOutTransition(0f, Color.BLACK, getTweenManager(), getAssetManager()),
                    new FadeInTransition(0f, Color.BLACK, getTweenManager(), getAssetManager()),
                    new Action() {
                        @Override
                        public void action() {

                        }
                    });
        }

    }
    public void startGameScreen(){
        game.setScreen(new GameScreen(game));
        dispose();
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
