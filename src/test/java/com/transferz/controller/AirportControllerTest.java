package com.transferz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transferz.controller.dto.AirportDTO;
import com.transferz.dao.Airport;
import com.transferz.service.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirportController.class)
class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirportService airportService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addAirportSuccessTest() throws Exception {
        AirportDTO airportDTO = new AirportDTO("Test Airport", "TST", "Test Country");
        String airportJson = objectMapper.writeValueAsString(airportDTO);

        when(airportService.addAirport(any(AirportDTO.class))).thenReturn(new Airport(1L, "Test Airport", "TST", "Test Country"));

        mockMvc.perform(post("/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(airportJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Airport"))
                .andExpect(jsonPath("$.code").value("TST"))
                .andExpect(jsonPath("$.country").value("Test Country"));
    }

    @Test
    void addAirportConflictTest() throws Exception {
        AirportDTO airportDTO = new AirportDTO("Existing Airport", "EXT", "Existing Country");
        String airportJson = objectMapper.writeValueAsString(airportDTO);
        when(airportService.addAirport(any(AirportDTO.class))).thenThrow(new IllegalArgumentException("Airport with the same code or name already exists"));

        mockMvc.perform(post("/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(airportJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllAirportsTest() throws Exception {
        when(airportService.getAllAirports(null, null, PageRequest.of(0, 20))).thenReturn(new PageImpl<>(Arrays.asList(
                new Airport(1L, "Airport1", "A1", "Country1"),
                new Airport(2L, "Airport2", "A2", "Country2")
        )));

        mockMvc.perform(get("/airports").param("page","0").param("size","20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Airport1"))
                .andExpect(jsonPath("$.content[1].name").value("Airport2"));
    }

}

