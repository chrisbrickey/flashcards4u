package com.chrisbrickey.flashcards3.controller;


import com.chrisbrickey.flashcards3.response.CardResponse;
import com.chrisbrickey.flashcards3.response.DeckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(DeckController.class)
public class DeckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void returnDeckResponseAttributes() throws IOException {
        DeckController controller = new DeckController();
        DeckResponse response = controller.getDeck("static/csv/sample.csv");
        List<CardResponse> cardResponses = response.getCards();
        DeckResponse.sortCards(cardResponses);

        assertEquals(4, cardResponses.size());

        var firstCard = cardResponses.get(0);
        assertEquals(1L, firstCard.getId());
        assertEquals("a cat", firstCard.getQuestion());
        assertEquals("un gato", firstCard.getAnswer());
        assertEquals("spanish", firstCard.getCategory());

        var secondCard = cardResponses.get(1);
        assertEquals(2L, secondCard.getId());
        assertEquals("a cat", secondCard.getQuestion());
        assertEquals("un chat", secondCard.getAnswer());
        assertEquals("french", secondCard.getCategory());

        var thirdCard = cardResponses.get(2);
        assertEquals(3L, thirdCard.getId());
        assertEquals("Index into a String.", thirdCard.getQuestion());
        assertEquals("someString.substring(0,1)", thirdCard.getAnswer());
        assertEquals("java", thirdCard.getCategory());
    }

    @Test
    public void shuffleCardsAfterIdsAssigned() {
      // setup
      List<List<String>> sample_content = Arrays.asList(
        Arrays.asList("a cat", "un gato", "spanish"),
        Arrays.asList("a cat", "un chat", "french"),
        Arrays.asList("Index into a String.", "someString.substring(0,1)", "java"),
        Arrays.asList("Create a new array of ints with a specified size of 4 elements.", "int[] myArray = new int[4]", "java")
      );

      // exercise DeckResponse directly
      DeckResponse response = new DeckResponse(sample_content, new Random(42L));
      List<CardResponse> cards = response.getCards();

      // First card in CSV has ID 1; after shuffling it should not still be first
      assertNotEquals(1L, cards.get(0).getId());
    }

    @Test
    // integration test
    public void returnCardResponse() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/deck")
                .param("filepath", "static/csv/sample.csv")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}