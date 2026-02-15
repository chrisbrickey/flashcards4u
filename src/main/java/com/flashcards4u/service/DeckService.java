package com.flashcards4u.service;

import com.flashcards4u.exception.CsvFileNotFoundException;
import com.flashcards4u.exception.CsvParsingException;
import com.flashcards4u.model.Card;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class DeckService {

    private static final Pattern FORBIDDEN_CHARACTERS = Pattern.compile("[\"<>]");

    public List<Card> buildDeck(String filepath, Random random) {
        List<List<String>> rawData = readCsvFile(filepath);
        List<Card> cards = constructCards(rawData);
        shuffleCards(cards, random);
        return cards;
    }

    private List<List<String>> readCsvFile(String filepath) {
        List<List<String>> content = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filepath);

        if (inputStream == null) {
            throw new CsvFileNotFoundException(filepath);
        }

        int lineNumber = 1; // csv files are 1-indexed
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            // skip header row of csv file
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                throw new CsvParsingException("CSV file is missing headers: " + filepath);
            }

            // parse all remaining rows
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                lineNumber++;
                List<String> cardStrings = new ArrayList<>(Arrays.asList(nextLine.split(",")));
                formatCardStrings(cardStrings);
                content.add(cardStrings);
            }
        } catch (IOException e) {
            throw new CsvParsingException("Error reading CSV file: " + filepath + " at line " + lineNumber);
        }

        if (content.isEmpty()) {
            throw new CsvParsingException("CSV file has headers but no content: " + filepath);
        }

        return content;
    }

    private void formatCardStrings(List<String> unformatted) {
        for (int i = 0; i < unformatted.size(); i++) {
            String cardString = unformatted.get(i);
            if (cardString.contains("`")) {
                String formattedString = cardString.replace('`', ',');
                unformatted.set(i, formattedString);
                cardString = formattedString;
            }

            var matcher = FORBIDDEN_CHARACTERS.matcher(cardString);
            if (matcher.find()) {
                throw new CsvParsingException(
                        // header row not included so CSV line number is only i + 1 instead of i + 2
                        "Forbidden character " + matcher.group() + " found on line " + (i + 1) + "; content: " + cardString);
            }
        }
    }

    private List<Card> constructCards(List<List<String>> cardData) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < cardData.size(); i++) {
            List<String> cardLine = cardData.get(i);

            // cardId should align with csv row and header already skipped
            int cardId = i + 2;

            // fail if minimum fields are not found; additional fields are ignored
            if (cardLine.size() < 3) {
                throw new CsvParsingException("Malformed row " + (cardId) + ": expected at least 3 columns but found " + cardLine.size());
            }

            String question = cardLine.get(0);
            String answer = cardLine.get(1);
            String category = cardLine.get(2);

            Card card = new Card(cardId, question, answer, category);
            cards.add(card);
        }
        return cards;
    }

    private void shuffleCards(List<Card> cards, Random random) {
        Collections.shuffle(cards, random);
    }
}
