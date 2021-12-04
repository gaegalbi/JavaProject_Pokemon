package com.pokemon.multibattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.pokemon.db.db;
import com.pokemon.game.Pokemon;
import com.pokemon.screen.GameScreen;
import com.pokemon.util.GifDecoder;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static com.pokemon.screen.BattleScreen.playerNum;
import static com.pokemon.ui.LoginUi.playerID;

public class PokemonSelectScreen implements Screen {
    final Pokemon game;
    private final GameScreen gameScreen;
    SpriteBatch batch;
    SelectButton selectButton;
    Animation<TextureRegion> character1;
    Animation<TextureRegion> character2;
    Animation<TextureRegion> character3;
    Animation<TextureRegion> character4;
    Animation<TextureRegion> character5;
    Animation<TextureRegion> character6;
    Animation<TextureRegion> Time;
    Texture SelectBacground;
    Texture oneP;
    Texture twoP;
    private Queue<Integer> q1 = new LinkedList<>();
    private float elapsed;
    private int i;
    private BattleClient battleClient;
    String[] pokemonList = new String[6];
    int[] pp = {0,0,0,0,0,0};
    Stack<String> stackList;
    String a1;
    String a2;
    String a3;
    String temp;
    String temp2;
    public PokemonSelectScreen(Pokemon game, final BattleClient bs, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        batch = new SpriteBatch();
        selectButton = new SelectButton(this,game);
        for(int i=0; i<6; i++){
            pokemonList[i] = db.sP(playerID,i+1);
        }
        oneP = new Texture(Gdx.files.internal("multibattle/001.png"));
        twoP = new Texture(Gdx.files.internal("multibattle/002.png"));
        character1 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/"+pokemonList[0] +".gif").read());
        character2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/"+pokemonList[1] +".gif").read());
        character3 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/"+pokemonList[2] +".gif").read());
        character4 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/"+pokemonList[3] +".gif").read());
        character5 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/"+pokemonList[4] +".gif").read());
        character6 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pokemon/front/"+pokemonList[5] +".gif").read());
        Time = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("multibattle/시간초.gif").read());
        SelectBacground = new Texture(Gdx.files.internal("multibattle/선택배경.png"));
        stackList = new Stack<>();
        this.battleClient = bs;
    }
    public Queue<Integer> getQ1() {
        return q1;
    }
    public void setQ1(int q1) {
        this.q1.add(q1);
    }
    public void removeQ1(){
        q1.remove();
    }
    public void setStackList(String str){
        stackList.add(str);
    }
    @Override
    public void show() {

    }
    public void letsgo(){
        game.setScreen(new Loading(game,battleClient,gameScreen));
        dispose();
    }

    @Override
    public void render(float delta) {
       Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                i =1;
            }
        },9);
       if(selectButton.getC()==1) {
           pp[0] = selectButton.getC();
       }
       if(selectButton.getC()==2){
           pp[3] = selectButton.getC();
       }
       if (selectButton.getC() == 1) {
           if(selectButton.getX() == pp[4] && selectButton.getY() == pp[5]) {
               selectButton.setC(0);
               stackList.pop();
           }else{
               pp[1] = selectButton.getX();
               pp[2] = selectButton.getY();
               if(stackList.size()>2 && stackList.size()%2==1) {
                   temp = stackList.pop();
                   temp2 = stackList.pop();
                   stackList.add(temp);
                   stackList.add(temp2);
               }
           }
       }
       if (selectButton.getC() == 2) {
           if(pp[4] != 0) {
               if (selectButton.getX() == pp[1] && selectButton.getY() == pp[2]) {
                   selectButton.setC(1);
                   stackList.pop();
               } else {
                   pp[4] = selectButton.getX();
                   pp[5] = selectButton.getY();
               }
           }else{
               pp[4] = selectButton.getX();
               pp[5] = selectButton.getY();
           }
       }

        elapsed += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0,0,0,1);
        batch.begin();
        batch.draw(SelectBacground,0,0);
        batch.draw(Time.getKeyFrame(elapsed),340,380,100,100);
        batch.draw(character1.getKeyFrame(elapsed),150,230);
        batch.draw(character2.getKeyFrame(elapsed),350,230);
        batch.draw(character3.getKeyFrame(elapsed),550,230);
        batch.draw(character4.getKeyFrame(elapsed),150,100);
        batch.draw(character5.getKeyFrame(elapsed),350,100);
        batch.draw(character6.getKeyFrame(elapsed),550,100);
        if(pp[0]==1){
            batch.draw(oneP,pp[1],pp[2]);
        }
        if(pp[3]==2){
            batch.draw(twoP,pp[4],pp[5]);
        }
        if(selectButton.getC()==2)
            selectButton.setC(0);
        batch.end();
        selectButton.update();
        if(i==1){
            if(stackList.size()<2){
                int sc[] = new int[2];
                for(int j=0; j<2; j++) {
                    sc[j] = (int)(Math.random()*6)+1;
                    for(int r = 0; r<j; r++){
                        if(sc[j] == sc[r])
                            j--;
                    }
                }
                for(int i=0; i<2; i++) {
                    String k = String.valueOf(sc[i]);
                    String s = playerID + " " + k;
                    stackList.add(s);
                }
            }
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
