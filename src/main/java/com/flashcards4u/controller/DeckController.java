package com.flashcards4u.controller;

import com.flashcards4u.exception.DeckNotFoundException;
import com.flashcards4u.model.Card;
import com.flashcards4u.response.CardResponse;
import com.flashcards4u.response.DeckResponse;
import com.flashcards4u.service.DeckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
public class DeckController {

    private static final Map<String, String> DECK_PATHS = Map.of(
        "english-to-french", "static/csv/english_to_french.csv"
    );

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("/v1/decks/{deckName}")
    public DeckResponse getDeck(@PathVariable String deckName) {
        String filepath = DECK_PATHS.get(deckName);
        if (filepath == null) {
            throw new DeckNotFoundException(deckName);
        }

        List<Card> cards = deckService.buildDeck(filepath, new Random());
        List<CardResponse> cardResponses = cards.stream()
            .map(CardResponse::fromCard)
            .toList();
        return new DeckResponse(cardResponses);
    }
}
