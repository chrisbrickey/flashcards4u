package com.flashcards4u.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    public void constructorSetsAllFields() {
        Card card = new Card(1L, "a cat", "un gato", "spanish");

        assertEquals(1L, card.getId());
        assertEquals("a cat", card.getQuestion());
        assertEquals("un gato", card.getAnswer());
        assertEquals("spanish", card.getCategory());
    }

    @Test
    public void compareToReturnsNegativeForSmallerIds() {
        Card card1 = new Card(1L, "q1", "a1", "c1");
        Card card2 = new Card(2L, "q2", "a2", "c2");

        assertTrue(card1.compareTo(card2) < 0);
    }

    @Test
    public void compareToReturnsPositiveForLargerIds() {
        Card card1 = new Card(2L, "q1", "a1", "c1");
        Card card2 = new Card(1L, "q2", "a2", "c2");

        assertTrue(card1.compareTo(card2) > 0);
    }

    @Test
    public void compareToReturnsZeroForEqualIds() {
        Card card1 = new Card(1L, "q1", "a1", "c1");
        Card card2 = new Card(1L, "q2", "a2", "c2");

        assertEquals(0, card1.compareTo(card2));
    }
}
