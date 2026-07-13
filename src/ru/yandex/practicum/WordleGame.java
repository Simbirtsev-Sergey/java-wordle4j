package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.io.PrintWriter;
/*
в этом классе нужны методы, которые
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее
    не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */


public class WordleGame {

    // Правильный ответ
    private final String answer;

    // Сколько попыток было использовано
    private int steps;

    // Словарь
    private final WordleDictionary dictionary;

    // log- файл
    PrintWriter writer;

    public WordleGame(WordleDictionary dictionary, PrintWriter writer) {
        steps = 0;
        this.dictionary = dictionary;
        answer = dictionary.getTheHiddenWord(new Random().nextInt(dictionary.getLengthDictionary()));
        this.writer = writer;
    }


    // Является ли слово ответом?
    public boolean isTheWordTheAnswer(String word) {
        return answer.equals(word);
    }

    // Увеличиваем количество использованных попыток
    public void incrementSteps() {
        steps++;
    }

    // Проверка на использование кириллицы
    public boolean isCorrectInput(String wordExtended) {
        StringBuilder word = new StringBuilder(wordExtended);
        for (int letter = 0; letter < 5; ++letter) {
            if (!((int) 'а' <=  (int) word.charAt(letter) && (int) word.charAt(letter) <= 'я')) {
                return false;
            }
        }
        return true;
    }

    public String getPattern(String enteredWord) {
        Map<Character, Integer> accountingSymbols = new HashMap<>();
        StringBuilder pattern = new StringBuilder("     ");

        // Заполняем хэш- таблицу
        for (int i = 0; i < answer.length(); ++i) {
            Character character = answer.charAt(i);
            accountingSymbols.put(character, accountingSymbols.getOrDefault(character, 0) + 1);
        }

        // Проставляем в итоговом паттерне '+'
        for (int letter = 0; letter < answer.length(); ++letter) {
            if (answer.charAt(letter) == enteredWord.charAt(letter)) {
                pattern.setCharAt(letter, '+');
                accountingSymbols.put(enteredWord.charAt(letter),
                        accountingSymbols.get(enteredWord.charAt(letter)) - 1);
            }
        }

        // Проставляем в итоговом паттерне '^'
        for (int letter = 0; letter < answer.length(); ++letter) {
            if (pattern.charAt(letter) == '+') {
                continue;
            }
            if (answer.indexOf(enteredWord.charAt(letter)) >= 0 &&
                    accountingSymbols.get(enteredWord.charAt(letter)) > 0) {
                pattern.setCharAt(letter, '^');
                accountingSymbols.put(enteredWord.charAt(letter),
                        accountingSymbols.get(enteredWord.charAt(letter)) - 1);
            }
        }

        // Проставляем в итоговом паттерне '-'
        for (int letter = 0; letter < answer.length(); ++letter) {
            if (pattern.charAt(letter) == ' ') {
                pattern.setCharAt(letter, '-');
            }
        }
        return pattern.toString();
    }

    public boolean hasStepsLeft() {
        return steps < 6;
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }
}
