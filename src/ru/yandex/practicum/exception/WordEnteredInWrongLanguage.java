package ru.yandex.practicum.exception;

public class WordEnteredInWrongLanguage extends Exception {

    String word;
    public WordEnteredInWrongLanguage(String message, String word) {
        super(message);
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
