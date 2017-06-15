package org.home.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsUtil implements Settings {
    private static SettingsUtil ourInstance = new SettingsUtil();
    private Preferences preferences;

    public static SettingsUtil getInstance() {
        return ourInstance;
    }

    private SettingsUtil() {
        preferences = Gdx.app.getPreferences("Settings loader");
    }

    public void loadingSettings() {
        Constants.isMusic = preferences.getBoolean("music");
        Constants.isSounds = preferences.getBoolean("sound");
        Constants.maxScoreCount = preferences.getInteger("score");
    }

    public void saveSettings() {
        preferences.putBoolean("music", Constants.isMusic);
        preferences.putBoolean("sound", Constants.isSounds);
        preferences.putInteger("score", Constants.maxScoreCount);
        preferences.flush();
    }
}
