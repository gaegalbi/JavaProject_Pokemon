package com.pokemon.multibattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.pokemon.game.Pokemon;
import com.pokemon.util.GifDecoder;

import java.util.Stack;

public class PokemonSelectScreen implements Screen {
    final Pokemon game;
    SpriteBatch batch;
    SelectButton selectButton;
    Animation<TextureRegion> character1;
    Animation<TextureRegion> character2;
    Animation<TextureRegion> character3;
    Animation<TextureRegion> character4;
    Animation<TextureRegion> character5;
    Animation<TextureRegion> character6;
    private float elapsed;
    private int i;
    private BattleClient battleClient;
    String[] pokemonList = {"갸라도스","강챙이","가디","고라파덕","거북왕","ch"};
    Stack<String> stackList;
    String a1;
    String a2;
    String a3;
    public PokemonSelectScreen(Pokemon game,final BattleClient bs) {
        this.game = game;
        batch = new SpriteBatch();
        selectButton = new SelectButton(this,game);
        character1 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("C:\\"+pokemonList[0]+".gif").read());
        character2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("C:\\강챙이.gif").read());
        character3 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("C:\\가디.gif").read());
        character4 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("C:\\고라파덕.gif").read());
        character5 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("C:\\거북왕.gif").read());
        character6 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("C:\\ch.gif").read());

        stackList = new Stack<>();
        this.battleClient = bs;
    }

    public void setStackList(String str){
        stackList.add(str);
    }
    @Override
    public void show() {

    }
    public void letsgo(){
        game.setScreen(new Loading(game));
        dispose();
    }

    @Override
    public void render(float delta) {
       Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                i =1;
            }
        },10);
        elapsed += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0,0,0,1);
        batch.begin();
        batch.draw(character1.getKeyFrame(elapsed),150,300);
        batch.draw(character2.getKeyFrame(elapsed),350,300);
        batch.draw(character3.getKeyFrame(elapsed),550,300);
        batch.draw(character4.getKeyFrame(elapsed),150,150);
        batch.draw(character5.getKeyFrame(elapsed),350,150);
        batch.draw(character6.getKeyFrame(elapsed),550,150);
        batch.end();
        selectButton.update();
        if(i==1){
            a1 = stackList.pop()+" ";
            a2 = stackList.pop();
            a3 = a1 + a2;
            game.setStr(a3);
            System.out.println("갓다");
            battleClient.setSendMessage(a3);
            letsgo();
        }
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
