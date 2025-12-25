package com.example.individualnoe;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private MaterialButton btnMainMenu, btnClearHistory;
    private RecyclerView recyclerView;

    private HistoryAdapter adapter;
    private List<GameAttempt> attemptsList;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.history), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setListeners();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        audioManager = AudioManager.getInstance(this);

        attemptsList = new ArrayList<>();
        adapter = new HistoryAdapter(attemptsList);
        recyclerView.setAdapter(adapter);

        loadHistory();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvHistory);
        btnMainMenu = findViewById(R.id.btnMainMenu);
        btnClearHistory = findViewById(R.id.btnClearHistory);
    }

    private void setListeners() {
        btnMainMenu.setOnClickListener(v -> {
            audioManager.playClickSound();
            finish();
        });

        btnClearHistory.setOnClickListener(v -> {
            audioManager.playClickSound();
            showClearConfirm();
        });
    }

    private void loadHistory() {
        HistoryPrefs historyPrefs = HistoryPrefs.getInstance(this);
        attemptsList.addAll(historyPrefs.getAttempts());
        adapter.notifyDataSetChanged();
    }


    private void showClearConfirm() {
        new AlertDialog.Builder(this)
                .setTitle("Очистить историю")
                .setMessage("Вы уверены, что хотите очистить историю игр?")
                .setPositiveButton("Очистить", (dialog, which) -> {
                    clearHistory();
                })
                .setNegativeButton("Отмена", null)
                .setIcon(R.drawable.ic_delete)
                .show();
    }

    private void clearHistory() {
        HistoryPrefs historyPrefs = HistoryPrefs.getInstance(this);
        historyPrefs.clearAttempts();

        attemptsList.clear();
        adapter.notifyDataSetChanged();
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
