package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.request.SearchFlightRequest;
import io.codelex.flightplanner.flights.response.SearchFlightsResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightsInMemoryRepository {
    private List<Flight> savedFlights = new ArrayList<>();

    public void saveFlight(Flight flight) {
        this.savedFlights.add(flight);
    }

    public List<Flight> listFlights() {
        return this.savedFlights;
    }

    public void deleteFlight(Long id) {
        for (Flight flight : this.savedFlights) {
            if (flight.getId() == id) {
                savedFlights.remove(flight);
                break;
            }
        }
    }

    public SearchFlightsResponse searchFlights(SearchFlightRequest searchFlightRequest) {
        List<Flight> flights = listFlights().stream()
                .filter(f -> f.getFrom().getAirport().equalsIgnoreCase(searchFlightRequest.getFrom()))
                .filter(f -> f.getTo().getAirport().equalsIgnoreCase(searchFlightRequest.getTo()))
                .filter(f -> f.getDepartureTime().toLocalDate().isEqual(LocalDate.parse(searchFlightRequest.getDepartureDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .toList();
        return new SearchFlightsResponse(flights.size(), flights.size(), flights);
    }

}
