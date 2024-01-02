package com.transferz.service;

import com.transferz.controller.dto.AirportDTO;
import com.transferz.dao.Airport;
import com.transferz.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * Service for managing airports, interfacing with {@link AirportRepository}.
 */
@Service
public class AirportService {

    private final AirportRepository airportRepository;

    /**
     * Constructs an AirportService with the given AirportRepository.
     *
     * @param airportRepository The repository used for airport operations.
     */
    @Autowired
    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    /**
     * Adds a new airport based on the provided AirportDTO.
     * Throws an exception if an airport with the same code or name already exists.
     *
     * @param airportDTO The DTO containing data for the new airport.
     * @return The added Airport entity.
     */
    @Transactional
    public Airport addAirport(AirportDTO airportDTO) {
        if (airportRepository.existsByCode(airportDTO.getCode()) ||
                airportRepository.existsByName(airportDTO.getName())) {
            throw new IllegalArgumentException("Airport with the same code or name already exists");
        }
        Airport airport = Airport.builder().code(airportDTO.getCode()).country(airportDTO.getCountry()).name(airportDTO.getName()).build();
        return airportRepository.save(airport);
    }

    /**
     * Retrieves a paginated list of airports. Can filter by name, code, or both.
     *
     * @param name     Optional filter for the airport name.
     * @param code     Optional filter for the airport code.
     * @param pageable Pagination configuration.
     * @return A page of airports matching the given criteria.
     */
    @Transactional(readOnly = true)
    public Page<Airport> getAllAirports(String name, String code, Pageable pageable) {
        if (name != null && code != null) {
            return airportRepository.findByNameAndCode(name, code, pageable);
        } else if (name != null) {
            return airportRepository.findByName(name, pageable);
        } else if (code != null) {
            return airportRepository.findByCode(code, pageable);
        } else {
            return airportRepository.findAll(pageable);
        }
    }

}
