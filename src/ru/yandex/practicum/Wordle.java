package ru.yandex.practicum;

import ru.yandex.practicum.exception.WordEnteredInWrongLanguage;
import ru.yandex.practicum.exception.WordNoFitTheSize;
import ru.yandex.practicum.exception.WordNotFoundInDictionary;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Wordle {
    private static final String FILE_NAME = "words_ru.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isGameOver = false;
    private static final ArrayList<String> patterns = new ArrayList<>();
    private static final ArrayList<String> listInputWords = new ArrayList<>();

    public static void main(String[] args) {

        // Создать лог- файл
        try (PrintWriter writer = new PrintWriter(new FileWriter("wordle.log", true))) {

            try {
                // Создать загрузчик словаря WordleDictionaryLoader
                WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader(writer);

                // Загрузить словарь WordleDictionary
                WordleDictionary wordleDictionary = new WordleDictionary(wordleDictionaryLoader, FILE_NAME);

                // Создать игру
                WordleGame wordleGame = new WordleGame(wordleDictionary, writer);

                // Ответ УДАЛИТЬ!!!!!!!!
                System.out.println("Ответ: " + wordleGame.getAnswer());

                // Вызвать игровой метод
                game(wordleGame, wordleDictionary, writer, patterns);

                // Вывести результат
                resultGame(wordleGame);
            } catch (Exception exception) {
                System.out.println("Не удалось открыть файл\n");
            }

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void game(WordleGame wordleGame, WordleDictionary wordleDictionary, final PrintWriter writer,
                            ArrayList<String> patterns) {
        while (wordleGame.hasStepsLeft()) {
            String word;
            System.out.println("Введите слово: ");
            try {
              //  word = scanner.nextLine();

                while ((word = scanner.nextLine()).isBlank()) {

                    if (word.isBlank()) {
                        Hint hint = new Hint(wordleDictionary, patterns, listInputWords);
                        List<String> possibleLetters = hint.generationHint();
                        System.out.println(possibleLetters);
                    } else {
                        if (word.length() != 5) {
                            throw new WordNoFitTheSize("Введенное слово не состоит из 5 букв", word);
                        }

                        if (!wordleGame.isCorrectInput(word)) {
                            throw new WordEnteredInWrongLanguage("Вы ввели недопустимые символы", word);
                        }

                        if (!wordleDictionary.isWordInDictionary(word)) {
                            throw new WordNotFoundInDictionary("Данного слова нет в словаре", word);
                        }

                        wordleGame.incrementSteps();
                        listInputWords.add(word);

                        if (wordleGame.isTheWordTheAnswer(word)) {
                            isGameOver = true;
                            return;
                        }
                        String pattern = wordleGame.getPattern(word);
                        System.out.println(pattern);
                        patterns.add(pattern);
                    }
                }

            } catch (WordEnteredInWrongLanguage exception) {
                System.out.println(exception.getMessage());
                writer.write("Слово " + "\"" + exception.getWord()
                        + "\" не подходит: Введено латиницей, ожидается кириллица\n");
            } catch (WordNotFoundInDictionary exception) {
                System.out.println(exception.getMessage());
                writer.write("Слово " + "\"" + exception.getWord() + "\" не найдено в словаре\n");
            } catch (WordNoFitTheSize exception) {
                System.out.println(exception.getMessage());
                writer.write("Слово " + "\"" + exception.getWord() + "\" не подходит: длина "
                        + exception.getWord().length() + " одидалось 5\n");
            }
        }
    }


    public static void resultGame(WordleGame wordleGame) {
        if (isGameOver) {
            System.out.println("Вы угадали слово!");
            System.out.println(String.format("Вам понадобилось %d попыток", wordleGame.getSteps()));
        } else {
            System.out.println("Вы проиграли");
            System.out.println(String.format("Загаданное слово: %s", wordleGame.getAnswer()));
        }
    }
}
