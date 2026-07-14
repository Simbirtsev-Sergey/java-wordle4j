package ru.yandex.practicum.wordle;

import java.util.List;


public class WordleDictionary {

    private final List<String> words;

    public WordleDictionary(final WordleDictionaryLoader wordleDictionaryLoader, final String nameFile) {
        words = wordleDictionaryLoader.getWordsFromFile(nameFile);
    }

    public WordleDictionary(final List<String> words) {
        this.words = words;
    }

    public int getLengthDictionary() {
        return words.size();
    }

    public String getTheHiddenWord(final int index) {
        return words.get(index);
    }

    public boolean isWordInDictionary(final String word) {
        return words.contains(word);
    }

    public List<String> getWords() {
        return words;
    }
}