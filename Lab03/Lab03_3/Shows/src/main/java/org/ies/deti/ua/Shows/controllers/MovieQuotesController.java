package org.ies.deti.ua.Shows.controllers;

import jakarta.validation.Valid;
import org.ies.deti.ua.Shows.Services.MovieService;
import org.ies.deti.ua.Shows.Services.QuoteService;
import org.ies.deti.ua.Shows.entities.Movie;
import org.ies.deti.ua.Shows.entities.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class MovieQuotesController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private QuoteService quoteService;


    @GetMapping("/quote")
    public ResponseEntity<Quote> getRandomQuote() {
        return new ResponseEntity<>(quoteService.getRandomQuote(), HttpStatus.OK);
    }

    @GetMapping("/quotes")
    public ResponseEntity<List<Quote>> getAllQuotes(@RequestParam(required=false) Long show) {
        if (show == null) {
            return new ResponseEntity<>(quoteService.getAllQuotes(), HttpStatus.OK);
        }
        else {
            List<Quote> quotes = quoteService.findAllByMovie(show);
            return new ResponseEntity<>(quotes, HttpStatus.OK);
        }
    }

    @PostMapping("/quotes/show/{id}")
        public ResponseEntity<Quote> addQuote(@Valid @RequestBody Quote quote, @PathVariable(value = "id") Long movieId) {
        Movie movie = movieService.getMovieById(movieId);
        quote.setMovie(movie);
        return  new ResponseEntity<>(quoteService.addQuote(quote), HttpStatus.OK);
    }

    @DeleteMapping("/quotes/{id}")
    public ResponseEntity<Quote> deleteQuote(@PathVariable(value = "id") Long quoteId) {
        quoteService.deleteQuote(quoteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/quotes/{id}")
    public ResponseEntity<Quote> updateQuote(@PathVariable(value = "id") Long quoteId, @RequestBody Quote quote) {
        Quote updated = quoteService.updateQuote(quoteId, quote);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }




    @GetMapping("/shows")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @PostMapping("/shows")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.addMovie(movie), HttpStatus.OK);
    }

    @DeleteMapping("/shows/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable(value = "id") Long movieId) {
        movieService.deleteMovie(movieId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/shows/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable(value = "id") Long movieId, @RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.updateMovie(movieId, movie), HttpStatus.OK);
    }
}
