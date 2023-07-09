package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Flight;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightsRepository {
    private List<Flight> savedFlights = new ArrayList<>();

    public void saveFlight(Flight flight) {
        this.savedFlights.add(flight);
    }

    public List<Flight> listFlights() {
        return this.savedFlights;
    }

    public void deleteFlight(int id) {
        for (Flight flight:this.savedFlights) {
            if (flight.getId()==id){
                savedFlights.remove(flight);
                break;
            }
        }
    }

}
