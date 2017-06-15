package org.home.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import static org.home.game.utils.Constants.*;

public class AudioManager {
    private static AudioManager ourInstance = new AudioManager();

    private Sound tubeSound, dieSound;
    private Music mainMusic;

    public static AudioManager getInstance() {
        return ourInstance;
    }

    private AudioManager() {
        tubeSound = Gdx.audio.newSound(Gdx.files.internal("audio/tubeSound.ogg"));
        dieSound = Gdx.audio.newSound(Gdx.files.internal("audio/dieSound.ogg"));
        mainMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/main.mp3"));
        mainMusic.setVolume(.5f);
        mainMusic.setLooping(true);
    }

    public void playTubeSound() {
        if (isSounds)
            tubeSound.play(1f);
    }

    public void playDieSound() {
        if (isSounds)
           dieSound.play(1f);
    }

    public void playMainMusic() {
        if (isMusic)
            mainMusic.play();
    }

    public void pauseMainMusic() {
        mainMusic.pause();
    }

    public boolean isPlayingMainMusic() {
        return mainMusic.isPlaying();
    }

    public void dispose() {
        tubeSound.dispose();
        dieSound.dispose();
        mainMusic.dispose();
    }
}
