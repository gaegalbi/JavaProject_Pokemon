package com.pokemon.model;

public enum OBJECT {
    STONE(1),
    WOOD(2);

    int type;

    OBJECT(int type) {
        this.type = type;
    }
}
