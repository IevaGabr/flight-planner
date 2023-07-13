package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.exception.DepartureAndArrivalAirportAreTheSameException;
import io.codelex.flightplanner.flights.exception.FlightAlreadyExistException;
import io.codelex.flightplanner.flights.exception.NothingFoundException;
import io.codelex.flightplanner.flights.request.AddFlightRequest;
import io.codelex.flightplanner.flights.request.SearchFlightRequest;
import io.codelex.flightplanner.flights.response.SearchFlightsResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FlightsService {

    private volatile FlightsRepository flightsRepository;

    public FlightsService(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

    public Flight addFlight(AddFlightRequest addFlightRequest) throws DepartureAndArrivalAirportAreTheSameException, IllegalArgumentException, FlightAlreadyExistException, NullPointerException {
        if (addFlightRequest.isAirportFromAndToEqual()) {
            throw new DepartureAndArrivalAirportAreTheSameException("Departure and arrival airport are  the same!");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (!addFlightRequest.isCorrectArrivalTime()) {
            throw new IllegalArgumentException();
        }
        synchronized (this) {
        if (!this.flightsRepository.listFlights().isEmpty() && this.flightsRepository.listFlights().stream()
                .filter(Objects::nonNull)
                .filter(f -> f.getFrom().equals(addFlightRequest.getFrom()))
                .filter(f -> f.getTo().equals(addFlightRequest.getTo()))
                .filter(f -> f.getCarrier().equals(addFlightRequest.getCarrier()))
                .filter(f -> f.getDepartureTime().isEqual(LocalDateTime.parse(addFlightRequest.getDepartureTime(), formatter)))
                .anyMatch(f -> f.getArrivalTime().isEqual(LocalDateTime.parse(addFlightRequest.getArrivalTime(), formatter)))) {
            throw new FlightAlreadyExistException("Flight already exists!");
        }
            Flight flight = new Flight(this.flightsRepository.listFlights().stream().mapToLong(Flight::getId).max().orElse(0) + 1,
                    addFlightRequest.getFrom(),
                    addFlightRequest.getTo(),
                    addFlightRequest.getCarrier(),
                    LocalDateTime.parse(addFlightRequest.getDepartureTime(), formatter),
                    LocalDateTime.parse(addFlightRequest.getArrivalTime(), formatter));
            this.flightsRepository.saveFlight(flight);
            return flight;
        }
    }

    public void clearFlightList() {
        this.flightsRepository.listFlights().clear();
    }

    public synchronized void deleteFlight(int id) {
        this.flightsRepository.deleteFlight(id);
    }

    public Flight getFlightById(int id) throws NothingFoundException {
        try {
            return this.flightsRepository.listFlights().stream()
                    .filter(f -> f.getId() == id)
                    .toList()
                    .get(0);
        } catch (Exception e) {
            throw new NothingFoundException("No such flight!");
        }
    }

    public List<Airport> searchAirport(String phrase) {
        List<Airport> airportsFrom = this.flightsRepository.listFlights().stream()
                .map(Flight::getFrom)
                .toList()
                .stream()
                .filter(a -> a.containsText(phrase))
                .toList();
        List<Airport> airportsTo = this.flightsRepository.listFlights().stream()
                .map(Flight::getTo)
                .toList()
                .stream()
                .filter(a -> a.containsText(phrase))
                .toList();
        return Stream.concat(airportsFrom.stream(), airportsTo.stream())
                .toList();
    }

    public SearchFlightsResponse searchFlights(SearchFlightRequest searchFlightRequest) throws DepartureAndArrivalAirportAreTheSameException {
        if (searchFlightRequest.getFrom().equalsIgnoreCase(searchFlightRequest.getTo())) {
            throw new DepartureAndArrivalAirportAreTheSameException("Departure and arrival airport are  the same!");
        }
        return this.flightsRepository.searchFlights(searchFlightRequest);
    }
}
