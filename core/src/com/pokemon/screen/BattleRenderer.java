package com.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pokemon.battle.Battle;
import com.pokemon.battle.SingleBattle;
import com.pokemon.battle.event.TextEvent;
import com.pokemon.controller.SingleBattleScreenController;
import com.pokemon.game.Pokemon;
import com.pokemon.game.Settings;
import com.pokemon.util.GifDecoder;

public class BattleRenderer {
    final Pokemon game;
    private SingleBattle battle;
    private Viewport gameViewport;
    private OrthographicCamera camera;

    private AssetManager assetManager;
    private TextureRegion background;
    private TextureRegion platform;

    //private Texture P_T;
    private Animation<TextureRegion> P_T;
    private Animation<TextureRegion> O_T;
    private Animation<TextureRegion> ball;
    //private Animation<TextureRegion> open;
    private Texture open;
    private Animation<TextureRegion> close;
    //private Texture O_T;


    private int squareSize = 100;

    private float playerSquareMiddleX = 0;
    private float playerSquareMiddleY = 0;
    private float opponentSquareMiddleX = 0;
    private float opponentSquareMiddleY = 0;

    //private Texture o;
    private int i = 1;
    int openCnt=0;
    int failCnt=30;
    private boolean check = false;
    private  BattleScreen battleScreen;
    private SingleBattleScreenController battleScreenController;

    public BattleRenderer(BattleScreen battleScreen, SingleBattleScreenController battleScreenController, Pokemon game, SingleBattle battle, OrthographicCamera camera){
        this.game = game;
        this.camera = camera;
        this.battle = battle;
        this.battleScreen = battleScreen;
        this.battleScreenController = battleScreenController;

        assetManager = new AssetManager();
        assetManager.load("battle/battlepack.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("battle/battlepack.atlas", TextureAtlas.class);
        background = atlas.findRegion("background");
        platform = atlas.findRegion("platform");
        ball = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/pokeball.gif").read());
        //open= GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/open1.gif").read());
        open= new Texture(Gdx.files.internal("battle/open.png"));
        close = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("battle/close_small.gif").read());
        //o = new Texture(Gdx.files.internal("pokemon/front/"+battle.getO_P().getName() +".gif"));
        //o = new Image(new Texture(Gdx.files.internal("pokemon/front/"+battle.getO_P().getName() +".gif")));
        // o.setDrawable(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("pokemon/front/"+battle.getO_P().getName() +".gif")))));

    }


