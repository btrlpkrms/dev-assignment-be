package com.transferz.service;

import com.transferz.controller.dto.FlightDTO;
import com.transferz.dao.Airport;
import com.transferz.dao.Flight;
import com.transferz.repository.AirportRepository;
import com.transferz.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing flights, interfacing with {@link FlightRepository} and {@link AirportRepository}.
 */
@Service
public class FlightService {
    private final FlightRepository flightRepository;

    private final AirportRepository airportRepository;

    /**
     * Constructs a FlightService with the given repositories.
     *
     * @param flightRepository  Repository used for flight-related operations.
     * @param airportRepository Repository used for airport-related operations.
     */
    @Autowired
    public FlightService(FlightRepository flightRepository,AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    /**
     * Adds a new flight based on the provided FlightDTO.
     * Throws an exception if a flight with the same code already exists,
     * or if the origin or destination airports are not found.
     *
     * @param flightDTO The DTO containing data for the new flight.
     * @return The added Flight entity.
     * @throws IllegalArgumentException if a flight with the same code exists,
     *                                  or if the origin/destination airports are not found.
     */
    @Transactional
    public Flight addFlight(FlightDTO flightDTO) {
        if (flightRepository.existsByCode(flightDTO.getCode())) {
            throw new IllegalArgumentException("Flight with code " + flightDTO.getCode() + " already exists.");
        }

        Airport originAirport = airportRepository.findByCode(flightDTO.getOriginAirportCode())
                .orElseThrow(() -> new IllegalArgumentException("Origin airport not found."));
        Airport destinationAirport = airportRepository.findByCode(flightDTO.getDestinationAirportCode())
                .orElseThrow(() -> new IllegalArgumentException("Destination airport not found."));

        Flight flight = Flight.builder()
                .code(flightDTO.getCode())
                .originAirport(originAirport)
                .destinationAirport(destinationAirport)
                .departureTime(flightDTO.getDepartureTime())
                .arrivalTime(flightDTO.getArrivalTime())
                .build();

        return flightRepository.save(flight);
    }

    /**
     * Retrieves a list of all flights.
     *
     * @return A list of all flights.
     */
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
}
