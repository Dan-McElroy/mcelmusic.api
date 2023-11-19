package com.mcelroy.mcelmusic.api.adapters.api.error;


import com.mcelroy.mcelmusic.api.domain.model.dto.ErrorDto;
import com.mcelroy.mcelmusic.api.domain.model.error.InvalidParametersException;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity<Object>> handleAllUncaughtException(RuntimeException exception) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDto.builder().failureReason(exception.getMessage()).build()));
    }

    @ExceptionHandler(InvalidParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<Object>> handleInvalidParametersException(InvalidParametersException exception) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.builder()
                        .failureReason(exception.getReason())
                        .details(Map.of("invalidParameters", exception.getInvalidParameters()))
                        .build()));
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<Object>> handleNotFoundException(NotFoundException exception) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorDto.builder().failureReason(exception.getReason()).build()));
    }

    @ExceptionHandler(VersionConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ResponseEntity<Object>> handleVersionConflictException(VersionConflictException exception) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorDto.builder().failureReason(exception.getReason()).build()));
    }
}