package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.exception.DepartureAndArrivalAirportAreTheSameException;
import io.codelex.flightplanner.flights.exception.FlightAlreadyExistException;
import io.codelex.flightplanner.flights.exception.NothingFoundException;
import io.codelex.flightplanner.flights.request.AddFlightRequest;
import io.codelex.flightplanner.flights.request.SearchFlightRequest;
import io.codelex.flightplanner.flights.response.SearchFlightsResponse;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlightsDatabaseService implements FlightsService {

    private FlightsRepository flightsRepository;

    private AirportRepository airportRepository;

    public FlightsDatabaseService(FlightsRepository flightsRepository, AirportRepository airportRepository) {
        this.flightsRepository = flightsRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    @Transactional
    public Flight addFlight(AddFlightRequest addFlightRequest) throws DepartureAndArrivalAirportAreTheSameException, IllegalArgumentException, FlightAlreadyExistException, NullPointerException {
        if (addFlightRequest.isAirportFromAndToEqual()) {
            throw new DepartureAndArrivalAirportAreTheSameException("Departure and arrival airport are  the same!");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (!addFlightRequest.isCorrectArrivalTime()) {
            throw new IllegalArgumentException();
        }
        if (!this.airportRepository.existsByAirportAndCityAndCountry(addFlightRequest.getFrom().getAirport(),
                addFlightRequest.getFrom().getCity(),
                addFlightRequest.getFrom().getCountry())) {
            Airport from = new Airport();
            from.setAirport(addFlightRequest.getFrom().getAirport());
            from.setCity(addFlightRequest.getFrom().getCity());
            from.setCountry(addFlightRequest.getFrom().getCountry());
            this.airportRepository.save(from);
        }
        if (!this.airportRepository.existsByAirportAndCityAndCountry(addFlightRequest.getTo().getAirport(),
                addFlightRequest.getTo().getCity(),
                addFlightRequest.getTo().getCountry())) {
            Airport to = new Airport();
            to.setAirport(addFlightRequest.getTo().getAirport());
            to.setCity(addFlightRequest.getTo().getCity());
            to.setCountry(addFlightRequest.getTo().getCountry());
            this.airportRepository.save(to);
        }
        if (this.flightsRepository.existsByCarrierAndFromAndToAndDepartureTimeAndArrivalTime(addFlightRequest.getCarrier(),
                addFlightRequest.getFrom(), addFlightRequest.getTo(), LocalDateTime.parse(addFlightRequest.getDepartureTime(), formatter), LocalDateTime.parse(addFlightRequest.getArrivalTime(), formatter))) {
            throw new FlightAlreadyExistException("Flight already exists!");
        }
        Flight newFlight = new Flight();
        newFlight.setCarrier(addFlightRequest.getCarrier());
        newFlight.setFrom(addFlightRequest.getFrom());
        newFlight.setTo(addFlightRequest.getTo());
        newFlight.setDepartureTime(LocalDateTime.parse(addFlightRequest.getDepartureTime(), formatter));
        newFlight.setArrivalTime(LocalDateTime.parse(addFlightRequest.getArrivalTime(), formatter));
        this.flightsRepository.save(newFlight);
        return this.flightsRepository.searchFlightByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(addFlightRequest.getFrom(),
                addFlightRequest.getTo(),
                addFlightRequest.getCarrier(),
                LocalDateTime.parse(addFlightRequest.getDepartureTime(), formatter),
                LocalDateTime.parse(addFlightRequest.getArrivalTime(), formatter));
    }

    @Override
    public void clearFlightList() {
        this.flightsRepository.deleteAll();
        this.airportRepository.deleteAll();
    }

    @Override
    public void deleteFlight(Long id) {
        this.flightsRepository.deleteById(id);
    }

    @Override
    public Flight getFlightById(Long id) throws NothingFoundException {
        return this.flightsRepository.findById(id).orElseThrow(() -> new NothingFoundException("No such flight!"));
    }

    @Override
    public List<Airport> searchAirport(String phrase) {
        return this.airportRepository.searchAirportByPhrase(phrase.toLowerCase().trim());
    }

    @Override
    public SearchFlightsResponse searchFlights(SearchFlightRequest searchFlightRequest) throws DepartureAndArrivalAirportAreTheSameException {
        if (searchFlightRequest.getFrom().equalsIgnoreCase(searchFlightRequest.getTo())) {
            throw new DepartureAndArrivalAirportAreTheSameException("Departure and arrival airport are  the same!");
        }
        List<Flight> flights = this.flightsRepository.searchFlights(searchFlightRequest.getFrom(),
                searchFlightRequest.getTo(),
                LocalDate.parse(searchFlightRequest.getDepartureDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return new SearchFlightsResponse(flights.size(), flights.size(), flights);
    }
}
