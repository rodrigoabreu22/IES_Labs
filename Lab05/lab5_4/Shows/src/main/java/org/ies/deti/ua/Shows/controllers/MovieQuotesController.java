package org.ies.deti.ua.Shows.controllers;

import jakarta.validation.Valid;
import org.ies.deti.ua.Shows.Services.MovieService;
import org.ies.deti.ua.Shows.Services.QuoteService;
import org.ies.deti.ua.Shows.entities.Movie;
import org.ies.deti.ua.Shows.entities.Quote;
import org.ies.deti.ua.Shows.Services.QuoteConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@RestController
@RequestMapping("api")
public class MovieQuotesController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private QuoteService quoteService;

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/quote")
    public ResponseEntity<Quote> getRandomQuote() {
        return new ResponseEntity<>(quoteService.getRandomQuote(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
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

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/quotes/show/{id}")
        public ResponseEntity<Quote> addQuote(@Valid @RequestBody Quote quote, @PathVariable(value = "id") Long movieId) {
        Movie movie = movieService.getMovieById(movieId);
        quote.setMovie(movie);
        return  new ResponseEntity<>(quoteService.addQuote(quote), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/quotes/{id}")
    public ResponseEntity<Quote> deleteQuote(@PathVariable(value = "id") Long quoteId) {
        quoteService.deleteQuote(quoteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/quotes/{id}")
    public ResponseEntity<Quote> updateQuote(@PathVariable(value = "id") Long quoteId, @RequestBody Quote quote) {
        Quote updated = quoteService.updateQuote(quoteId, quote);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }



    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/shows")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/shows")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.addMovie(movie), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/shows/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable(value = "id") Long movieId) {
        movieService.deleteMovie(movieId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/shows/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable(value = "id") Long movieId, @RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.updateMovie(movieId, movie), HttpStatus.OK);
    }
}
