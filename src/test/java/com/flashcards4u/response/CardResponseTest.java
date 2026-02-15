package com.flashcards4u.response;

import com.flashcards4u.model.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardResponseTest {

    @Test
    public void constructorSetsAllFields() {
        CardResponse card = new CardResponse(1L, "a cat", "un gato", "spanish");

        assertEquals(1L, card.getId());
        assertEquals("a cat", card.getQuestion());
        assertEquals("un gato", card.getAnswer());
        assertEquals("spanish", card.getCategory());
    }

    @Test
    public void setIdUpdatesId() {
        CardResponse card = new CardResponse(1L, "q", "a", "c");
        card.setId(99L);
        assertEquals(99L, card.getId());
    }

    @Test
    public void setQuestionUpdatesQuestion() {
        CardResponse card = new CardResponse(1L, "q", "a", "c");
        card.setQuestion("new question");
        assertEquals("new question", card.getQuestion());
    }

    @Test
    public void setAnswerUpdatesAnswer() {
        CardResponse card = new CardResponse(1L, "q", "a", "c");
        card.setAnswer("new answer");
        assertEquals("new answer", card.getAnswer());
    }

    @Test
    public void setCategoryUpdatesCategory() {
        CardResponse card = new CardResponse(1L, "q", "a", "c");
        card.setCategory("new category");
        assertEquals("new category", card.getCategory());
    }

    @Test
    public void fromCardMapsAllFieldsCorrectly() {
        Card card = new Card(5L, "a cat", "un chat", "french");
        CardResponse response = CardResponse.fromCard(card);

        assertEquals(5L, response.getId());
        assertEquals("a cat", response.getQuestion());
        assertEquals("un chat", response.getAnswer());
        assertEquals("french", response.getCategory());
    }
}
