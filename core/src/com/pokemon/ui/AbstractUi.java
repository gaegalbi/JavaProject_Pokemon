package com.pokemon.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class AbstractUi {
    public void resize(int width, int height) {}
    public void update() {}
    public void dispose() {}
    public Stage getStage() {
        return null;
    }
}
