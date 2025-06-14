package de.tonypsilon.rankify.adapter.in.poll.exception;

import de.tonypsilon.rankify.domain.NoRankingsException;
import de.tonypsilon.rankify.domain.PollNotActiveException;
import de.tonypsilon.rankify.infrastructure.exception.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(-1)
public class CastBallotExceptionsAdvice {

    @ResponseBody
    @ExceptionHandler(NoRankingsException.class)
    public ResponseEntity<ErrorResponse> handleNoRankingsException(NoRankingsException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(PollNotActiveException.class)
    public ResponseEntity<ErrorResponse> handlePollNotActiveException(PollNotActiveException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(exception.getMessage()));
    }
}
