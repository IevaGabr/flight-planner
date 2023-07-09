package io.codelex.flightplanner.flights.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DepartureAndArrivalAirportAreTheSameException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleArithmeticExceptions(DepartureAndArrivalAirportAreTheSameException e, WebRequest request){
        String bodyOfResponse = "Departure and arrival airport are  the same!";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST,request);
    }

    @ExceptionHandler({FlightAlreadyExistException.class})
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleArithmeticExceptions(FlightAlreadyExistException e, WebRequest request){
        String bodyOfResponse = "Departure and arrival airport are  the same!";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT,request);
    }

    @ExceptionHandler({NothingFoundException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleArithmeticExceptions(ArithmeticException e, WebRequest request){
        String bodyOfResponse = "Nothing found";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND,request);
    }


}
