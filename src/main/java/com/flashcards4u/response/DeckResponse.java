package com.flashcards4u.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DeckResponse {
    private List<CardResponse> cards;

    @JsonCreator
    public DeckResponse(@JsonProperty("cards") List<CardResponse> cards) {
        this.cards = cards;
    }

    public List<CardResponse> getCards() { return cards; }
    public void setCards(List<CardResponse> cards) { this.cards = cards; }
}
