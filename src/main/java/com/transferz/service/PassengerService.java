package com.transferz.service;

import com.transferz.config.ApplicationProperties;
import com.transferz.dao.Flight;
import com.transferz.dao.Passenger;
import com.transferz.repository.FlightRepository;
import com.transferz.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for managing passengers, interfacing with {@link PassengerRepository} and {@link FlightRepository}.
 */
@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;
    private final ApplicationProperties applicationProperties;

    /**
     * Constructs a PassengerService with the given repositories and application properties.
     *
     * @param passengerRepository   Repository used for passenger related operations.
     * @param flightRepository      Repository used for flight related operations.
     * @param applicationProperties Configuration properties for the application.
     */
    @Autowired
    public PassengerService(PassengerRepository passengerRepository, FlightRepository flightRepository, ApplicationProperties applicationProperties) {
        this.passengerRepository = passengerRepository;
        this.flightRepository = flightRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Adds a new passenger to a flight identified by the flight code.
     * Handles flight full and flight departed scenarios.
     *
     * @param name       The name of the passenger to add.
     * @param flightCode The code of the flight to which the passenger is being added.
     * @return The added Passenger entity.
     */
    @Transactional
    public Passenger addPassenger(String name, String flightCode) {
        Flight flight = flightRepository.findByCode(flightCode)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with code: " + flightCode));

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Passenger name is required.");
        }

        if (flight.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Flight has already departed.");
        }

        long passengerCount = passengerRepository.countByFlightCode(flight.getCode());
        if (passengerCount >= applicationProperties.getMaxPassengerCount()) {
            flight = findNextAvailableFlight(flight.getDestinationAirport().getCode(), flightCode);
            if (flight == null) {
                throw new IllegalArgumentException("No available flights");
            }
        }
        boolean isNameUnique = passengerRepository.countByFlightCodeAndName(flight.getCode(), name) == 0;
        if (!isNameUnique) {
            throw new IllegalArgumentException("Passenger with the same name already exists on this flight.");
        }


        Passenger passenger = new Passenger();
        passenger.setName(name);
        passenger.setFlight(flight);
        passengerRepository.save(passenger);
        return passenger;
    }

    /**
     * Finds the next available flight that meets certain criteria.
     *
     * @param currentDestinationAirportCode The current destination airport code to exclude from the search.
     * @param excludeFlightCode             The flight code to exclude from the search.
     * @return The next available Flight, or null if none is found.
     */
    private Flight findNextAvailableFlight(String currentDestinationAirportCode, String excludeFlightCode) {
        return flightRepository.findAll().stream()
                .filter(f -> !f.getCode().equals(excludeFlightCode))
                .filter(f -> f.getDepartureTime().isAfter(LocalDateTime.now()))
                .filter(f -> passengerRepository.countByFlightCode(f.getCode()) < applicationProperties.getMaxPassengerCount())
                .filter(f -> !f.getOriginAirport().getCode().equals(currentDestinationAirportCode))
                .findFirst()
                .orElse(null);
    }

}
