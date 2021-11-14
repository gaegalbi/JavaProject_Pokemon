package com.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pokemon.battle.Battle;
import com.pokemon.game.Pokemon;
import com.pokemon.game.Settings;
import com.pokemon.model.PK;

import static com.pokemon.screen.BattleScreen.playerNum;
import static com.pokemon.ui.LoginUi.playerID;

public class BattleRenderer {
    final Pokemon game;
    private Battle battle;
    private Viewport gameViewport;
    private OrthographicCamera camera;

    private AssetManager assetManager;
    private TextureRegion background;
    private TextureRegion platform;

    private Texture P_T;
    private Texture O_T;

    private int squareSize = 100;

    private float playerSquareMiddleX = 0;
    private float playerSquareMiddleY = 0;
    private float opponentSquareMiddleX = 0;
    private float opponentSquareMiddleY = 0;


    public BattleRenderer(Pokemon game, Battle battle, OrthographicCamera camera){
        this.game = game;
        this.camera = camera;
        this.battle = battle;
        assetManager = new AssetManager();

        assetManager.load("battle/battlepack.atlas", TextureAtlas.class);
        assetManager.load("pokemon/bulbasaur.png", Texture.class);
        assetManager.load("pokemon/slowpoke.png", Texture.class);
        assetManager.load("pokemon/"+battle.getP_P().getName() +".png", Texture.class);
        assetManager.load("pokemon/"+battle.getO_P().getName() +".png", Texture.class);
        assetManager.finishLoading();
/*
        this.P_T = assetManager.get("pokemon/bulbasaur.png", Texture.class);
        this.O_T = assetManager.get("pokemon/slowpoke.png", Texture.class);
        String[] userKey = {playerID,String.valueOf(playerNum)};

        PK Player = new PK(userKey,P_T);
        PK Opponent = new PK(userKey,P_T);

        System.out.print(Player.getName());*/
        P_T= assetManager.get("pokemon/"+battle.getP_P().getName()+".png",Texture.class);
        O_T= assetManager.get("pokemon/"+battle.getO_P().getName()+".png",Texture.class);
        TextureAtlas atlas = assetManager.get("battle/battlepack.atlas", TextureAtlas.class);
        background = atlas.findRegion("background");
        platform = atlas.findRegion("platform");
    }

    public void render(Batch batch) {
        // recalc the player's square's middle
        playerSquareMiddleX = Gdx.graphics.getWidth()/2 - (squareSize + Gdx.graphics.getWidth()/15);
        playerSquareMiddleY = Gdx.graphics.getHeight()/2 + (25*Settings.SCALE);

        // recalc the opponent's square's middle
        opponentSquareMiddleX = Gdx.graphics.getWidth()/2 + (squareSize + Gdx.graphics.getWidth()/15);
        opponentSquareMiddleY = Gdx.graphics.getHeight()/2 + (25*Settings.SCALE);

        float platformYOrigin = playerSquareMiddleY - platform.getRegionHeight()/2*Settings.SCALE;


        batch.draw(background, 0, 0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    //Gdx.graphics.getWidth()
        batch.draw(platform,
                playerSquareMiddleX-platform.getRegionWidth()/2*Settings.SCALE,
                platformYOrigin-(50*Settings.SCALE),
                platform.getRegionWidth()*Settings.SCALE,
                platform.getRegionHeight()*Settings.SCALE);
        batch.draw(platform,
                opponentSquareMiddleX-platform.getRegionWidth()/2*Settings.SCALE,
                platformYOrigin,
                platform.getRegionWidth()*Settings.SCALE,
                platform.getRegionHeight()*Settings.SCALE);

        float playerX = 0f;
        float playerY = 0f;

        if(P_T!=null){
            playerX = playerSquareMiddleX - P_T.getWidth()/4;
            //playerX = opponentSquareMiddleX - O_T.getWidth()/2*Settings.SCALE;
            playerY = platformYOrigin-(50*Settings.SCALE);
            batch.draw(
                    P_T,
                    playerX,
                    playerY,
                    P_T.getWidth()/2,
                    //playerWidth*P_T.getWidth()*Settings.SCALE,
                    P_T.getHeight()/2,
                    //playerHeight*P_T.getHeight()*Settings.SCALE,
                    0,
                    0,
                    P_T.getWidth(),
                    P_T.getHeight(),
                    false, // 좌우반전
                    false);
        }
        float opponentX = 0f;
        float opponentY = 0f;
        if (O_T != null) {
            opponentX = opponentSquareMiddleX - O_T.getWidth()/4;
           // opponentX = opponentSquareMiddleX - O_T.getWidth()/2*Settings.SCALE;
            opponentY = platformYOrigin;
            batch.draw(
                    O_T,
                    opponentX,
                    opponentY,
                    O_T.getWidth()/2,
                    //opponentWidth*O_T.getWidth()*Settings.SCALE,
                    O_T.getHeight()/2,
                    //opponentHeight*O_T.getHeight()*Settings.SCALE,
                    0,
                    0,
                    O_T.getWidth(),
                    O_T.getHeight(),
                    true, //좌우반전
                    false);
        }

    }
}
