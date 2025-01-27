package ru.development.statement.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private static DateTimeFormatter time_pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handle(LoanRefusalException exception) {
        log.warn("LoanRefusalException exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), exception.getMessage(), LocalDateTime.now().format(time_pattern));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorObject handle(WebClientResponseException exception) {
        log.warn("WebClientResponseException exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase().toUpperCase(), exception.getMessage(), LocalDateTime.now().format(time_pattern));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handle(ErrorProcessingRequest exception) {
        log.warn("ErrorProcessingRequest exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), exception.getMessage(), LocalDateTime.now().format(time_pattern));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handle(MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), exception.getFieldError().getDefaultMessage(), LocalDateTime.now().format(time_pattern));
    }
}
