package ru.yandex.practicum.hint;

import ru.yandex.practicum.wordle.WordleDictionary;

import java.util.*;

public class Hint {

    private final WordleDictionary wordleDictionary;
    private final List<String> patterns;
    private final List<String> listInputWords;
    private final List<Character> lettersExactlyPresent = new ArrayList<>();

    public Hint(final WordleDictionary wordleDictionary, final List<String> patterns,
                final List<String> listInputWords) {
        this.wordleDictionary = wordleDictionary;
        this.patterns = new ArrayList<>(patterns);
        this.listInputWords = new ArrayList<>(listInputWords);
    }

    public List<String> generationHint() {
        Map<Integer, ArrayList<Character>> possibleLetters = new HashMap<>();
        StringBuilder mask = new StringBuilder("*****");
        final List<String> appropriateWords = new ArrayList<>();
        List<String> words = wordleDictionary.getWords();

        // Генерируем маску с плюсами со всех шаблонах
        buildMask(mask);

        // Создаем лист с русскими буквами (за исключением буквы ё)
        List<Character> letters = listWithRussianLetters();

        // Определяем: какие буквы можно поставить на каждую позицию
        fillInEachPosition(possibleLetters, mask, letters);

        // Исключаем буквы, которые не могут стоять на каждой позиции
        excludeLetterEverywhere(possibleLetters);

        // Фильтрация слов
        filteringWords(words, mask, possibleLetters, appropriateWords);

        return appropriateWords;
    }

    // На основе паттернов, определяем на каких позициях мы знаем точные буквы
    // т.е. ищем "плюсики"
    public void buildMask(StringBuilder mask) {
        for (int position = 0; position < patterns.size(); ++position) {
            int startIndex = 0;
            String pattern = patterns.get(position);
            while ((startIndex = pattern.indexOf("+", startIndex)) != -1) {
                int ind = pattern.indexOf("+", startIndex);
                startIndex++;

                mask.setCharAt(ind, listInputWords.get(position).charAt(ind));
            }
        }
    }

    // Создаем лист с русскими буквами
    public List<Character> listWithRussianLetters() {
        List<Character> letters = new ArrayList<>();
        for (int codeRussianLetter = 1072; codeRussianLetter < 1104; ++codeRussianLetter) {
            letters.add((char) codeRussianLetter);
        }
        return letters;
    }

    // Какие буквы могут быть на каждой позиции
    // Если создается пустой ArrayList, то программа уже знает какая буква будет на этом месте
    public void fillInEachPosition(final Map<Integer, ArrayList<Character>> possibleLetters, final StringBuilder mask,
                                   final List<Character> letters) {
        for (int position = 0; position < 5; ++position) {
            if (mask.charAt(position) != '*') {
                possibleLetters.put(position, new ArrayList<>());
            } else {
                possibleLetters.put(position, new ArrayList<>(letters));
            }
        }
    }

    // Проходимся по всем паттернам
    // Если стоит '-', то удаляем этот символ из всех возможных ArrayList
    // Если стоит '^', то удаляем этот символ только из ArrayList, который соответсвует этой позиции
    public void excludeLetterEverywhere(Map<Integer, ArrayList<Character>> possibleLetters) {
        for (int numberPattern = 0; numberPattern < patterns.size(); ++numberPattern) {
            for (int numberWord = 0; numberWord < 5; ++numberWord) {

                Character character = listInputWords.get(numberPattern).charAt(numberWord);

                if (patterns.get(numberPattern).charAt(numberWord) == '-') {
                    for (int position = 0; position < 5; ++position) {
                        possibleLetters.get(position).remove(character);
                    }
                }

                if (patterns.get(numberPattern).charAt(numberWord) == '^') {
                    possibleLetters.get(numberWord).remove(character);
                    lettersExactlyPresent.add(character);
                }
            }
        }
    }

    public void filteringWords(final List<String> words, final StringBuilder mask, final Map<Integer,
            ArrayList<Character>> possibleLetters, final List<String> appropriateWords) {
        for (String word : words) {
            if (matchesPattern(word, mask, possibleLetters)) {
                appropriateWords.add(word);
            }
        }
    }

    // Проверить, подходит ли слово из словаря под паттерны
    public boolean matchesPattern(final String word, final StringBuilder mask, Map<Integer,
            ArrayList<Character>> possibleLetters) {
        for (int position = 0; position < 5; ++position) {
            if (mask.charAt(position) == '*') {
                if (!possibleLetters.get(position).contains(word.charAt(position))) {
                    return false;
                }
            } else {
                if (word.charAt(position) != mask.charAt(position)) {
                    return false;
                }
            }
            for (Character character : lettersExactlyPresent) {
                if (!word.contains(character.toString())) {
                    return false;
                }
            }
        }
        return true;
    }
}