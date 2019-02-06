package com.example.cheho.mydictionary;

public class Word {
    private int id;
    private String word;
    private String translation;
    private int level;
    private int counter;

    Word(int id, String word, String translation, int level, int counter) {
        this.id = id;
        this.word = word;
        this.translation = translation;
        this.level = level;
        this.counter = counter;
    }

    public int getId() {
        return id;
    }
     String getIDToString() {
         return String.valueOf(id);
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

     int getLevel() {
        return level;
    }

    public int getCounter() {
        return counter;
    }
}
