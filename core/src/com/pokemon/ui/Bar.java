package com.pokemon.ui;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Widget that displays HP.
 *
 * @author hydrozoa
 */
public class Bar extends Widget {

    private enum STATE {
        ANIMATING, IDLE;
    }

    private Skin skin;

    private float amount = 1f;
    private Table barArea;

    private Drawable green;
    private Drawable yellow;
    private Drawable red;
    private Drawable background_bar;
    private Drawable bar_left;
    private Drawable bar;

    private STATE state = STATE.IDLE;

    private float timer = 0f;
    private float animationDuration = 0f;

    private float barStart;
    private float barEnd;

    public Bar(Skin skin) {
        super();
        this.skin = skin;

        green = skin.getDrawable("green");
        yellow = skin.getDrawable("yellow");
        red = skin.getDrawable("red");
        background_bar = skin.getDrawable("background_hpbar");
        bar_left = skin.getDrawable("hpbar_side");
        bar = skin.getDrawable("hpbar_bar");

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        int padLeft = 1;
        int padRight = 2;
        int padTop = 2;
        int padBottom = 2;

        float barWidth = amount * (bar.getMinWidth()-padLeft-padRight);

        Drawable barColor = null;
        if (amount <= 0.1) {
            barColor = red;
        } else if (amount <= 0.5) {
            barColor = yellow;
        } else {
            barColor = green;
        }

        /* HP LOGO TO THE LEFT */
        bar_left.draw(batch, this.getX(), this.getY(), bar_left.getMinWidth(), bar_left.getMinHeight());

        /* BACKGROUND OF THE BAR */
        background_bar.draw(batch, this.getX()+bar_left.getMinWidth()+padLeft, this.getY()+padBottom, bar.getMinWidth()-padRight-padLeft, bar.getMinHeight()-padTop-padBottom);

        /* ACTUAL COLORED HP */
        barColor.draw(batch, this.getX()+bar_left.getMinWidth()+padLeft, this.getY()+padBottom, barWidth, (bar.getMinHeight()-padTop-padBottom));

        /* HP BAR FRAME */
        bar.draw(batch, this.getX()+bar_left.getMinWidth(), this.getY(), bar.getMinWidth(), bar.getMinHeight());
    }

    @Override
    public float getMinHeight() {
        return bar_left.getMinHeight();
    }

    @Override
    public float getMinWidth() {
        return bar_left.getMinWidth()+bar.getMinWidth();
    }


    public void displayBarLeft(float bar) {
        this.amount = bar;
        amount = MathUtils.clamp(amount, 0f, 1f);
    }
}
