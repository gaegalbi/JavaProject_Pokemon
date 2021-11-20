package com.pokemon.screen;

import com.pokemon.model.RenderHelper;

import java.util.Comparator;

public class WorldObjectYComparator implements Comparator<RenderHelper> {
    @Override
    public int compare(RenderHelper o1, RenderHelper o2) {
        return Float.compare(o2.getY(), o1.getY());
    }
}
