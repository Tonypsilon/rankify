package de.tonypsilon.rankify.adapter.in.poll.exception;

import de.tonypsilon.rankify.domain.IllegalPollStateChangeException;
import de.tonypsilon.rankify.infrastructure.exception.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(-1)
public class IllegalPollStateChangeExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(IllegalPollStateChangeException.class)
    public ResponseEntity<ErrorResponse> handleIllegalPollStateChangeException(IllegalPollStateChangeException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(exception.getMessage()));
    }
}
