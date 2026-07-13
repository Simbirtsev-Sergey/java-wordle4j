package ru.yandex.practicum;

import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words;

    public WordleDictionary(final WordleDictionaryLoader wordleDictionaryLoader, final String nameFile) {
        words = wordleDictionaryLoader.getWordsFromFile(nameFile);
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