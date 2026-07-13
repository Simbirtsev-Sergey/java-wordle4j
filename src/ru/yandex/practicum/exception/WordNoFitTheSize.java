package ru.yandex.practicum.exception;

public class WordNoFitTheSize extends Exception {
    String word;
    public WordNoFitTheSize(String message, String word) {
        super(message);
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
