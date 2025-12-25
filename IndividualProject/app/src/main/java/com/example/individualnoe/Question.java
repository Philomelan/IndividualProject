package com.example.individualnoe;

public class Question {
    public int id;
    public String questionText;
    public String optionA;
    public String optionB;
    public int[] consA;
    public int[] consB;
    public int characterId;
    public Character character;
    public boolean shown = false;


    public Question(int id, String questionText, String optionA, String optionB,
                    int[] consA, int[] consB, int characterId) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.consA = consA;
        this.consB = consB;
        this.characterId = characterId;
    }

}
