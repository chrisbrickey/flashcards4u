package com.flashcards4u.service;

import com.flashcards4u.model.Card;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class DeckService {

    public List<Card> buildDeck(String filepath, Random random) throws IOException {
        List<List<String>> rawData = readCsvFile(filepath);
        List<Card> cards = parseCards(rawData);
        shuffleCards(cards, random);
        return cards;
    }

    private List<List<String>> readCsvFile(String filepath) throws IOException {
        List<List<String>> content = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filepath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // skip header row of csv file
        String header = reader.readLine();
        if (header == null || header.isBlank()) {
            throw new IOException("CSV file is missing headers: " + filepath);
        }

        while (reader.ready()) {
            String nextLine = reader.readLine();
            List<String> cardStrings = new ArrayList<>(Arrays.asList(nextLine.split(",")));
            formatCardStrings(cardStrings);
            content.add(cardStrings);
        }

        return content;
    }

    private void formatCardStrings(List<String> unformatted) {
        for (int i = 0; i < unformatted.size(); i++) {
            String cardString = unformatted.get(i);
            if (cardString.contains("`")) {
                String formattedString = cardString.replace('`', ',');
                unformatted.set(i, formattedString);
            }
        }
    }

    private List<Card> parseCards(List<List<String>> cardData) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < cardData.size(); i++) {
            List<String> cardLine = cardData.get(i);
            String question = cardLine.get(0);
            String answer = cardLine.get(1);
            String category = cardLine.get(2);

            int cardId = i + 1;
            Card card = new Card(cardId, question, answer, category);
            cards.add(card);
        }
        return cards;
    }

    private void shuffleCards(List<Card> cards, Random random) {
        Collections.shuffle(cards, random);
    }
}
