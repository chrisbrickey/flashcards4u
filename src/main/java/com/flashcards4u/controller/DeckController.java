package com.flashcards4u.controller;

import com.flashcards4u.model.Card;
import com.flashcards4u.response.CardResponse;
import com.flashcards4u.response.DeckResponse;
import com.flashcards4u.service.DeckService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@RestController
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @RequestMapping(value="/v1/deck", method=RequestMethod.GET)
    public DeckResponse getDeck(@RequestParam String filepath) throws IOException {
        List<Card> cards = deckService.buildDeck(filepath, new Random());
        List<CardResponse> cardResponses = cards.stream()
            .map(CardResponse::fromCard)
            .toList();
        return new DeckResponse(cardResponses);
    }
}
