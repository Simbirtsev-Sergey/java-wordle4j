package ru.yandex.practicum.exception;

public class WordNotFoundInDictionary extends Exception {

    String word;

    public WordNotFoundInDictionary(String message, String word) {
        super(message);
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
