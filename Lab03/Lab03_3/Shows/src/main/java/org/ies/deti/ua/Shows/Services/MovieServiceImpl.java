package org.ies.deti.ua.Shows.Services;

import org.ies.deti.ua.Shows.entities.Movie;
import org.ies.deti.ua.Shows.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {

        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    };

    public Movie getMovieById(Long id){
        return movieRepository.findById(id).get();
    };

    public Movie addMovie(Movie movie){
        return movieRepository.save(movie);
    };

    public Movie updateMovie(Long id, Movie movie){
        Movie movieToUpdate = movieRepository.findById(id).orElse(null);
        assert(movieToUpdate != null);
        movieToUpdate.setTitle(movie.getTitle());
        movieToUpdate.setYear(movie.getYear());
        return movieRepository.save(movieToUpdate);
    };

    public void deleteMovie(Long id){
        movieRepository.deleteById(id);
    };
}
