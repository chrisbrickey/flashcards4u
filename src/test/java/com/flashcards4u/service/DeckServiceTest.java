package com.flashcards4u.service;

import com.flashcards4u.exception.CsvFileNotFoundException;
import com.flashcards4u.exception.CsvParsingException;
import com.flashcards4u.model.Card;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DeckServiceTest {

    private final DeckService service = new DeckService();

    // HAPPY PATH TESTS

    @Test
    public void buildDeckParsesCardsCorrectly() {
        List<Card> cards = service.buildDeck("static/csv/multiple_categories.csv", new Random(42L));
        Collections.sort(cards);

        assertEquals(5, cards.size());

        var firstCard = cards.get(0);
        assertEquals("a cat", firstCard.getQuestion());
        assertEquals("un chat", firstCard.getAnswer());
        assertEquals("french", firstCard.getCategory());

        var secondCard = cards.get(1);
        assertEquals("a cat", secondCard.getQuestion());
        assertEquals("un gato", secondCard.getAnswer());
        assertEquals("spanish", secondCard.getCategory());
    }

    @Test
    public void buildDeckAssignsIdsThatMatchLineNumbersOnCSV() {
        List<Card> cards = service.buildDeck("static/csv/multiple_categories.csv", new Random(42L));
        Collections.sort(cards);

        for (int i = 0; i < cards.size(); i++) {
            assertEquals(i + 2, cards.get(i).getId());
        }
    }

    @Test
    public void buildDeckReplacesBacktickWithComma() {
        List<Card> cards = service.buildDeck("static/csv/backticks.csv", new Random(42L));
        Collections.sort(cards);

        assertEquals(2, cards.size());

        var firstCard = cards.get(0);
        var expectedQuestion = "Given a regular array, create a NumPy array of training examples.";
        assertEquals(expectedQuestion, firstCard.getQuestion());

        var secondCard = cards.get(1);
        var expectedAnswer = "x = numpy.array([1.0, 2.5, -3])";
        assertEquals(expectedAnswer, secondCard.getAnswer());
    }

    @Test
    public void buildDeckWithSingleCardSucceeds() {
        List<Card> cards = service.buildDeck("static/csv/single_card.csv", new Random(42L));

        assertEquals(1, cards.size());
        assertEquals("demanding", cards.get(0).getQuestion());
        assertEquals("exigeant(e)", cards.get(0).getAnswer());
        assertEquals("french", cards.get(0).getCategory());
    }

    @Test
    public void buildDeckShufflesCards() {
        List<Card> cards = service.buildDeck("static/csv/multiple_categories.csv", new Random(42L));

        // After shuffling, the first card should not still be id = 2 (first content line of CSV)
        assertNotEquals(2L, cards.get(0).getId());
    }

    @Test
    public void buildDeckProducesDifferentOrdersWithDifferentSeeds() {
        List<Card> cards1 = service.buildDeck("static/csv/multiple_categories.csv", new Random(42L));
        List<Card> cards2 = service.buildDeck("static/csv/multiple_categories.csv", new Random(99L));

        List<Long> ids1 = cards1.stream().map(Card::getId).toList();
        List<Long> ids2 = cards2.stream().map(Card::getId).toList();

        assertNotEquals(ids1, ids2, "Different seeds should produce different order of cards.");
    }

    @Test
    public void buildDeckIgnoresExtraColumns() {
        List<Card> cards = service.buildDeck("static/csv/extra_columns.csv", new Random(42L));
        Collections.sort(cards);

        assertEquals(2, cards.size());

        var firstCard = cards.get(0);
        assertEquals("a cat", firstCard.getQuestion());
        assertEquals("un chat", firstCard.getAnswer());
        assertEquals("french", firstCard.getCategory());

        var secondCard = cards.get(1);
        assertEquals("a dog", secondCard.getQuestion());
        assertEquals("un chien", secondCard.getAnswer());
        assertEquals("french", secondCard.getCategory());
    }

    @Test
    public void buildDeckAllowsSingleQuotesInCardData() {
        List<Card> cards = service.buildDeck("static/csv/single_quotes.csv", new Random(42L));

        assertEquals(1, cards.size());
        assertEquals("exigeant(e); pronounced 'ehk-see-joh(T)'", cards.get(0).getAnswer());
    }

    // ERROR HANDLING TESTS

    @Test
    public void buildDeckWithMissingFileFails() {
        String nonexistantFile = "nonexistent.csv";
        String path = "static/csv/" + nonexistantFile;

        CsvFileNotFoundException exception = assertThrows(CsvFileNotFoundException.class,
                () -> service.buildDeck(path, new Random(42L)));
        assertTrue(exception.getMessage().contains(nonexistantFile));
    }

    @Test
    public void buildDeckWithEmptyFileFails() {
        String emptyFile = "empty.csv";
        String path = "static/csv/" + emptyFile;

        CsvParsingException exception = assertThrows(CsvParsingException.class,
                () -> service.buildDeck(path, new Random(42L)));
        assertTrue(exception.getMessage().contains(emptyFile));
    }

    @Test
    public void buildDeckWithNoCardsFails() {
        String noCardsFile = "no_cards.csv";
        String path = "static/csv/" + noCardsFile;

        CsvParsingException exception = assertThrows(CsvParsingException.class,
                () -> service.buildDeck(path, new Random(42L)));
        assertTrue(exception.getMessage().contains(noCardsFile));
    }

    @Test
    public void buildDeckWithNoHeadersFails() {
        String noHeaderFile = "no_headers.csv";
        String path = "static/csv/" + noHeaderFile;

        CsvParsingException exception = assertThrows(CsvParsingException.class,
                () -> service.buildDeck(path, new Random(42L)));
        assertTrue(exception.getMessage().contains(noHeaderFile));
    }

    @Test
    public void buildDeckWithMalformedRowFails() {
        CsvParsingException exception = assertThrows(CsvParsingException.class,
                () -> service.buildDeck("static/csv/malformed_row.csv", new Random(42L)));
        assertTrue(exception.getMessage().contains("Malformed row 3: expected at least 3 columns but found 1"));
    }

    @Test
    public void buildDeckWithDoubleQuotesInCardDataFails() {
        CsvParsingException exception = assertThrows(CsvParsingException.class,
                () -> service.buildDeck("static/csv/double_quotes.csv", new Random(42L)));
        assertTrue(exception.getMessage().contains("Forbidden character \" found on line 1; content: \"cat\""));
    }

    @Test
    public void buildDeckWithLeftAngleBracketsInCardDataFails() {
        CsvParsingException exception = assertThrows(CsvParsingException.class,
                () -> service.buildDeck("static/csv/left_angle_brackets.csv", new Random(42L)));
        assertTrue(exception.getMessage().contains("Forbidden character < found on line 2; content: List<List"));

    }

    @Test
    public void buildDeckWithRightAngleBracketsInCardDataFails() {
        CsvParsingException exception = assertThrows(CsvParsingException.class,
                () -> service.buildDeck("static/csv/right_angle_brackets.csv", new Random(42L)));
        assertTrue(exception.getMessage().contains("Forbidden character > found on line 2; content: someArrayList.stream().filter(e ->"));
    }
}
