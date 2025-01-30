package org.ies.deti.ua.Shows.repositories;

import org.ies.deti.ua.Shows.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long>{
    Optional<Movie> findByTitle(String title);
}
