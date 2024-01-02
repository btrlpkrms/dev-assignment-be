package com.transferz.service;

import com.transferz.controller.dto.AirportDTO;
import com.transferz.dao.Airport;
import com.transferz.repository.AirportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportService airportService;

    @Test
    void addAirportSuccessTest() throws Exception {
        AirportDTO airportDTO = new AirportDTO("Test Airport", "TST", "Test Country");
        when(airportRepository.existsByCode("TST")).thenReturn(false);
        when(airportRepository.existsByName("Test Airport")).thenReturn(false);
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArguments()[0]);

        Airport result = airportService.addAirport(airportDTO);

        assertEquals("Test Airport", result.getName());
        assertEquals("TST", result.getCode());
        assertEquals("Test Country", result.getCountry());
    }

    @Test
    void addAirportExistsTest() {
        AirportDTO airportDTO = new AirportDTO("Existing Airport", "EXT", "Existing Country");
        when(airportRepository.existsByCode("EXT")).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            airportService.addAirport(airportDTO);
        });

        assertEquals("Airport with the same code or name already exists", exception.getMessage());
    }

    @Test
    void getAllAirportsTest() {
        Page<Airport> mockPage = new PageImpl<>(Arrays.asList(
                new Airport(1L, "Airport1", "A1", "Country1"),
                new Airport(2L, "Airport2", "A2", "Country2")
        ));
        when(airportRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        Page<Airport> result = airportService.getAllAirports(null, null, PageRequest.of(0, 10));

        assertEquals(2, result.getContent().size());
        assertEquals("Airport1", result.getContent().get(0).getName());
    }

}
