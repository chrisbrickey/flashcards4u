package com.flashcards4u.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(RootController.class)
public class RootControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void rootRoutesToPrimaryView() throws Exception {
        this.mockMvc.perform(get("/"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.forwardedUrl("index.html"));
    }
}
