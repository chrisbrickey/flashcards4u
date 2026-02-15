package com.flashcards4u.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcards4u.model.Card;
import com.flashcards4u.response.DeckResponse;
import com.flashcards4u.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.flashcards4u.exception.DeckNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(DeckController.class)
public class DeckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeckService deckService;

    @Test
    public void returnDeckResponseWithFlashcardContent() throws Exception {
        List<Card> mockCards = List.of(new Card(1L, "demanding", "exigeant(e)", "french"));
        when(deckService.buildDeck(anyString(), any(Random.class))).thenReturn(mockCards);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/decks/english-to-french")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cards[0].question").value("demanding"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cards[0].answer").value("exigeant(e)"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cards[0].category").value("french"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cards[0].id").value(1))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        DeckResponse deckResponse = mapper.readValue(result.getResponse().getContentAsString(), DeckResponse.class);
        assertInstanceOf(DeckResponse.class, deckResponse);
    }

    @Test
    public void returnNotFoundForUnknownDeck() throws Exception {
      String deckName = "nonexistent";
      String path= "/v1/decks/" + deckName;

      this.mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertInstanceOf(DeckNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(deckName)));
    }
}
