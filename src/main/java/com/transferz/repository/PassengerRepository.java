package com.transferz.repository;

import com.transferz.dao.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Passenger} entities.
 */
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    /**
     * Counts the number of passengers on a flight with a given code.
     */
    long countByFlightCode(String flightCode);

    /**
     * Counts the number of passengers with a given name on a flight with a specific code.
     */
    long countByFlightCodeAndName(String code,String name);
}
