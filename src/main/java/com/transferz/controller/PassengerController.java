package com.transferz.controller;

import com.transferz.controller.dto.PassengerDTO;
import com.transferz.dao.Passenger;
import com.transferz.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST Controller for managing passengers.
 */
@RestController
@RequestMapping("/passengers")
public class PassengerController {
    private final PassengerService passengerService;

    /**
     * Constructs a PassengerController with the given PassengerService.
     *
     * @param passengerService The service used for passenger-related operations.
     */
    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * Adds a new passenger based on the provided PassengerDTO.
     *
     * @param passengerDTO DTO containing passenger's name and associated flight code.
     * @return ResponseEntity containing the saved passenger or an error message.
     */
    @PostMapping
    public ResponseEntity<?> addPassenger(@Valid @RequestBody PassengerDTO passengerDTO) {
        try {
            Passenger savedPassenger = passengerService.addPassenger(passengerDTO.getName(),passengerDTO.getFlightCode());
            return ResponseEntity.ok(savedPassenger);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add Passenger: " + e.getMessage());
        }
    }

}
