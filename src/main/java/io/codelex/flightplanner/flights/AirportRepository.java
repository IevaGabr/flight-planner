package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, String> {

    boolean existsByAirportAndCityAndCountry(String airport, String city, String Country);

    @Query("SELECT a FROM Airport a WHERE lower(a.city)  like ('%' || :search || '%') or lower(a.airport)  like ('%' || :search || '%') or lower(a.country)  like ('%' || :search || '%')")
    List<Airport> searchAirportByPhrase(@Param("search") String phrase);

}
