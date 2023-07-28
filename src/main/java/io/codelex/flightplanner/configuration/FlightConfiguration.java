package io.codelex.flightplanner.configuration;

import io.codelex.flightplanner.flights.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlightConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "flights", name = "service.version", havingValue = "in-memory")
    public FlightsService getInMemoryVersion(FlightsInMemoryRepository flightsInMemoryRepository) {
        return new FlightsInMemoryService(flightsInMemoryRepository);
    }


    @Bean
    @ConditionalOnProperty(prefix = "flights", name = "service.version", havingValue = "database")
    public FlightsService getDatabaseVersion(FlightsRepository flightsRepository, AirportRepository airportRepository) {
        return new FlightsDatabaseService(flightsRepository, airportRepository);
    }

}
