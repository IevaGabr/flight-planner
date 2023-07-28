package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.exception.DepartureAndArrivalAirportAreTheSameException;
import io.codelex.flightplanner.flights.exception.FlightAlreadyExistException;
import io.codelex.flightplanner.flights.exception.NothingFoundException;
import io.codelex.flightplanner.flights.request.AddFlightRequest;
import io.codelex.flightplanner.flights.request.SearchFlightRequest;
import io.codelex.flightplanner.flights.response.SearchFlightsResponse;

import java.util.List;

public interface FlightsService {

    Flight addFlight(AddFlightRequest addFlightRequest) throws DepartureAndArrivalAirportAreTheSameException, IllegalArgumentException, FlightAlreadyExistException, NullPointerException;

    void clearFlightList();

    void deleteFlight(Long id);

    Flight getFlightById(Long id) throws NothingFoundException;

    List<Airport> searchAirport(String phrase);

    SearchFlightsResponse searchFlights(SearchFlightRequest searchFlightRequest) throws DepartureAndArrivalAirportAreTheSameException;
}
