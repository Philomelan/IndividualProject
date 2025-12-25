package com.example.individualnoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    private Button btnBack;
    private SeekBar sbMusic, sbSound;

    private AudioManager audioManager;

    private SharedPreferences prefs;
    private static final String PREFS_NAME = "GameSettings";
    private static final String KEY_MUSIC_VOLUME = "music_volume";
    private static final String KEY_SOUND_VOLUME = "sound_volume";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        audioManager = AudioManager.getInstance(this);

        initViews();
        setListeners();
        loadSettings();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        sbMusic = findViewById(R.id.sbMusic);
        sbSound = findViewById(R.id.sbSound);
    }

    private void setListeners() {
        btnBack.setOnClickListener(v -> {
            audioManager.playClickSound();
            finish();
        });

        sbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int volume, boolean fromUser) {
                if (fromUser) { audioManager.updateMusicVolume(volume); }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sbSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int volume, boolean fromUser) {
                if (fromUser) {
                    audioManager.updateSoundVolume(volume);
                    audioManager.playClickSound();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void loadSettings() {
        sbMusic.setProgress(prefs.getInt(KEY_MUSIC_VOLUME, 50));
        sbSound.setProgress(prefs.getInt(KEY_SOUND_VOLUME, 50));
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioManager.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioManager.resumeMusic();
    }
}