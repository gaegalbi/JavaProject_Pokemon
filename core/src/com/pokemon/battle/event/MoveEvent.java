package com.pokemon.battle.event;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.*;

public class MoveEvent{
    Animation<TextureRegion> animation;
    private float timer = 0f;
    private float delay;
    private int i=0;
    private int j=0;
    private int count = 0;
    private boolean finished;
    private boolean turn = true;
    private float a,b;
    private Timer m = new Timer();
    public MoveEvent(Animation<TextureRegion> animation, float delay,boolean finished,float a,float b) {
        this.animation = animation;
        this.delay = delay;
        this.finished = finished;
        this.a = a;
        this.b = b;
    }
    public void begin(SpriteBatch batch,float elapsed) {
        if(turn == true) {
            if (i == j) {
                batch.draw(animation.getKeyFrame(elapsed), 150f + i, 200f);
                count++;
            }
        }else{
            if (i == j) {
                batch.draw(animation.getKeyFrame(elapsed), 500f - i, 200f);
                count--;
            }
        }
        if(finished == false){
            i++;
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(finished) {
                    i += 7;
                    j += 7;
                    if(i == 350){
                        turn = false;
                        i=0;
                        j=0;
                    }
                    if (count <= 0) {
                        finished = false;
                    }
                }
            }
        };
        m.schedule(task,1);
    }
}
