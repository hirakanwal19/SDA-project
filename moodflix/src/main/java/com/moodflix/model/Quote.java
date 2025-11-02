package com.moodflix.model;

public class Quote {
    private String text;
    private String author;
    private String mood;

    public Quote(String text, String author, String mood) {
        this.text = text;
        this.author = author;
        this.mood = mood;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public String getMood() {
        return mood;
    }

    @Override
    public String toString() {
        return "\"" + text + "\" — " + author + " [" + mood + "]";
    }
}
