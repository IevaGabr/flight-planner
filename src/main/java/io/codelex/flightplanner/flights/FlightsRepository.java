package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightsRepository extends JpaRepository<Flight, Long> {

    boolean existsByCarrierAndFromAndToAndDepartureTimeAndArrivalTime(String carrier,
                                                                      Airport from,
                                                                      Airport to,
                                                                      LocalDateTime arrivalTime,
                                                                      LocalDateTime departureTime);

    Flight searchFlightByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(Airport from,
                                                                           Airport to,
                                                                           String carrier,
                                                                           LocalDateTime arrivalTime,
                                                                           LocalDateTime departureTime);

    @Query("SELECT f FROM Flight f WHERE f.from.airport like ('%' || :from || '%') and f.to.airport like ('%' || :to || '%') and Date(f.departureTime)=  :depDate")
    List<Flight> searchFlights(@Param("from") String from, @Param("to") String to, @Param("depDate") LocalDate departureDate);
}
