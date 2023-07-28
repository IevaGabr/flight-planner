package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.exception.DepartureAndArrivalAirportAreTheSameException;
import io.codelex.flightplanner.flights.exception.FlightAlreadyExistException;
import io.codelex.flightplanner.flights.exception.NothingFoundException;
import io.codelex.flightplanner.flights.request.AddFlightRequest;
import io.codelex.flightplanner.flights.request.SearchFlightRequest;
import io.codelex.flightplanner.flights.response.SearchFlightsResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


public class FlightsInMemoryService implements FlightsService {

    private volatile FlightsInMemoryRepository flightsInMemoryRepository;

    public FlightsInMemoryService(FlightsInMemoryRepository flightsInMemoryRepository) {
        this.flightsInMemoryRepository = flightsInMemoryRepository;
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
            if (!this.flightsInMemoryRepository.listFlights().isEmpty() && this.flightsInMemoryRepository.listFlights().stream()
                    .filter(Objects::nonNull)
                    .filter(f -> f.getFrom().equals(addFlightRequest.getFrom()))
                    .filter(f -> f.getTo().equals(addFlightRequest.getTo()))
                    .filter(f -> f.getCarrier().equals(addFlightRequest.getCarrier()))
                    .filter(f -> f.getDepartureTime().isEqual(LocalDateTime.parse(addFlightRequest.getDepartureTime(), formatter)))
                    .anyMatch(f -> f.getArrivalTime().isEqual(LocalDateTime.parse(addFlightRequest.getArrivalTime(), formatter)))) {
                throw new FlightAlreadyExistException("Flight already exists!");
            }
            Flight flight = new Flight(this.flightsInMemoryRepository.listFlights().stream().mapToLong(Flight::getId).max().orElse(0) + 1,
                    addFlightRequest.getFrom(),
                    addFlightRequest.getTo(),
                    addFlightRequest.getCarrier(),
                    LocalDateTime.parse(addFlightRequest.getDepartureTime(), formatter),
                    LocalDateTime.parse(addFlightRequest.getArrivalTime(), formatter));
            this.flightsInMemoryRepository.saveFlight(flight);
            return flight;
        }
    }

    public void clearFlightList() {
        this.flightsInMemoryRepository.listFlights().clear();
    }

    public synchronized void deleteFlight(Long id) {
        this.flightsInMemoryRepository.deleteFlight(id);
    }

    public Flight getFlightById(Long id) throws NothingFoundException {
        try {
            return this.flightsInMemoryRepository.listFlights().stream()
                    .filter(f -> f.getId() == id)
                    .toList()
                    .get(0);
        } catch (Exception e) {
            throw new NothingFoundException("No such flight!");
        }
    }

    public List<Airport> searchAirport(String phrase) {
        List<Airport> airportsFrom = this.flightsInMemoryRepository.listFlights().stream()
                .map(Flight::getFrom)
                .toList()
                .stream()
                .filter(a -> a.containsText(phrase))
                .toList();
        List<Airport> airportsTo = this.flightsInMemoryRepository.listFlights().stream()
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
        return this.flightsInMemoryRepository.searchFlights(searchFlightRequest);
    }
}
