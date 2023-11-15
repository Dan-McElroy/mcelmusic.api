package com.mcelroy.mcelmusic.api.adapters.api.error;


import com.mcelroy.mcelmusic.api.domain.model.error.InvalidParametersException;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<Object>> handleInvalidParametersException(
            InvalidParametersException exception,
            ServerWebExchange exchange) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getReason()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<Object>> handleNotFoundException(
            NotFoundException exception,
            ServerWebExchange exchange) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getReason()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(VersionConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ResponseEntity<Object>> handleVersionConflictException(
            VersionConflictException exception,
            ServerWebExchange exchange) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getReason()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity<Object>> handleAllUncaughtException(
            RuntimeException exception,
            ServerWebExchange exchange) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage()));
    }
}