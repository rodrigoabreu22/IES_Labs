package org.ies.deti.ua.Shows.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity(name="movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title is mandatory")
    @Column(name="title",nullable = false)
    private String title;

    @Column(name="year", nullable = false)
    private int year;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Movie (id: " +this.getId() + "): title = " + this.getTitle() + " year = " + this.getYear();
    }
}