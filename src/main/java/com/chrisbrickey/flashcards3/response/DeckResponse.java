package com.chrisbrickey.flashcards3.response;

import java.util.*;

public class DeckResponse {
    private List<CardResponse> cards;

    public DeckResponse(List<List<String>> cardData, Random randomSeed) {
        // Set cards before shuffling so that the CardResponse.id reflects the original order of the source content.
        setCards(cardData);
        shuffleCards(randomSeed);
    }

    public List<CardResponse> getCards() { return cards; }

    // TODO: move most of this parsing logic to a DeckService
    public void setCards(List<List<String>> cardData) {
        List<CardResponse> deck = new ArrayList<>();
        for(int i=0; i< cardData.size(); i++) {
            List<String> cardLine = cardData.get(i);

            // below assumes the structure of the CSV file; using maps instead of arrays might loosen this up a bit
            String question = cardLine.get(0);
            String answer = cardLine.get(1);
            String category = cardLine.get(2);

            // TODO: add validation on content here or in a Card file
            // TODO: append i to error message to make it easier to identify location of parsing error
            int cardId = i + 1;
            CardResponse card = new CardResponse(cardId, question, answer, category);
            deck.add(card);
        }

        this.cards = deck;
    }

    private void shuffleCards(Random randomSeed) {
        Collections.shuffle(cards, randomSeed);
    }

    public static void sortCards(List<CardResponse> cards) {
        Collections.sort(cards, new CustomComparator());
    }
}

class CustomComparator implements Comparator<CardResponse> {
    @Override
    public int compare(CardResponse card1, CardResponse card2) {
        Long firstId = card1.getId();
        Long secondId = card2.getId();
        return firstId.compareTo(secondId);
    }
}