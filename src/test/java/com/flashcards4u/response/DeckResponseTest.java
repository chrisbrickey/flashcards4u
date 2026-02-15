package com.flashcards4u.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckResponseTest {

    @Test
    public void constructorStoresCards() {
        List<CardResponse> cards = List.of(
            new CardResponse(1L, "a", "b", "c"),
            new CardResponse(2L, "x", "y", "z")
        );
        DeckResponse response = new DeckResponse(cards);

        assertEquals(2, response.getCards().size());
        assertEquals(1L, response.getCards().get(0).getId());
        assertEquals(2L, response.getCards().get(1).getId());
    }

    @Test
    public void constructorWithEmptyListSucceeds() {
        DeckResponse response = new DeckResponse(List.of());

        assertEquals(0, response.getCards().size());
    }

    @Test
    public void setCardsReplacesExistingCards() {
        DeckResponse response = new DeckResponse(List.of(
            new CardResponse(1L, "a cat", "un chat", "french")
        ));

        List<CardResponse> newCards = List.of(
            new CardResponse(1L, "hello", "bonjour", "french")
        );
        response.setCards(newCards);

        assertEquals(1, response.getCards().size());
        assertEquals("hello", response.getCards().get(0).getQuestion());
    }
}
