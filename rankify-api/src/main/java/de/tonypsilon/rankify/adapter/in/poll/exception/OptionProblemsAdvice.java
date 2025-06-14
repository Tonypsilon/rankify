package de.tonypsilon.rankify.adapter.in.poll.exception;

import de.tonypsilon.rankify.domain.DuplicateOptionsException;
import de.tonypsilon.rankify.domain.InvalidOptionException;
import de.tonypsilon.rankify.domain.TooFewPollOptionsException;
import de.tonypsilon.rankify.infrastructure.exception.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(-1)
public class OptionProblemsAdvice {

    @ResponseBody
    @ExceptionHandler(DuplicateOptionsException.class)
    ResponseEntity<ErrorResponse> handleDuplicateOptionsException(DuplicateOptionsException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(TooFewPollOptionsException.class)
    ResponseEntity<ErrorResponse> handleTooFewPollOptionsException(TooFewPollOptionsException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(InvalidOptionException.class)
    ResponseEntity<ErrorResponse> handleInvalidOptionException(InvalidOptionException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(exception.getMessage()));
    }
}
