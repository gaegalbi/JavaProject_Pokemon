package com.pokemon.ui.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ImageUI extends Image {

    public ImageUI(TextureRegion style) {
        super(style);
    }

    public ImageUI(TextureRegion skin, Vector2 origin, int w, int h) {
        this(skin);
        this.setSize(w, h);
        this.setPosition(origin.x, origin.y);
        this.setTouchable(Touchable.disabled);
    }

}
