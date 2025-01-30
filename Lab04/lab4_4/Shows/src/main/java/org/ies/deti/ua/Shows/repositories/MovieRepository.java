package org.ies.deti.ua.Shows.repositories;

import org.ies.deti.ua.Shows.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long>{
}
