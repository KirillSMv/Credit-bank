package ru.development.Gateway.error_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorObject handle(ErrorProcessingRequest errorProcessingRequest) {
        log.warn("ErrorProcessingRequest: ", errorProcessingRequest);
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorProcessingRequest.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorObject handle(LoanRefusalException loanRefusalException) {
        log.warn("ErrorProcessingRequest: ", loanRefusalException);
        return new ErrorObject(HttpStatus.CONFLICT.getReasonPhrase(), loanRefusalException.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorObject handle(WebClientResponseException webClientResponseException) {
        log.warn("WebClientResponseException: ", webClientResponseException);
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase(), webClientResponseException.getMessage(), LocalDateTime.now());
    }

}
