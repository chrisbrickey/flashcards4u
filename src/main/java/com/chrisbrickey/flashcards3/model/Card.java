package com.chrisbrickey.flashcards3.model;

public class Card implements Comparable<Card> {
    private long id;
    private String question;
    private String answer;
    private String category;

    public Card(long id, String question, String answer, String category) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category;
    }

    public long getId() { return id; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getCategory() { return category; }

    @Override
    public int compareTo(Card other) {
        return Long.compare(this.id, other.id);
    }
}
