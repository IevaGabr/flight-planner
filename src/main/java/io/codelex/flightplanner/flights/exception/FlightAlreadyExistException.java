package io.codelex.flightplanner.flights.exception;

public class FlightAlreadyExistException extends Exception{
    public FlightAlreadyExistException(String message){
        super(message);
    }
}
