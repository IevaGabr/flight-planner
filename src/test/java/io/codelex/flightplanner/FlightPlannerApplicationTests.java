package io.codelex.flightplanner;

import io.codelex.flightplanner.flights.FlightsController;
import io.codelex.flightplanner.flights.FlightsRepository;
import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.request.AddFlightRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class FlightPlannerApplicationTests {

    @Autowired
    FlightsController flightsController;
    @Autowired
    FlightsRepository flightsRepository;


    private final Airport from = new Airport("Latvia", "Riga", "RIX");
    private final Airport to = new Airport("Italy", "Milan", "BGT");
    private final LocalDateTime departureTime = LocalDateTime.of(2023, 7, 13, 7, 0);
    private final LocalDateTime arrivalTime = departureTime.plusHours(3);
    private final String carrier = "AirBaltic";
    @Test
    @DisplayName("Should be able add flight")
    void canAddFlight() throws Exception {
        int countSavedFlights = flightsRepository.listFlights().size();
        AddFlightRequest addFlightRequest = new AddFlightRequest(from, to, carrier, departureTime, arrivalTime);

        Flight savedFlight = flightsController.addFlight(addFlightRequest);

        assertTrue(countSavedFlights < flightsRepository.listFlights().size());

        assertNotNull(savedFlight.getId());
        assertEquals(savedFlight.getFrom(), from);
        assertEquals(savedFlight.getTo(), to);
        assertEquals(savedFlight.getCarrier(), carrier);
        assertEquals(savedFlight.getDepartureTime(), departureTime);
        assertEquals(savedFlight.getArrivalTime(), arrivalTime);
    }

    @Test
    @DisplayName("Should be able to clear flights")
    void canClearFlights() {

        AddFlightRequest addFlightRequest = new AddFlightRequest(from, to, carrier, departureTime, arrivalTime);

        flightsController.addFlight(addFlightRequest);

        assertTrue(flightsRepository.listFlights().size() > 0);

        flightsController.clearFlights();

        assertEquals(0, flightsRepository.listFlights().size());
    }

}
