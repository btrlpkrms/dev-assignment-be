package com.transferz.repository;

import com.transferz.dao.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Flight} entities.
 */
public interface FlightRepository extends JpaRepository<Flight, Long> {

    /**
     * Retrieves a flight by its unique code.
     */
    Optional<Flight> findByCode(String code);

    /**
     * Checks if a flight with the specified code exists.
     */
    boolean existsByCode(String code);
}
