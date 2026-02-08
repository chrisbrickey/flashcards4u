package com.chrisbrickey.flashcards3;

import static org.junit.jupiter.api.Assertions.assertEquals;
<<<<<<< Updated upstream
import com.chrisbrickey.flashcards3.controller.DeckController;
import com.chrisbrickey.flashcards3.response.DeckResponse;
=======
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.chrisbrickey.flashcards3.model.Card;
import com.chrisbrickey.flashcards3.service.DeckService;
>>>>>>> Stashed changes
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

<<<<<<< Updated upstream
=======
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

>>>>>>> Stashed changes
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

//    TODO: extract reading of csv file into memory to a DeckService and test using that service
    private boolean canParseCsv(String filepath) {
        try {
            DeckController controller = new DeckController();
            DeckResponse response = controller.getDeck(filepath);
            response.getCards();
        } catch (Exception e){
          // TODO: surface the line number where parsing error was encountered (from DeckResponse.setCards)
          return false;
        }

        return true;
    }
}
