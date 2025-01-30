package org.ies.deti.ua.Movies;

import java.util.ArrayList;
import java.util.Random;

public class Show {
    private long id;
    private String title;
    private ArrayList<String>quotes;

    private Show(long id, String title) {
        this.id = id;
        this.title = title;
        this.quotes = new ArrayList<>();
    }

    public static Show createShow(long id, String title){
        return new Show(id, title);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String>getQuotes() {
        return quotes;
    }

    public void addQuote(String quote) {
        quotes.add(quote);
    }

    public String getRandomQuote() {

        if (quotes.isEmpty()) {
            return "No quotes available";
        }

        Random rand = new Random();
        int randomIndex = rand.nextInt(quotes.size());

        return quotes.get(randomIndex);
    }

    @Override
    public String toString() {
        return "Show \"" + title + "\" - id: "+id+"\n";
    }
}
