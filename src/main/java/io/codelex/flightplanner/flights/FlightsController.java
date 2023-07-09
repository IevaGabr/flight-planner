package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.exception.DepartureAndArrivalAirportAreTheSameException;
import io.codelex.flightplanner.flights.exception.FlightAlreadyExistException;
import io.codelex.flightplanner.flights.exception.NothingFoundException;
import io.codelex.flightplanner.flights.request.AddFlightRequest;
import io.codelex.flightplanner.flights.request.SearchFlightRequest;
import io.codelex.flightplanner.flights.response.SearchFlightsResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
public class FlightsController {

    private final FlightsService flightsService;

    public FlightsController(FlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @PostMapping("/testing-api/clear")
    public void clearFlights() {
        this.flightsService.clearFlightList();
    }

    @PutMapping("/admin-api/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public Flight addFlight(@Valid @RequestBody AddFlightRequest addFlightRequest) {
        try {
            return this.flightsService.addFlight(addFlightRequest);
        } catch (DepartureAndArrivalAirportAreTheSameException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (FlightAlreadyExistException e){
            throw new ResponseStatusException((HttpStatus.CONFLICT));
        }
    }

    @DeleteMapping("/admin-api/flights/{id}")
    public void deleteFlight(@PathVariable("id") int id){
        this.flightsService.deleteFlight(id);
    }

    @GetMapping ("/admin-api/flights/{id}")
    public Flight getFlight(@PathVariable("id") int id){
        try {
            return this.flightsService.getFlightById(id);
        } catch (NothingFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "api/airports")
    @ResponseStatus(HttpStatus.OK)
    public List<Airport> searchAirport(@RequestParam(name = "search") String phrase){
            return this.flightsService.searchAirport(phrase);
    }

    @PostMapping("/api/flights/search")
    @ResponseStatus(HttpStatus.OK)
    public SearchFlightsResponse searchFlights (@RequestBody SearchFlightRequest searchFlightRequest){
        try {
            return this.flightsService.searchFlights(searchFlightRequest);
        } catch (DepartureAndArrivalAirportAreTheSameException| NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/flights/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Flight searchFlightById (@PathVariable("id") int id){
        try {
            return this.flightsService.getFlightById(id);
        } catch (NothingFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }



}
