package com.transferz.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Flight information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {

    /** Unique code identifying the flight. */
    @NotNull
    private String code;

    /** Airport code for the flight's origin. */
    @NotNull
    private String originAirportCode;

    /** Airport code for the flight's destination. */
    @NotNull
    private String destinationAirportCode;

    /** Scheduled departure time of the flight. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime departureTime;

    /** Scheduled arrival time of the flight. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime arrivalTime;

}
