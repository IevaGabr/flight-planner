package io.codelex.flightplanner.flights.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

@Entity
public class Airport {
    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    @NotNull
    @Id
    private String airport;
    @OneToMany(mappedBy = "from")
    private List<Flight> flightsFrom;

    @OneToMany(mappedBy = "to")
    private List<Flight> flightsTo;

    public Airport() {
    }

    public Airport(String country, String city, String airport) {
        this.country = country;
        this.city = city;
        this.airport = airport;
    }

    public boolean containsText(String text) {
        String textToSearch = text.trim().toLowerCase();
        return this.airport.toLowerCase().contains(textToSearch)
                || this.city.toLowerCase().contains(textToSearch)
                || this.country.toLowerCase().contains(textToSearch);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport1 = (Airport) o;
        return Objects.equals(country, airport1.country) && Objects.equals(city, airport1.city) && Objects.equals(airport, airport1.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, airport);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", airport='" + airport + '\'' +
                '}';
    }
}
