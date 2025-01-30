package org.ies.deti.ua.Shows.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity(name="quote")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Quote is mandatory")
    @Column(name="quote",nullable = false)
    private String quote;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="movie_id", nullable = false)
    private Movie movie;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public String toString() {
        return "Quote (" + this.getId() + "): "+ this.getQuote() + " - " + this.getMovie().getTitle() + "("+ this.getMovie().getYear() +")";
    }
}
