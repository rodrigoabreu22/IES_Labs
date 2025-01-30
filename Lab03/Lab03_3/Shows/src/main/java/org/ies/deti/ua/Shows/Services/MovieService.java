package org.ies.deti.ua.Shows.Services;

import org.ies.deti.ua.Shows.entities.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieService {
    public List<Movie> getAllMovies();

    public Movie getMovieById(Long id);

    public Movie addMovie(Movie movie);

    public Movie updateMovie(Long id, Movie movie);

    public void deleteMovie(Long id);
}
