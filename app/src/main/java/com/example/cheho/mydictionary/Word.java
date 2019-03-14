package com.example.cheho.mydictionary;

import android.support.annotation.NonNull;

public class Word {
    private int id;
    private String word;
    private String translation;
    private int level;
    private int counter;
    private int repetition = 0;

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

     String getWord() {
        return word;
    }

     String getTranslation() {
        return translation;
    }

    int getLevel() {
        return level;
    }


     void successfulRepetition(){this.repetition++;}
     void unSuccessfulRepetition(){this.repetition=0;}

    boolean repetitionCompleted(){
        return repetition > 2;
    }

    @NonNull
    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                ", level=" + level +
                ", counter=" + counter +
                ", repetition=" + repetition +
                '}';
    }

    String getIDToString() {
        return String.valueOf(id);
    }
}
