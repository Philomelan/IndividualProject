package com.example.individualnoe;

import android.content.Context;

public class Character {
    public int id;
    public String name;
    public String avatarName;


    public Character(int id, String name, String avatarName) {
        this.id = id;
        this.name = name;
        this.avatarName = avatarName;
    }


    public int getAvatarResId(Context context) {
        switch (avatarName) {
            case "kai": return R.drawable.kai;
            case "beatrice": return R.drawable.beatrice;
            case "korvin": return R.drawable.korvin;
            case "rork": return R.drawable.rork;
            case "veks": return R.drawable.veks;
            case "lira": return R.drawable.lira;
            case "zora": return R.drawable.zora;
            default: return R.drawable.korvin;
        }
    }
}
