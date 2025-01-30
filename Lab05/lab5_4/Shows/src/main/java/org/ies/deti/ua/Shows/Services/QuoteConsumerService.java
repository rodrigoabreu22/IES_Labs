package org.ies.deti.ua.Shows.Services;

import org.ies.deti.ua.Shows.entities.Quote;
import org.ies.deti.ua.Shows.repositories.QuoteRepository;
import org.ies.deti.ua.Shows.repositories.MovieRepository;
import org.ies.deti.ua.Shows.entities.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuoteConsumerService {

    private static final String TOPIC = "lab05_113626";
    private final QuoteRepository quoteRepository;
    private final MovieRepository movieRepository;


    @Autowired
    public QuoteConsumerService(QuoteRepository quoteRepository, MovieRepository movieRepository) {
        this.quoteRepository = quoteRepository;
        this.movieRepository = movieRepository;
    }

    @KafkaListener(topics = TOPIC, groupId = "listener")
    @Transactional
    public void consume(Quote incomingQuote) {
        // Check if the movie exists in the database
        Movie movie = movieRepository.findByTitle(incomingQuote.getMovie().getTitle())
                .orElseGet(() -> {
                    // If the movie doesn't exist, create and save it
                    Movie newMovie = new Movie();
                    newMovie.setTitle(incomingQuote.getMovie().getTitle());
                    newMovie.setYear(incomingQuote.getMovie().getYear());
                    return movieRepository.save(newMovie);
                });

        // Associate the persisted movie with the incoming quote
        incomingQuote.setMovie(movie);

        // Save the quote
        quoteRepository.save(incomingQuote);

        System.out.println(String.format("Quote received and saved -> %s", incomingQuote));
    }

}
