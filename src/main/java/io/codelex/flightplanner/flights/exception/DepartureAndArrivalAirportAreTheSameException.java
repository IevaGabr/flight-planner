package io.codelex.flightplanner.flights.exception;

public class DepartureAndArrivalAirportAreTheSameException extends Exception{
    public DepartureAndArrivalAirportAreTheSameException (String message){
        super(message);
    }
}
