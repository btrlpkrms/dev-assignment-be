package com.transferz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transferz.controller.dto.PassengerDTO;
import com.transferz.dao.Flight;
import com.transferz.dao.Passenger;
import com.transferz.service.PassengerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PassengerController.class)
public class PassengerControllerTest {

    public static final String PASSENGER_NAME = "PassengerX";
    public static final String FLIGHT_CODE = "FL123";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerService passengerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddPassenger_Success() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setName(PASSENGER_NAME);
        Flight flight = new Flight();
        flight.setCode(FLIGHT_CODE);
        passenger.setFlight(flight);
        PassengerDTO passengerDTO = new PassengerDTO(PASSENGER_NAME, FLIGHT_CODE);

        when(passengerService.addPassenger(anyString(), anyString())).thenReturn(passenger);

        mockMvc.perform(post("/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passengerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(PASSENGER_NAME))
                .andExpect(jsonPath("$.flight.code").value(FLIGHT_CODE));
    }

    @Test
    public void testAddPassenger_FlightNotFound() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setName(PASSENGER_NAME);

        PassengerDTO passengerDTO = new PassengerDTO(PASSENGER_NAME, FLIGHT_CODE);

        when(passengerService.addPassenger(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Flight not found"));

        mockMvc.perform(post("/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passengerDTO)))
                .andExpect(status().isInternalServerError());
    }

}

