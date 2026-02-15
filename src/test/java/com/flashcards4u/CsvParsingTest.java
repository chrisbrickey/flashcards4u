package com.flashcards4u;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.flashcards4u.exception.CsvFileNotFoundException;
import com.flashcards4u.exception.CsvParsingException;
import com.flashcards4u.model.Card;
import com.flashcards4u.service.DeckService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvParsingTest {

    private static final Logger logger = LoggerFactory.getLogger(CsvParsingTest.class);

    @Test
    public void ensureAllCsvFilesCanBeParsed() throws Exception {
        Path csvDir = Paths.get("src/main/resources/static/csv");
        List<String> filepaths;
        try (Stream<Path> paths = Files.list(csvDir)) {
            filepaths = paths
                    .filter(p -> p.toString().endsWith(".csv"))
                    .map(p -> "static/csv/" + p.getFileName().toString())
                    .collect(Collectors.toList());
        }

        assertFalse(filepaths.isEmpty(), "Expected at least one CSV file in static/csv/");

        logger.info("CSV files to test: {}", filepaths);
        for (String filepath : filepaths) {
            logger.info("Testing CSV parsing: {}", filepath);
            assertEquals(true, canParseCsv(filepath), "Failed to parse: " + filepath);
        }
    }

    private boolean canParseCsv(String filepath) {
        try {
            DeckService service = new DeckService();
            List<Card> cards = service.buildDeck(filepath, new Random());
            cards.size();
        } catch (CsvFileNotFoundException | CsvParsingException e) {
            logger.warn("Failed to parse CSV file: {}", filepath, e);
            return false;
        }

        return true;
    }
}
