package org.home.game.utils;


import com.badlogic.gdx.Gdx;

public final class Constants {

    public static final float PPM = 100f;

    // Screen variable
    public static final int WIDTH = Gdx.graphics.getWidth();
    public static final int HEIGHT = Gdx.graphics.getHeight();

    //Game bits
    public static final int NOTHING_BIT = 128;
    public static final int MARTIAN_BIT = 1;
    public static final int MARTIAN_HEAD_BIT = 2;
    public static final int ASTEROID_BIT = 4;
    public static final int ITEM_BIT = 8;
    public static final int TUBE_BIT = 16;
    public static final int MAP_BIT = 32;
    public static final int MARTIAN_ITEM_BIT = 64;

    public static boolean isSounds = true;
    public static boolean isMusic = true;

    public static int maxScoreCount;
}
