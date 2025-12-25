package com.example.individualnoe;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionLoader {

    public static List<Question> loadQuestions(Context context) {
        try (InputStream is = context.getAssets().open("questions.json")) {
            String jsonString;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // API 33+
                jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            } else {
                // Для старых версий
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                jsonString = result.toString(StandardCharsets.UTF_8.name());
            }

            Type questionListType = new TypeToken<List<Question>>() {}.getType();
            return new Gson().fromJson(jsonString, questionListType);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Question> loadQuestionsWithCharacters(Context context, List<Character> characters) {
        List<Question> questions = loadQuestions(context);

        Map<Integer, Character> characterMap = new HashMap<>();
        for (Character character : characters) {
            characterMap.put(character.id, character);
        }

        for (Question question : questions) {
            question.character = characterMap.get(question.characterId);
        }

        return questions;
    }

}