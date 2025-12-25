package com.example.individualnoe;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnGame, btnHistory, btnSettings;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setButtonListeners();

        audioManager = AudioManager.getInstance(this);
        audioManager.playBackgroundMusic(R.raw.music);
    }

    private void initViews() {
        btnGame = findViewById(R.id.btnGame);
        btnHistory = findViewById(R.id.btnHistory);
        btnSettings = findViewById(R.id.btnSettings);
    }

    private void setButtonListeners() {
        btnGame.setOnClickListener(v -> {
            audioManager.playClickSound();
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            audioManager.playClickSound();
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            audioManager.playClickSound();
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
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