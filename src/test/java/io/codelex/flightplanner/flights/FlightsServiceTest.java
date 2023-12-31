package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.exception.DepartureAndArrivalAirportAreTheSameException;
import io.codelex.flightplanner.flights.request.SearchFlightRequest;
import io.codelex.flightplanner.flights.response.SearchFlightsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class FlightsServiceTest {

    @Mock
    FlightsRepository flightsRepository;
    @InjectMocks
    FlightsService flightsService;

    @Test
    @DisplayName("Should be able to search flight")
    void canSearchFlights() throws DepartureAndArrivalAirportAreTheSameException {
        SearchFlightRequest request = new SearchFlightRequest();
        request.setFrom("rix");
        request.setTo("us5");
        request.setDepartureDate("2023-07-16");
        SearchFlightsResponse mockResponse = new SearchFlightsResponse(0,0, new ArrayList<>());
        Mockito.when(flightsRepository.searchFlights(request)).thenReturn(mockResponse);
        SearchFlightsResponse actualResponse = flightsService.searchFlights(request);
        Mockito.verify(flightsRepository).searchFlights(request);
        Assertions.assertEquals(0, actualResponse.getPage());
        Assertions.assertEquals(0, actualResponse.getTotalItems());
        Assertions.assertEquals(mockResponse.getItems(), actualResponse.getItems());

    }
}