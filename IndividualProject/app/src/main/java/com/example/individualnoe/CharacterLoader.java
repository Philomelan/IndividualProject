package com.example.individualnoe;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CharacterLoader {

    public static List<Character> loadCharacters(Context context) {
        try (InputStream is = context.getAssets().open("characters.json")) {
            String jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Type characterListType = new TypeToken<List<Character>>() {}.getType();
            return new Gson().fromJson(jsonString, characterListType);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
