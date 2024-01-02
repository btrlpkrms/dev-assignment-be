package com.transferz.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object for Airport information.
 */
@Getter
@AllArgsConstructor
public class AirportDTO {
    /**
     * Name of the airport.
     */
    @NotNull
    private String name;

    /** Unique airport code. */
    @NotNull
    private String code;

    /** Country where the airport is located. */
    @NotNull
    private String country;
}
