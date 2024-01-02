package com.transferz.service;

import com.transferz.config.ApplicationProperties;
import com.transferz.dao.Airport;
import com.transferz.dao.Flight;
import com.transferz.dao.Passenger;
import com.transferz.repository.FlightRepository;
import com.transferz.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class PassengerServiceTest {

    public static final int MAX_CAPACITY = 150;
    public static final String FLIGHT_CODE = "FL123";
    public static final String PASSENGER_NAME = "PassengerX";
    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private PassengerService passengerService;

    private Flight flight;

    @BeforeEach
    void setUp() {
        flight = new Flight();
        flight.setCode(FLIGHT_CODE);
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight.setOriginAirport(new Airport());
        flight.getOriginAirport().setCode("ESB");
        flight.setDestinationAirport(new Airport());
        flight.getDestinationAirport().setCode("SBW");
        when(applicationProperties.getMaxPassengerCount()).thenReturn(MAX_CAPACITY);
    }


    @Test
    void addPassengerSuccessTest() throws Exception {
        when(flightRepository.findByCode(FLIGHT_CODE)).thenReturn(Optional.of(flight));
        when(passengerRepository.countByFlightCodeAndName(FLIGHT_CODE, PASSENGER_NAME)).thenReturn(0L);

        Passenger result = passengerService.addPassenger(PASSENGER_NAME, FLIGHT_CODE);

        assertEquals(PASSENGER_NAME, result.getName());
        assertEquals(FLIGHT_CODE, result.getFlight().getCode());
    }

    @Test
    void addPassengerFlightNotFoundTest() {
        when(flightRepository.findByCode(FLIGHT_CODE)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            passengerService.addPassenger(PASSENGER_NAME, FLIGHT_CODE);
        });

        assertEquals("Flight not found with code: FL123", exception.getMessage());
    }

    @Test
    void addPassengerNameIsRequiredTest() {
        when(flightRepository.findByCode(FLIGHT_CODE)).thenReturn(Optional.of(flight));

        Exception exception = assertThrows(Exception.class, () -> {
            passengerService.addPassenger("", FLIGHT_CODE);
        });

        assertEquals("Passenger name is required.", exception.getMessage());
    }

    @Test
    void addPassengerFlightOverCapacityTest() {

        when(flightRepository.findByCode(FLIGHT_CODE)).thenReturn(Optional.of(flight));
        when(passengerRepository.countByFlightCode(FLIGHT_CODE)).thenReturn((long) MAX_CAPACITY);

        Exception exception = assertThrows(Exception.class, () -> {
            passengerService.addPassenger(PASSENGER_NAME, FLIGHT_CODE);
        });

        assertEquals("No available flights", exception.getMessage());
    }

    @Test
    void addPassengerToNextAvailableFlight() throws Exception {


        when(flightRepository.findByCode(FLIGHT_CODE)).thenReturn(Optional.of(flight));
        when(applicationProperties.getMaxPassengerCount()).thenReturn(150);
        when(passengerRepository.countByFlightCode(FLIGHT_CODE)).thenReturn((long) 150);

        Flight nextFlight = new Flight();
        nextFlight.setCode("FL124");
        nextFlight.setDepartureTime(LocalDateTime.now().plusDays(2));
        nextFlight.setOriginAirport(new Airport());
        nextFlight.getOriginAirport().setCode("OTHER");

        when(flightRepository.findAll()).thenReturn(Arrays.asList(flight, nextFlight));
        when(passengerRepository.countByFlightCode(nextFlight.getCode())).thenReturn(0L);

        Passenger result = passengerService.addPassenger(PASSENGER_NAME, FLIGHT_CODE);

        assertNotEquals(FLIGHT_CODE, result.getFlight().getCode());
        assertEquals("FL124", result.getFlight().getCode());
        verify(passengerRepository).save(any(Passenger.class));
    }

}

