package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.wordle.WordleDictionary;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryTest {

    private static WordleDictionary wordleDictionary;
    @BeforeEach
    public void beforeEach() {
        wordleDictionary = new WordleDictionary(List.of("гузка", "ряска", "турка", "ангоб", "башня", "ухарь", "динас",
                "дадан", "гадес", "букля"));
    }

    @Test
    public void checkingReturnLength() {
        assertEquals(10, wordleDictionary.getLengthDictionary());
    }

    @Test
    public void returnWordOnIndex() {
        assertEquals("турка", wordleDictionary.getTheHiddenWord(2));
    }

    @Test
    public void hasDictionaryThisWord() {
        assertFalse(wordleDictionary.isWordInDictionary("аборт"));
    }

    @Test
    public void noHasDictionaryThisWord() {
        assertTrue(wordleDictionary.isWordInDictionary("башня"));
    }
}