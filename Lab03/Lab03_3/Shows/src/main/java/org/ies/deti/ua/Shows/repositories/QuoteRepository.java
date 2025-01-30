package org.ies.deti.ua.Shows.repositories;

import org.ies.deti.ua.Shows.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long>{
    List<Quote> findAllByMovieId(Long movieId);
}
