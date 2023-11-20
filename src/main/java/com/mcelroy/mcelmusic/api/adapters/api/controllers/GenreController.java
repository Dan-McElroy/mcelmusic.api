package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Genre;
import com.mcelroy.mcelmusic.api.domain.model.dto.GenreCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.GenreUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.service.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Implements API endpoints related to a genre.
 *
 * @see "API docs: src/main/resources/public/openapi.yml"
 */
@RestController
@AllArgsConstructor
@RequestMapping("/v1/genre")
public class GenreController {

    private final GenreService genreService;

    @GetMapping("{genreId}")
    public Mono<Genre> getGenre(@PathVariable String genreId) {
        return genreService.getGenre(genreId);
    }

    @PutMapping
    public Mono<Genre> createGenre(@RequestBody GenreCreationParamsDto genreCreationParams) {
        return genreService.createGenre(genreCreationParams);
    }

    @PatchMapping("{genreId}")
    public Mono<Genre> updateGenre(@PathVariable String genreId,
                                   @RequestBody GenreUpdateParamsDto genreUpdateParams) {
        return genreService.updateGenre(genreId, genreUpdateParams);
    }

    @DeleteMapping("{genreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGenre(@PathVariable String genreId) {
        return genreService.deleteGenre(genreId);
    }
}
