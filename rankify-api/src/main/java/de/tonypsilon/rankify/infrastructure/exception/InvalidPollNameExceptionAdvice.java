package de.tonypsilon.rankify.infrastructure.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(-1)
public class InvalidPollNameExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(InvalidPollNameException.class)
    ResponseEntity<ErrorResponse> handleInvalidPollNameException(InvalidPollNameException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Invalid poll name provided"));
    }
}
