package ru.yandex.practicum;

import java.util.*;

public class Hint {

    private final WordleDictionary wordleDictionary;
    private final List<String> patterns;
    private final List<String> listInputWords;
    private static final List<Character> lettersExactlyPresent = new ArrayList<>();

    public Hint(final WordleDictionary wordleDictionary, final List<String> patterns,
                final List<String> listInputWords) {
        this.wordleDictionary = wordleDictionary;
        this.patterns = new ArrayList<>(patterns);
        this.listInputWords = new ArrayList<>(listInputWords);
    }

    public List<String> generationHint() {
        Map<Integer, ArrayList<Character>> possibleLetters = new HashMap<>();
        StringBuilder mask = new StringBuilder("*****");


        for (int i = 0; i < patterns.size(); ++i) {
            int startIndex = 0;
            String pattern = patterns.get(i);
            while ((startIndex = pattern.indexOf("+", startIndex)) != -1) {
                int ind = pattern.indexOf("+", startIndex);
                startIndex++;

                mask.setCharAt(ind, listInputWords.get(i).charAt(ind));
            }
        }

        // Генерируем массив с русскими буквами (за исключением буквы ё)
        List<Character> letters = new ArrayList<>();
        for (int codeRussianLetter = 1072; codeRussianLetter < 1104; ++codeRussianLetter) {
            letters.add((char) codeRussianLetter);
        }

        // Какие буквы могут быть на каждой позиции
        // Если создается пустой ArrayList, то программа уже знает какая буква будет на этом месте
        for (int i = 0; i < 5; ++i) {
            if (mask.charAt(i) != '*') {
                possibleLetters.put(i, new ArrayList<>());
            } else {
                possibleLetters.put(i, new ArrayList<>(letters));
            }
        }

        // Проходимся по всем паттернам
        // Если стоит '-', то удаляем этот символ из всех возможных ArrayList
        // Если стоит '^', то удаляем этот символ только из ArrayList, который соответсвует этой позиции
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

        final List<String> appropriateWords = new ArrayList<>();
        List<String> words = wordleDictionary.getWords();
        for (String word : words) {
            if (matchesPattern(word, mask, possibleLetters)) {
                appropriateWords.add(word);
            }
        }
        return appropriateWords;
    }


    private boolean matchesPattern(String word, StringBuilder mask, Map<Integer, ArrayList<Character>> possibleLetters) {
        for (int position = 0; position < 5; ++position) {
            if (mask.charAt(position) == '*') {
                if (!possibleLetters.get(position).contains(word.charAt(position))){
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