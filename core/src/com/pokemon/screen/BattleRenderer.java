package com.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pokemon.battle.Battle;
import com.pokemon.game.Pokemon;
import com.pokemon.game.Settings;

public class BattleRenderer {
    final Pokemon game;
    private Battle battle;
    private Viewport gameViewport;
    private OrthographicCamera camera;

    private AssetManager assetManager;
    private TextureRegion background;
    private TextureRegion platform;

    //private Texture P_T;
    private Animation<TextureRegion> P_T;
    private Animation<TextureRegion> O_T;
    //private Texture O_T;
    float elapsed;

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
        assetManager.finishLoading();

        //배틀에서 이미지 가져옴
        P_T = battle.getP_P().getImage();
        O_T = battle.getO_P().getImage();

        TextureAtlas atlas = assetManager.get("battle/battlepack.atlas", TextureAtlas.class);
        background = atlas.findRegion("background");
        platform = atlas.findRegion("platform");

    }

    public void render(Batch batch,float elapsed) {
        this.elapsed = elapsed;
        //플레이어 플랫폼위치
        playerSquareMiddleX = Gdx.graphics.getWidth()/2 - (squareSize + Gdx.graphics.getWidth()/15);
        playerSquareMiddleY = Gdx.graphics.getHeight()/2 + (25*Settings.SCALE);

        //상대 플랫폼위치
        opponentSquareMiddleX = Gdx.graphics.getWidth()/2 + (squareSize + Gdx.graphics.getWidth()/15);
        opponentSquareMiddleY = Gdx.graphics.getHeight()/2 + (25*Settings.SCALE);

        float platformYOrigin = playerSquareMiddleY - platform.getRegionHeight()/2*Settings.SCALE;

        batch.draw(background, 0, 0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
            playerX = playerSquareMiddleX-P_T.getKeyFrame(elapsed).getRegionWidth()/2;
            playerY = platformYOrigin-P_T.getKeyFrame(elapsed).getRegionHeight();
            batch.draw(P_T.getKeyFrame(elapsed), playerX , playerY );
        }
        float opponentX = 0f;
        float opponentY = 0f;
        if (O_T != null) {
            opponentX = opponentSquareMiddleX - O_T.getKeyFrame(elapsed).getRegionWidth()/2;
            opponentY = platformYOrigin+O_T.getKeyFrame(elapsed).getRegionHeight()/3;
            batch.draw(O_T.getKeyFrame(elapsed),  opponentX, opponentY);
        }

    }
}
