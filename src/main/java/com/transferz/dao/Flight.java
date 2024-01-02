package com.transferz.dao;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a flight in the system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
    /**
     * The unique identifier for the flight.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique code of the flight.
     */
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * The airport from which the flight departs.
     */
    @ManyToOne
    @JoinColumn(name = "origin_airport_id",nullable = false)
    private Airport originAirport;

    /**
     * The destination airport of the flight.
     */
    @ManyToOne
    @JoinColumn(name = "destination_airport_id",nullable = false)
    private Airport destinationAirport;

    /**
     * The scheduled departure time of the flight.
     */
    @Column(nullable = false)
    private LocalDateTime departureTime;

    /**
     * The scheduled arrival time of the flight.
     */
    @Column(nullable = false)
    private LocalDateTime arrivalTime;
}
