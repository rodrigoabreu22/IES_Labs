package org.ies.deti.ua.Shows.Services;

import org.ies.deti.ua.Shows.entities.Quote;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuoteService {
    public List<Quote> getAllQuotes();

    public List<Quote> findAllByMovie(Long movieId);

    public Quote addQuote(Quote quote);

    public Quote updateQuote(Long id, Quote quote);

    public void deleteQuote(Long id);

    public Quote getRandomQuote();
}