    public void render(Batch batch,float elapsed) {
        //배틀에서 이미지 가져옴
        if(P_T != battle.getP_P().getImage()) {
            i=1;
            P_T = battle.getP_P().getImage();
        }
        O_T = battle.getO_P().getImage();

        //플레이어 플랫폼위치
        playerSquareMiddleX = Gdx.graphics.getWidth() / 2 - (squareSize + Gdx.graphics.getWidth() / 15);
        playerSquareMiddleY = Gdx.graphics.getHeight() / 2 + (25 * Settings.SCALE);

        //상대 플랫폼위치
        opponentSquareMiddleX = Gdx.graphics.getWidth() / 2 + (squareSize + Gdx.graphics.getWidth() / 15);
        opponentSquareMiddleY = Gdx.graphics.getHeight() / 2 + (25 * Settings.SCALE);

        float platformYOrigin = playerSquareMiddleY - platform.getRegionHeight() / 2 * Settings.SCALE;

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(platform,
                playerSquareMiddleX - platform.getRegionWidth() / 2 * Settings.SCALE,
                platformYOrigin - (50 * Settings.SCALE),
                platform.getRegionWidth() * Settings.SCALE,
                platform.getRegionHeight() * Settings.SCALE);
        batch.draw(platform,
                opponentSquareMiddleX - platform.getRegionWidth() / 2 * Settings.SCALE,
                platformYOrigin,
                platform.getRegionWidth() * Settings.SCALE,
                platform.getRegionHeight() * Settings.SCALE);

        float playerX = 0f;
        float playerY = 0f;

        float opponentX = 0f;
        float opponentY = 0f;

        playerX = playerSquareMiddleX - P_T.getKeyFrame(elapsed).getRegionWidth() / 2;
        // playerY = platformYOrigin - (P_T.getKeyFrame(elapsed).getRegionHeight() +  P_T.getKeyFrame(elapsed).getRegionHeight()/2);
        playerY = 183;

        opponentX = opponentSquareMiddleX - O_T.getKeyFrame(elapsed).getRegionWidth() / 2;
        //opponentY = platformYOrigin + O_T.getKeyFrame(elapsed).getRegionHeight() / 3;
        opponentY = 280;

        if(battle.getPokeball()) {
            i = 3;
        }
        if (i == 1) {
            batch.draw(ball.getKeyFrame(elapsed), 0, 110);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    i = 2;
                }
            }, 0.8f);
        }
        if (i == 2) {
            if (P_T != null)
                batch.draw(P_T.getKeyFrame(elapsed), playerX, playerY);
            if (O_T != null)
                batch.draw(O_T.getKeyFrame(elapsed), opponentX, opponentY);
        }
        if(i==3){
            if (P_T != null)
                batch.draw(P_T.getKeyFrame(elapsed), playerX, playerY);
            //몬스터볼 열린 모습
            batch.draw(open, opponentX+80, opponentY+60);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                }
            }, 0.8f);

            //들어가는 모션
            if(openCnt<30){
                batch.draw(O_T.getKeyFrame(elapsed),opponentX+openCnt*2,opponentY+openCnt*2,O_T.getKeyFrame(elapsed).getRegionWidth()-openCnt*2,O_T.getKeyFrame(elapsed).getRegionHeight()-openCnt*2);
                openCnt++;
                if(openCnt==30)
                    check = true;
            }
            //잡혔는지 안잡혔는지
            if(battle.getCapture()){
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if(openCnt!=0) {
                            battle.setPokeball(false);
                            battle.getO_P().setCapture(true);
                            battle.queueEvent(new TextEvent(battle.getO_P().getName() + "을(를) 잡았습니다!", true));
                            battle.setState(SingleBattle.STATE.WIN);
                            i=4;
                        }
                        openCnt = 0;
                    }
                }, 1f);
            }else if(!battle.getCapture()&&check){
                batch.draw(open, opponentX+80, opponentY+60);

                //511 , 300
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                    }
                }, 2f);


                if(failCnt>=1){
                    batch.draw(O_T.getKeyFrame(elapsed),opponentX+failCnt*2,opponentY+failCnt*2,
                            O_T.getKeyFrame(elapsed).getRegionWidth()-failCnt*2,O_T.getKeyFrame(elapsed).getRegionHeight()-failCnt*2);
                    failCnt--;
                    System.out.println(failCnt);
                }

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if(failCnt!=30){
                            battle.setPokeball(false);
                            i = 2;
                            check=false;
                            openCnt=0;
                        }
                        failCnt=30;
                    }
                }, 1f);

                if (O_T != null && check && failCnt==0)
                    batch.draw(O_T.getKeyFrame(elapsed), opponentX, opponentY);
            }
        }
        if(i==4){
            if (P_T != null)
                batch.draw(P_T.getKeyFrame(elapsed), playerX, playerY);

        }

    }

}
//닫히고
//batch.draw(close.getKeyFrame(elapsed), opponentX, opponentY + close.getKeyFrame(elapsed).getRegionHeight() + 10);
//System.out.println("닫힘");
       /*     Timer.schedule(new Timer.Task() {
                @Override
                public void run() {

                }
            }, 1f);*/
/*
            //흔들리는 거 추가,수정해야함

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {

                }
            }, 1f);*/
//if(false){
//if(true){
          /*  if(battle.getCapture()){
                //끝 ,수정해야함
                batch.draw(close.getKeyFrame(elapsed), opponentX, opponentY + close.getKeyFrame(elapsed).getRegionHeight() + 10);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        battle.setPokeball(false);
                    }
                }, 1f);
                System.out.println("성공");
                //System.out.println(battle.getO_P().getName());
            }else{
                //튀어나옴,수정해야함
                batch.draw(open.getKeyFrame(elapsed), opponentX, opponentY + open.getKeyFrame(elapsed).getRegionHeight() + 10);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        battle.setPokeball(false);
                    }
                }, 1f);
                System.out.println("실패");
            }
            //batch.draw(O_T.getKeyFrame(elapsed),opponentX,opponentY + open.getKeyFrame(elapsed).getRegionHeight() + 10,O_T.getKeyFrame(elapsed).getRegionWidth()-cnt*2,O_T.getKeyFrame(elapsed).getRegionHeight()-cnt*10);

            //batch.draw(o,opponentX,opponentY + open.getKeyFrame(elapsed).getRegionHeight() + 10,O_T.getKeyFrame(elapsed).getRegionWidth()-cnt*2,O_T.getKeyFrame(elapsed).getRegionHeight()-cnt*10);
        }else
            cnt = 0;*/