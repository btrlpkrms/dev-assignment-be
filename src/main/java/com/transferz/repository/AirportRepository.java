package com.transferz.repository;

import com.transferz.dao.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Airport} entities.
 */
public interface AirportRepository extends JpaRepository<Airport,Long> {

    /**
     * Checks if an airport with the specified code exists.
     */
    boolean existsByCode(String code);

    /**
     * Checks if an airport with the specified name exists.
     */
    boolean existsByName(String name);

    /**
     * Finds a page of airports matching both name and code.
     */
    Page<Airport> findByNameAndCode(String name, String code, Pageable pageable);

    /**
     * Finds a page of airports matching the given name.
     */
    Page<Airport> findByName(String name, Pageable pageable);

    /**
     * Finds a page of airports matching the given code.
     */
    Page<Airport> findByCode(String code, Pageable pageable);

    /**
     * Finds an airport by its code.
     */
    Optional<Airport> findByCode(String code);
}
