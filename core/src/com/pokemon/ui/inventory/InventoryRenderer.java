/*
package com.pokemon.ui.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class InventoryRenderer extends Window {

    private static final WindowStyle windowStyle;
   // private static final ImageButton.ImageButtonStyle closeButtonStyle;
    static {
        //TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("windows.pack"));
        AssetManager assetManager = new AssetManager();
        //TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("windows.png"));
        assetManager.load("window-1-background.png", Texture.class);
        assetManager.load("window-1-close-button.png", Texture.class);
        assetManager.finishLoading();
       // Texture close = assetManager.get("window-1-close-button.png");
        Texture back = assetManager.get("window-1-background.png");

        windowStyle = new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(back));
        //closeButtonStyle = new ImageButton.ImageButtonStyle();
        //closeButtonStyle.imageUp = new TextureRegionDrawable(close);
    }

    */
/**
     * Default constructor.
     *//*

    public InventoryRenderer() {
        super("", windowStyle);

       addListener(new DragListener() {
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                // example code below for origin and position
               setOrigin(Gdx.input.getX(), Gdx.input.getY());
               setPosition(x, y);
                System.out.println("touchdragged" + x + ", " + y);

            }
        });
        setClip(false);
        setTransform(true);
    }

}*/
