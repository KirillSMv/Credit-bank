package ru.development.calculator.error_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handle(MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), exception.getFieldError().getDefaultMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorObject handle(PrescoringException exception) {
        log.warn("PrescoringException exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), exception.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorObject handle(ScoringException exception) {
        log.warn("ScoringException exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), exception.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handle(IllegalArgumentException exception) {
        log.warn("IllegalArgumentException exception: {}", exception.getMessage());
        return new ErrorObject(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), exception.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }
}
