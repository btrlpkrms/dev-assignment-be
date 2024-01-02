package com.transferz.controller;

import com.transferz.controller.dto.FlightDTO;
import com.transferz.dao.Flight;
import com.transferz.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for managing flights.
 */
@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;

    /**
     * Constructs a FlightController with the given FlightService.
     *
     * @param flightService The service used for flight operations.
     */
    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Adds a new flight based on the provided FlightDTO.
     *
     * @param flight DTO containing flight data.
     * @return ResponseEntity containing the saved flight or an error message.
     */
    @PostMapping
    public ResponseEntity<?> addFlight(@Valid @RequestBody FlightDTO flight) {
        try {
            Flight savedFlight = flightService.addFlight(flight);
            return ResponseEntity.ok(savedFlight);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add Flight:" + e.getMessage());
        }
    }

    /**
     * Retrieves a list of all flights.
     *
     * @return ResponseEntity containing a list of flights.
     */
    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }
}
