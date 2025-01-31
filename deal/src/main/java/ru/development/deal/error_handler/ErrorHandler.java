package ru.development.deal.error_handler;

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
    private final DateTimeFormatter timePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handle(MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), exception.getFieldError().getDefaultMessage(), LocalDateTime.now().format(timePattern));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorObject handle(SQLException e) {
        log.error("SQLException exception: {}", e.getMessage());
        return new ErrorObject(HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), e.getMessage(), LocalDateTime.now().format(timePattern));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorObject handle(WebClientResponseException exception) {
        log.warn("WebClientResponseException exception: {}", exception.getMessage());
        return new ErrorObject(exception.getStatusText(), exception.getMessage(), LocalDateTime.now().format(timePattern));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handle(NoObjectFoundException exception) {
        log.warn("NoObjectFoundException exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), exception.getMessage(), LocalDateTime.now().format(timePattern));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorObject handle(LoanRefusalException exception) {
        log.warn("LoanRefusalException exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), exception.getMessage(), LocalDateTime.now().format(timePattern));
    }
}
