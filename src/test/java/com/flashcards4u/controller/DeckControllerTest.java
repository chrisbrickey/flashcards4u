package com.flashcards4u.controller;

import com.flashcards4u.model.Card;
import com.flashcards4u.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

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
    public void returnCardResponse() throws Exception {
        List<Card> mockCards = List.of(new Card(1L, "demanding", "exigeant(e)", "french"));
        when(deckService.buildDeck(anyString(), any(Random.class))).thenReturn(mockCards);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/deck")
                .param("filepath", "static/csv/multiple_categories.csv")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void returnBadRequestForMissingFilepath() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/deck")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
