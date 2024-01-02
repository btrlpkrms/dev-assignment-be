package com.transferz.service;

import com.transferz.controller.dto.FlightDTO;
import com.transferz.dao.Airport;
import com.transferz.dao.Flight;
import com.transferz.repository.AirportRepository;
import com.transferz.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class FlightServiceTest {

    public static final String FLIGHT_CODE = "FL123";
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private FlightService flightService;


    @Test
    void addFlightSuccessTest() {
        FlightDTO flightDTO = new FlightDTO(FLIGHT_CODE, "ORI", "DES", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        when(flightRepository.existsByCode(FLIGHT_CODE)).thenReturn(false);
        when(airportRepository.findByCode("ORI")).thenReturn(Optional.of(new Airport()));
        when(airportRepository.findByCode("DES")).thenReturn(Optional.of(new Airport()));
        when(flightRepository.save(any(Flight.class))).thenAnswer(i -> i.getArguments()[0]);

        Flight result = flightService.addFlight(flightDTO);

        assertEquals(FLIGHT_CODE, result.getCode());
        assertNotNull(result.getOriginAirport());
        assertNotNull(result.getDestinationAirport());
    }

    @Test
    void addFlightExistsTest() {
        FlightDTO flightDTO = new FlightDTO(FLIGHT_CODE, "ORI", "DES", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        when(flightRepository.existsByCode(FLIGHT_CODE)).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flightService.addFlight(flightDTO);
        });

        assertEquals("Flight with code FL123 already exists.", exception.getMessage());
    }

    @Test
    void addFlightOriginAirportNotFoundTest() {
        FlightDTO flightDTO = new FlightDTO(FLIGHT_CODE, "ORI", "DES", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        when(flightRepository.existsByCode(FLIGHT_CODE)).thenReturn(false);
        when(airportRepository.findByCode("ORI")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flightService.addFlight(flightDTO);
        });

        assertEquals("Origin airport not found.", exception.getMessage());
    }

    @Test
    void getAllFlightsTest() {
        when(flightRepository.findAll()).thenReturn(Arrays.asList(new Flight(), new Flight()));

        List<Flight> result = flightService.getAllFlights();

        assertEquals(2, result.size());
    }
}

