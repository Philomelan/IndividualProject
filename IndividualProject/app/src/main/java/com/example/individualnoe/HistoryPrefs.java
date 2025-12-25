package com.example.individualnoe;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryPrefs {

    private static final String PREFS_NAME = "GameHistory";
    private static final String KEY_ATTEMPTS = "attempts_list";
    private static HistoryPrefs instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public HistoryPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static HistoryPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new HistoryPrefs(context.getApplicationContext());
        }
        return instance;
    }

    public void saveAttempt(GameAttempt attempt) {
        List<GameAttempt> attempts = getAttempts();
        attempts.add(0, attempt);

        String attemptsJson = gson.toJson(attempts);
        sharedPreferences.edit().putString(KEY_ATTEMPTS, attemptsJson).apply();
    }

    public List<GameAttempt> getAttempts() {
        String attemptsJson = sharedPreferences.getString(KEY_ATTEMPTS, "[]");
        Type type = new TypeToken<ArrayList<GameAttempt>>(){}.getType();
        return gson.fromJson(attemptsJson, type);
    }

    public void clearAttempts() {
        String emptyJson = gson.toJson(new ArrayList<>());
        sharedPreferences.edit().putString(KEY_ATTEMPTS, emptyJson).apply();
    }
}
