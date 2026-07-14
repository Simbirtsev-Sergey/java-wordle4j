package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class HintTest {
    private static Hint hint;
    private static WordleDictionary wordleDictionary;


    @BeforeEach
    public void beforeEach() {
        wordleDictionary = new WordleDictionary(List.of("гузка", "ряска", "турка", "ангоб", "башня", "ухарь", "динас",
                "дадан", "гадес", "букля", "охота"));
        // Загаданное слово - "охота"
    }

    @Test
    public void buildMaskFillsKnownLettersAndIgnoresCaret() {
        hint = new Hint(wordleDictionary, List.of("---++", "+---^"), List.of("карта", "озеро"));
        StringBuilder mask = new StringBuilder("*****");
        hint.buildMask(mask);
        assertEquals("о**та", mask.toString());
    }

    @Test
    public void buildMaskFillsKnownLettersAndCaret() {
        hint = new Hint(wordleDictionary, List.of("-^---"), List.of("город"));
        StringBuilder mask = new StringBuilder("*****");
        hint.buildMask(mask);
        assertEquals("*****", mask.toString());
    }

    @Test
    public void buildMaskFillsOnlyKnownLetters() {
        hint = new Hint(wordleDictionary, List.of("+++++"), List.of("охота"));
        StringBuilder mask = new StringBuilder("*****");
        hint.buildMask(mask);
        assertEquals("охота", mask.toString());
    }

    @Test
    public void listWithRussianLetters_contains32Letters() {
        hint = new Hint(wordleDictionary, List.of("+++++"), List.of("охота"));
        List<Character> letters = hint.listWithRussianLetters();

        assertEquals(32, letters.size());
        assertEquals('а', letters.getFirst());
        assertEquals('я', letters.getLast());
        assertFalse(letters.contains('ё'));
        assertTrue(letters.contains('о'));
    }

    @Test
    void fillInEachPosition_allUnknown_everyPositionGetsFullAlphabet() {
        Hint hint = new Hint(wordleDictionary, List.of(), List.of()); // аргументы конструктора тут не важны
        Map<Integer, ArrayList<Character>> possibleLetters = new HashMap<>();
        StringBuilder mask = new StringBuilder("*****");
        List<Character> letters = List.of('а', 'б', 'в');

        hint.fillInEachPosition(possibleLetters, mask, letters);

        assertEquals(5, possibleLetters.size());               // ключи для всех 5 позиций
        for (int i = 0; i < 5; i++) {
            assertEquals(List.of('а', 'б', 'в'), possibleLetters.get(i)); // каждая позиция — полный алфавит
        }
    }

    @Test
    void fillInEachPosition_allKnown_everyPositionIsEmpty() {
        Hint hint = new Hint(wordleDictionary, List.of(), List.of());
        Map<Integer, ArrayList<Character>> possibleLetters = new HashMap<>();
        StringBuilder mask = new StringBuilder("карта");        // ни одной '*'
        List<Character> letters = List.of('а', 'б', 'в');

        hint.fillInEachPosition(possibleLetters, mask, letters);

        for (int i = 0; i < 5; i++) {
            assertTrue(possibleLetters.get(i).isEmpty());       // всё известно → пустые списки
        }
    }

    @Test
    void fillInEachPosition_mixedMask_knownEmpty_unknownFull() {
        Hint hint = new Hint(wordleDictionary, List.of(), List.of());
        Map<Integer, ArrayList<Character>> possibleLetters = new HashMap<>();
        StringBuilder mask = new StringBuilder("о**та");        // известны позиции 0, 3, 4
        List<Character> letters = List.of('а', 'б', 'в');

        hint.fillInEachPosition(possibleLetters, mask, letters);

        assertTrue(possibleLetters.get(0).isEmpty());           // 'о' — известна
        assertTrue(possibleLetters.get(3).isEmpty());           // 'т'
        assertTrue(possibleLetters.get(4).isEmpty());           // 'а'
        assertEquals(List.of('а', 'б', 'в'), possibleLetters.get(1)); // '*' — весь алфавит
        assertEquals(List.of('а', 'б', 'в'), possibleLetters.get(2));
    }

    @Test
    void fillInEachPosition_positionsAreIndependentCopies() {
        Hint hint = new Hint(wordleDictionary, List.of(), List.of());
        Map<Integer, ArrayList<Character>> possibleLetters = new HashMap<>();
        StringBuilder mask = new StringBuilder("*****");
        List<Character> letters = new ArrayList<>(List.of('а', 'б', 'в'));

        hint.fillInEachPosition(possibleLetters, mask, letters);

        // меняем список ОДНОЙ позиции
        possibleLetters.get(0).remove(Character.valueOf('а'));

        // другие позиции и исходный алфавит не пострадали, следовательно это были копии
        assertTrue(possibleLetters.get(1).contains('а'));
        assertTrue(letters.contains('а'));
    }

    @Test
    void excludeLetterEverywhere_minusRemovesLetterFromAllPositions() {
        // '-' на 'а' (позиция 0) должен убрать 'а' из списков всех позиций
        Hint hint = new Hint(wordleDictionary, List.of("-++++"), List.of("абвгд"));
        Map<Integer, ArrayList<Character>> possibleLetters = smallAlphabetMap();

        hint.excludeLetterEverywhere(possibleLetters);

        for (int i = 0; i < 5; i++) {
            assertFalse(possibleLetters.get(i).contains('а'), "'а' должна быть удалена с позиции " + i);
        }
        assertTrue(possibleLetters.get(0).contains('б'));
    }

    @Test
    void excludeLetterEverywhere_caretRemovesLetterOnlyFromThatPosition() {
        // '^' на 'а' (позиция 0) убирает 'а' только с позиции 0, на остальных она остаётся
        Hint hint = new Hint(wordleDictionary, List.of("^++++"), List.of("абвгд"));
        Map<Integer, ArrayList<Character>> possibleLetters = smallAlphabetMap();

        hint.excludeLetterEverywhere(possibleLetters);

        assertFalse(possibleLetters.get(0).contains('а')); // со своей позиции удалена
        assertTrue(possibleLetters.get(1).contains('а'));  // на других осталась
        assertTrue(possibleLetters.get(4).contains('а'));
    }

    @Test
    void filteringWords_keepsOnlyWordsMatchingMask() {
        // маска "о**та": известны позиции 0, 3, 4 — проходят только слова с о_?_?_т_а
        Hint hint = new Hint(wordleDictionary, List.of(), List.of());
        StringBuilder mask = new StringBuilder("о**та");
        Map<Integer, ArrayList<Character>> possibleLetters = fullAlphabetMap(hint); // на '*' любые буквы
        List<String> words = List.of("охота", "карта", "мышка");
        List<String> appropriateWords = new ArrayList<>();

        hint.filteringWords(words, mask, possibleLetters, appropriateWords);

        assertEquals(List.of("охота"), appropriateWords);
    }

    @Test
    void matchesPattern_returnsTrueWhenKnownAndAllowedLettersFit() {
        Hint hint = new Hint(wordleDictionary, List.of(), List.of());
        StringBuilder mask = new StringBuilder("о**та");
        Map<Integer, ArrayList<Character>> possibleLetters = fullAlphabetMap(hint);

        assertTrue(hint.matchesPattern("охота", mask, possibleLetters));
    }

    @Test
    void matchesPattern_returnsFalseWhenGreenPositionMismatch() {
        // на позиции 0 маска требует 'о', а в слове стоит 'к'
        Hint hint = new Hint(wordleDictionary, List.of(), List.of());
        StringBuilder mask = new StringBuilder("о**та");
        Map<Integer, ArrayList<Character>> possibleLetters = fullAlphabetMap(hint);

        assertFalse(hint.matchesPattern("карта", mask, possibleLetters));
    }

    @Test
    void matchesPattern_returnsFalseWhenLetterNotAllowedAtUnknownPosition() {
        // все позиции неизвестны, но 'к' запрещена на позиции 0
        Hint hint = new Hint(wordleDictionary, List.of(), List.of());
        StringBuilder mask = new StringBuilder("*****");
        Map<Integer, ArrayList<Character>> possibleLetters = fullAlphabetMap(hint);
        possibleLetters.get(0).remove(Character.valueOf('к'));

        assertFalse(hint.matchesPattern("карта", mask, possibleLetters)); // 'к' на позиции 0 не разрешена
        assertTrue(hint.matchesPattern("барка", mask, possibleLetters));  // 'б' на позиции 0 разрешена
    }

    @Test
    void matchesPattern_returnsFalseWhenWordMissingRequiredPresentLetter() {
        // '^' на 'с' в слове "слива" делает 'с' обязательной в слове-кандидате
        Hint hint = new Hint(wordleDictionary, List.of("^----"), List.of("слива"));
        // прогоняем excludeLetterEverywhere только чтобы заполнить список обязательных букв ('с')
        hint.excludeLetterEverywhere(fullAlphabetMap(hint));

        StringBuilder mask = new StringBuilder("*****");
        Map<Integer, ArrayList<Character>> possibleLetters = fullAlphabetMap(hint);

        assertFalse(hint.matchesPattern("карта", mask, possibleLetters)); // нет 'с'
        assertTrue(hint.matchesPattern("весна", mask, possibleLetters));  // есть 'с'
    }

    @Test
    void generationHint_allPlus_returnsExactWord() {
        // все буквы угаданы на своих местах → подходит ровно одно слово словаря
        Hint hint = new Hint(wordleDictionary, List.of("+++++"), List.of("охота"));

        assertEquals(List.of("охота"), hint.generationHint());
    }

    @Test
    void generationHint_filtersByMaskPresentAndExcludedLetters() {
        // "улика" + "^--++": маска "***ка", 'у' обязательна (^), 'л' и 'и' исключены (-)
        // из словаря подходят слова, оканчивающиеся на "ка" и содержащие 'у': гузка, турка
        Hint hint = new Hint(wordleDictionary, List.of("^--++"), List.of("улика"));

        assertEquals(List.of("гузка", "турка"), hint.generationHint());
    }

    // Вспомогательные методы

    // 5 позиций, на каждой — полный русский алфавит (независимые копии)
    private Map<Integer, ArrayList<Character>> fullAlphabetMap(Hint hint) {
        Map<Integer, ArrayList<Character>> map = new HashMap<>();
        List<Character> alphabet = hint.listWithRussianLetters();
        for (int i = 0; i < 5; i++) {
            map.put(i, new ArrayList<>(alphabet));
        }
        return map;
    }

    // 5 позиций, на каждой — маленький алфавит [а, б, в] для наглядных проверок
    private Map<Integer, ArrayList<Character>> smallAlphabetMap() {
        Map<Integer, ArrayList<Character>> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put(i, new ArrayList<>(List.of('а', 'б', 'в')));
        }
        return map;
    }

}
