package com.pokemon.ui.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pokemon.inventory.Item;
import com.pokemon.util.SkinGenerator;

/**
 * Window UI that acts as a tooltip for an item displaying its stats
 * The window should adjust its size according to how much information an item has
 *
 * @author Ming Li
 */
public class ItemTooltip extends Window {

    private Label desc;
    private static final WindowStyle windowStyle;


    static{
        AssetManager assetManager = new AssetManager();
        //투명한 186 * 70
        assetManager.load("texture/default.png", Texture.class);
        assetManager.load("texture/back.png", Texture.class);
        assetManager.finishLoading();
       // Texture back = assetManager.get("texture/default.png");
        Texture back = assetManager.get("texture/back.png");

        windowStyle = new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(back));
       // windowStyle = new WindowStyle(skin.getFont("font"), Color.BLACK, new TextureRegionDrawable(back));
    }

    public ItemTooltip(Skin skin) {
        super("",windowStyle);
        desc = new Label("", skin);

        left();
        // fix padding because of scaling
        pad(10,10,10,10);
        add(desc);
        pack();
        this.setTouchable(Touchable.disabled);
        this.setVisible(false);
        this.setMovable(false);
        this.setOrigin(Align.bottomLeft);
    }

    public void show(Item item, float x, float y) {
        this.setPosition(x, y);
        this.setVisible(true);
        updateText(item);
    }

    public void updateText(Item item) {
        this.getTitleLabel().setText(item.labelName);
        desc.setText(item.getInfo());
        desc.setAlignment(center().getAlign());
        desc.setColor(Color.GREEN);
        pack();
    }

    public void hide() {
        this.setVisible(false);
    }

}
