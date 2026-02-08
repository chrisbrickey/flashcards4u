package com.flashcards4u.response;

import java.util.List;

public class DeckResponse {
    private List<CardResponse> cards;

    public DeckResponse(List<CardResponse> cards) {
        this.cards = cards;
    }

    public List<CardResponse> getCards() { return cards; }
    public void setCards(List<CardResponse> cards) { this.cards = cards; }
}
