package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.wordle.WordleDictionary;
import ru.yandex.practicum.wordle.WordleGame;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleGameTest {

    private static WordleDictionary wordleDictionary;
    private static WordleGame wordleGame;

    @BeforeAll
    public static void beforeAll() {
        wordleDictionary = new WordleDictionary(List.of("гузка", "ряска", "турка", "ангоб", "башня", "ухарь", "динас",
                "дадан", "гадес", "букля"));
    }

    @BeforeEach
    public void beforeEach() {
        PrintWriter writer = new PrintWriter(System.out, true);
        wordleGame = new WordleGame(wordleDictionary, writer);
    }

    @Test
    public void checkingOnCorrectInput() {
        assertTrue(wordleGame.isCorrectInput("гузка"));
    }

    @Test
    public void checkingOnUncorrectInput() {
        // Здесь вместо русских букв 'у' и 'а' вставлены латинские буквы
        assertFalse(wordleGame.isCorrectInput("гyзкa"));
    }

    @Test
    public void patternWithPlusesAndMinuses() {
        wordleGame.setAnswer("ротор");
        assertEquals("-++++", wordleGame.getPattern("мотор"));
    }

    @Test
    public void patternWhereAllMinuses() {
        wordleGame.setAnswer("абзац");
        assertEquals("-----", wordleGame.getPattern("ротор"));
    }

    @Test
    public void patternWithPlusesAndCarets() {
        wordleGame.setAnswer("озеро");
        assertEquals("+-^--", wordleGame.getPattern("охота"));
    }

    @Test
    public void patternWithCarets() {
        wordleGame.setAnswer("озеро");
        assertEquals("--^--", wordleGame.getPattern("абзац"));
    }
}
