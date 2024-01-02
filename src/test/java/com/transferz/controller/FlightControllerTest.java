package com.transferz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transferz.controller.dto.FlightDTO;
import com.transferz.dao.Flight;
import com.transferz.service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addFlightSuccessTest() throws Exception {
        FlightDTO flightDTO = new FlightDTO("FL123", "ORI", "DES", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        String flightJson = objectMapper.writeValueAsString(flightDTO);

        when(flightService.addFlight(any(FlightDTO.class))).thenReturn(new Flight());

        mockMvc.perform(post("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightJson))
                .andExpect(status().isOk());
    }

    @Test
    void addFlightBadRequestTest() throws Exception {
        FlightDTO flightDTO = new FlightDTO();
        String flightJson = objectMapper.writeValueAsString(flightDTO);

        mockMvc.perform(post("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllFlightsTest() throws Exception {
        when(flightService.getAllFlights()).thenReturn(Arrays.asList(new Flight(), new Flight()));

        mockMvc.perform(get("/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

}

