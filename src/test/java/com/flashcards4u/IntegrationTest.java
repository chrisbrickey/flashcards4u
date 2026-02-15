package com.flashcards4u;

import com.flashcards4u.controller.RootController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RootController controller;

    @Test
    public void contextLoadsForSmokeTest() throws Exception {
      assertThat(controller).isNotNull();
    }

    @Test
    public void rootSpinsUpServerOnRandomPortAndDisplaysContent() throws Exception {
      assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class))
        .contains("flashcards4u");
    }

    @Test
    public void allDeckUrlsOnFrontendRouteToValidContentOnBackend() throws Exception {
        String sourceJs = new ClassPathResource("static/source.js").getContentAsString(StandardCharsets.UTF_8);

        Pattern pattern = Pattern.compile("/v1/decks/([\\w-]+)");
        Matcher matcher = pattern.matcher(sourceJs);

        List<String> deckPaths = new ArrayList<>();
        while (matcher.find()) {
            deckPaths.add(matcher.group());
        }
        assertThat(deckPaths).as("source.js should contain at least one deck URL").isNotEmpty();

        for (String path : deckPaths) {
            ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + path, String.class);
            assertThat(response.getStatusCode().is2xxSuccessful())
                .as("Expected 2xx for %s but got %s", path, response.getStatusCode())
                .isTrue();
        }
    }
}
