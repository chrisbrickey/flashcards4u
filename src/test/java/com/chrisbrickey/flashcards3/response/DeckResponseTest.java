package com.chrisbrickey.flashcards3.response;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DeckResponseTest {

    private List<List<String>> sampleCardData() {
        return Arrays.asList(
            Arrays.asList("a cat", "un gato", "spanish"),
            Arrays.asList("a cat", "un chat", "french"),
            Arrays.asList("Index into a String.", "someString.substring(0,1)", "java"),
            Arrays.asList("Create a new array of ints with a specified size of 4 elements.", "int[] myArray = new int[4]", "java")
        );
    }

    @Test
    public void constructorParsesCardData() {
        DeckResponse response = new DeckResponse(sampleCardData(), new Random(42L));
        List<CardResponse> cards = response.getCards();
        DeckResponse.sortCards(cards);

        assertEquals(4, cards.size());

        var firstCard = cards.get(0);
        assertEquals(1L, firstCard.getId());
        assertEquals("a cat", firstCard.getQuestion());
        assertEquals("un gato", firstCard.getAnswer());
        assertEquals("spanish", firstCard.getCategory());

        var secondCard = cards.get(1);
        assertEquals(2L, secondCard.getId());
        assertEquals("a cat", secondCard.getQuestion());
        assertEquals("un chat", secondCard.getAnswer());
        assertEquals("french", secondCard.getCategory());

        var thirdCard = cards.get(2);
        assertEquals(3L, thirdCard.getId());
        assertEquals("Index into a String.", thirdCard.getQuestion());
        assertEquals("someString.substring(0,1)", thirdCard.getAnswer());
        assertEquals("java", thirdCard.getCategory());
    }

    @Test
    public void constructorAssigns1BasedIds() {
        DeckResponse response = new DeckResponse(sampleCardData(), new Random(42L));
        List<CardResponse> cards = response.getCards();
        DeckResponse.sortCards(cards);

        for (int i = 0; i < cards.size(); i++) {
            assertEquals(i + 1, cards.get(i).getId());
        }
    }

    @Test
    public void constructorShufflesCards() {
        DeckResponse response = new DeckResponse(sampleCardData(), new Random(42L));
        List<CardResponse> cards = response.getCards();

        // After shuffling, the first card should not still be id=1
        assertNotEquals(1L, cards.get(0).getId());
    }

    @Test
    public void setCardsReplacesExistingCards() {
        DeckResponse response = new DeckResponse(sampleCardData(), new Random(42L));

        List<List<String>> newData = Arrays.asList(
            Arrays.asList("hello", "hola", "spanish")
        );
        response.setCards(newData);

        assertEquals(1, response.getCards().size());
        assertEquals("hello", response.getCards().get(0).getQuestion());
    }

    @Test
    public void setCardsWithEmptyList() {
        DeckResponse response = new DeckResponse(sampleCardData(), new Random(42L));

        response.setCards(new ArrayList<>());

        assertEquals(0, response.getCards().size());
    }

    @Test
    public void setCardsWithSingleCard() {
        List<List<String>> singleCard = Arrays.asList(
            Arrays.asList("hello", "hola", "spanish")
        );
        DeckResponse response = new DeckResponse(singleCard, new Random(42L));
        List<CardResponse> cards = response.getCards();

        assertEquals(1, cards.size());
        assertEquals(1L, cards.get(0).getId());
    }

    @Test
    public void sortCardsOrdersByIdAscending() {
        List<CardResponse> cards = Arrays.asList(
            new CardResponse(3L, "q3", "a3", "c3"),
            new CardResponse(1L, "q1", "a1", "c1"),
            new CardResponse(2L, "q2", "a2", "c2")
        );

        DeckResponse.sortCards(cards);

        assertEquals(1L, cards.get(0).getId());
        assertEquals(2L, cards.get(1).getId());
        assertEquals(3L, cards.get(2).getId());
    }

    @Test
    public void sortCardsWithEmptyList() {
        List<CardResponse> cards = new ArrayList<>();

        assertDoesNotThrow(() -> DeckResponse.sortCards(cards));
        assertEquals(0, cards.size());
    }
}
