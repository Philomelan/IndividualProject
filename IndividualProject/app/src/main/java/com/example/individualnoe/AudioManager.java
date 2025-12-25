package com.example.individualnoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;

public class AudioManager {
    private static AudioManager instance;

    private Context context;
    private SharedPreferences prefs;

    private MediaPlayer backgroundMusic;
    private SoundPool soundPool;

    private int swipeSoundId;
    private int clickSoundId;

    private float musicVolume = 0.5f;
    private float soundVolume = 0.5f;
    private boolean musicEnabled = true;
    private boolean soundsEnabled = true;

    private static final String PREFS_NAME = "GameSettings";
    private static final String KEY_MUSIC_VOLUME = "music_volume";
    private static final String KEY_SOUND_VOLUME = "sound_volume";

    private AudioManager(Context context) {
        this.context = context.getApplicationContext();
        prefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        initSoundPool();
        loadSettings();
    }

    public static synchronized AudioManager getInstance(Context context) {
        if (instance == null) {
            instance = new AudioManager(context);
        }
        return instance;
    }

    private void initSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attributes)
                .build();

        swipeSoundId = soundPool.load(context, R.raw.swipe_sound, 1);
        clickSoundId = soundPool.load(context, R.raw.click_sound, 1);
    }

    private void loadSettings() {
        musicVolume = prefs.getInt(KEY_MUSIC_VOLUME, 50) / 100f;
        soundVolume = prefs.getInt(KEY_SOUND_VOLUME, 50) / 100f;
    }

    public void playBackgroundMusic(int musicResId) {
        if (!musicEnabled || backgroundMusic != null) return;

        new Thread(() -> {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, musicResId);

            new Handler(Looper.getMainLooper()).post(() -> {
                if (mediaPlayer != null) {
                    backgroundMusic = mediaPlayer;
                    backgroundMusic.setLooping(true);
                    backgroundMusic.setVolume(musicVolume, musicVolume);
                    backgroundMusic.start();
                }
            });
        }).start();
    }

    public void playSwipeSound() {
        if (!soundsEnabled) return;
        soundPool.play(swipeSoundId, soundVolume, soundVolume, 1, 0, 1f);
    }

    public void playClickSound() {
        if (!soundsEnabled) return;
        soundPool.play(clickSoundId, soundVolume, soundVolume, 1, 0, 1f);
    }

    public void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void resumeMusic() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying() && musicEnabled) {
            backgroundMusic.start();
        }
    }

    public void updateMusicVolume(int volumePercent) {
        musicVolume = volumePercent / 100f;
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(musicVolume, musicVolume);
        }
        saveSettings();
    }

    public void updateSoundVolume(int volumePercent) {
        soundVolume = volumePercent / 100f;
        saveSettings();
    }

    private void saveSettings() {
        prefs.edit()
                .putInt("music_volume", (int)(musicVolume * 100))
                .putInt("sound_volume", (int)(soundVolume * 100))
                .apply();
    }
}
