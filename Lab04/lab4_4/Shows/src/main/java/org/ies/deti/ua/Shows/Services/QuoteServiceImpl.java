package org.ies.deti.ua.Shows.Services;

import org.ies.deti.ua.Shows.entities.Quote;
import org.ies.deti.ua.Shows.repositories.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public List<Quote> getAllQuotes(){
        return quoteRepository.findAll();
    };

    public List<Quote> findAllByMovie(Long movieId){
        return quoteRepository.findAllByMovieId(movieId);
    };

    public Quote addQuote(Quote quote){
        return quoteRepository.save(quote);
    };

    public Quote updateQuote(Long id, Quote quote){
        Quote quoteToUpdate = quoteRepository.findById(id).orElse(null);
        assert(quoteToUpdate != null);
        quoteToUpdate.setMovie(quote.getMovie());
        quoteToUpdate.setQuote(quote.getQuote());
        return quoteRepository.save(quoteToUpdate);
    };

    public void deleteQuote(Long id){
        quoteRepository.deleteById(id);
    };

    public Quote getRandomQuote(){
        List<Quote> quotes = quoteRepository.findAll();
        if (quotes.isEmpty()) {
            return null;
        }

        Random rand = new Random();
        int randomIndex = rand.nextInt(quotes.size());

        return quotes.get(randomIndex);
    }
}
