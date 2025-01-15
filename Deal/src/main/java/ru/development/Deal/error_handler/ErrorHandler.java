package ru.development.Deal.error_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handle(MethodArgumentNotValidException exception) {
        log.warn("Exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), exception.getFieldError().getDefaultMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorObject handle(SQLException e) {
        log.error("Exception: {}", e.getMessage());
        return new ErrorObject(HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorObject handle(WebClientResponseException exception) {
        log.warn("Exception: {}", exception.getMessage());
        return new ErrorObject(exception.getStatusText(), exception.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handle(NoObjectFoundException exception) {
        log.warn("Exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), exception.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorObject handle(LoanRefusalException exception) {
        log.warn("Exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), exception.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }
}
