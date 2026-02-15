package com.flashcards4u.service;

import com.flashcards4u.model.Card;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DeckServiceTest {

    private final DeckService service = new DeckService();

    @Test
    public void buildDeckParsesCardsCorrectly() throws Exception {
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
    public void buildDeckAssigns1BasedIds() throws Exception {
        List<Card> cards = service.buildDeck("static/csv/multiple_categories.csv", new Random(42L));
        Collections.sort(cards);

        for (int i = 0; i < cards.size(); i++) {
            assertEquals(i + 1, cards.get(i).getId());
        }
    }

    @Test
    public void buildDeckShufflesCards() throws Exception {
        List<Card> cards = service.buildDeck("static/csv/multiple_categories.csv", new Random(42L));

        // After shuffling, the first card should not still be id=1
        assertNotEquals(1L, cards.get(0).getId());
    }

    @Test
    public void buildDeckWithSingleCardSucceeds() throws Exception {
        List<Card> cards = service.buildDeck("static/csv/single_card.csv", new Random(42L));

        assertEquals(1, cards.size());
        assertEquals("demanding", cards.get(0).getQuestion());
        assertEquals("exigeant(e)", cards.get(0).getAnswer());
        assertEquals("french", cards.get(0).getCategory());
    }

    @Test
    public void buildDeckReplacesBacktickWithComma() throws Exception {
        List<Card> cards = service.buildDeck("static/csv/backticks.csv", new Random(42L));
        Collections.sort(cards);

        assertEquals(2, cards.size());

        var firstCard = cards.get(0);
        var expectedQuestion  = "Given a regular array, create a NumPy array of training examples.";
        assertEquals(expectedQuestion, firstCard.getQuestion());

        var secondCard = cards.get(1);
        var expectedAnswer = "x = numpy.array([1.0, 2.5, -3])";
        assertEquals(expectedAnswer, secondCard.getAnswer());
    }

    @Test
    public void buildDeckWithNoCardsSucceeds() throws Exception {
        List<Card> cards = service.buildDeck("static/csv/no_cards.csv", new Random(42L));

        assertEquals(0, cards.size());
    }

    @Test
    public void buildDeckWithNoHeadersFails() {
        String noHeaderFile = "no_headers.csv";
        String path = "static/csv/" + noHeaderFile;

        IOException exception = assertThrows(IOException.class,
                () -> service.buildDeck(path, new Random(42L)));
        assertTrue(exception.getMessage().contains(noHeaderFile));
    }

    @Test
    public void buildDeckThrowsExceptionForMissingFile() {
        String nonexistantFile = "nonexistent.csv";
        String path = "static/csv/" + nonexistantFile;

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> service.buildDeck(path, new Random(42L)));
    }

    @Test
    public void buildDeckProducesDifferentOrdersWithDifferentSeeds() throws Exception {
        List<Card> cards1 = service.buildDeck("static/csv/multiple_categories.csv", new Random(42L));
        List<Card> cards2 = service.buildDeck("static/csv/multiple_categories.csv", new Random(99L));

        List<Long> ids1 = cards1.stream().map(Card::getId).toList();
        List<Long> ids2 = cards2.stream().map(Card::getId).toList();

        assertNotEquals(ids1, ids2, "Different seeds should produce different order of cards.");
    }
}
