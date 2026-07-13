package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    PrintWriter writer;

    public WordleDictionaryLoader(final PrintWriter writer) {
        this.writer = writer;
    }

    public List<String> getWordsFromFile(String nameFile) {
        final List<String> words = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nameFile))) {
            while (bufferedReader.ready()) {
                String word = bufferedReader.readLine();
                if (word.length() == 5) {
                    word = word.toLowerCase().replace('ё', 'е');
                    words.add(word);
                }
            }
        } catch (IOException exception) {
            writer.write("Произошла ошибка при чтении файла " + nameFile);
        }
        return words;
    }
} // Может просто передать PrintWriter в метод?