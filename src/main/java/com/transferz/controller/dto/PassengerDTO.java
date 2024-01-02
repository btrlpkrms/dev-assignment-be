package com.transferz.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
/**
 * Data Transfer Object for Passenger information.
 */
@Getter
@AllArgsConstructor
public class PassengerDTO {

    /** Name of the passenger. */
    @NotNull
    private String name;

    /** Code of the flight the passenger is associated with. */
    @NotNull
    private String flightCode;
}
