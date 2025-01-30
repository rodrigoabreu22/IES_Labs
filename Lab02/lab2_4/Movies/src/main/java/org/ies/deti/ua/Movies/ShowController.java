package org.ies.deti.ua.Movies;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;

@RestController
public class ShowController {

    private final AtomicLong counter = new AtomicLong();
    private List<Show> shows = populateShows();

    @GetMapping("api/quote")
    public Quote quote() {
        Show show = getRandomShow();

        return new Quote(counter.incrementAndGet(), show.getRandomQuote());
    }

    @GetMapping("api/shows")
    public Shows shows(){
        List<String[]> showsWithQuotes = new ArrayList<String[]>();
        for(Show show : shows){
            if (!show.getQuotes().isEmpty()){
                showsWithQuotes.add(new String[]{String.valueOf(show.getId()),show.getTitle()});
            }
        }
        return new Shows(counter.incrementAndGet(), showsWithQuotes);
    }

    @GetMapping("api/quotes")
    public Quotes quotes(@RequestParam(value="show") long show){
        for(Show sh : shows){
            if (sh.getId()==Integer.parseInt(String.valueOf(show))){
                return new Quotes(counter.incrementAndGet(), sh.getTitle(), sh.getQuotes());
            }
        }
        return null;
    }

    public List<Show> populateShows(){
        List<Show> movies = new ArrayList<>();

        Show movie1 = Show.createShow(counter.incrementAndGet(),"Dead Poets Society");
        movies.add(movie1);
        movie1.addQuote("Welcome to the Dead Poets Society");
        movie1.addQuote("Oh captain, my captain!");
        movie1.addQuote("We read and write poetry because we are members of the human race!");

        Show movie2 = Show.createShow(counter.incrementAndGet(),"Interstellar");
        movies.add(movie2);
        movie2.addQuote("Don't let me leave Murph!");
        movie2.addQuote("Every hour we spend on this planet will be 7 years on earth.");

        Show movie3 = Show.createShow(counter.incrementAndGet(),"Rocky");
        movies.add(movie3);

        Show movie4 = Show.createShow(counter.incrementAndGet(),"Creed");
        movies.add(movie4);
        movie4.addQuote("One step at a time. One punch at a time. One round at a time.");
        movie4.addQuote("You didn't tell me your uncle was Rocky Balboa.");

        return movies;
    }

    public Show getRandomShow(){
        Random rand = new Random();
        int randomIndex = rand.nextInt(shows.size());

        Show randomShow = shows.get(randomIndex);

        return randomShow;
    }

}