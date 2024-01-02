package com.transferz.controller;

import com.transferz.controller.dto.AirportDTO;
import com.transferz.dao.Airport;
import com.transferz.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for managing airports.
 */
@RestController
@RequestMapping("/airports")
public class AirportController {
    private final AirportService airportService;

    /**
     * Constructs an AirportController with the given AirportService.
     *
     * @param airportService The service used for airport operations.
     */
    @Autowired
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    /**
     * Adds a new airport based on the provided AirportDTO.
     *
     * @param airport DTO containing airport data.
     * @return ResponseEntity containing the saved airport or an error message.
     */
    @PostMapping
    public ResponseEntity<?> addAirport(@Valid @RequestBody AirportDTO airport) {
        try {
            Airport savedAirport = airportService.addAirport(airport);
            return ResponseEntity.ok(savedAirport);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add airport: " + e.getMessage());
        }
    }

    /**
     * Retrieves a paginated list of airports. Optional query parameters can filter results by name or code.
     *
     * @param name     Optional airport name filter.
     * @param code     Optional airport code filter.
     * @param pageable Pagination configuration.
     * @return ResponseEntity containing a page of airports.
     */
    @GetMapping
    public ResponseEntity<Page<Airport>> getAllAirports(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            Pageable pageable) {
        Page<Airport> airports = airportService.getAllAirports(name, code, pageable);
        return ResponseEntity.ok(airports);
    }
}
