package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.service.ArtistService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Implements API endpoints related to an artist.
 *
 * @see "API docs: src/main/resources/public/openapi.yml"
 */
@RestController
@AllArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping("today")
    public Mono<Artist> getArtistOfTheDay(ServerWebExchange exchange) {
        return exchange.getSession().flatMap(
                session -> artistService.getArtistOfTheDay(session.getLastAccessTime()));
    }

    @GetMapping("{artistId}")
    public Mono<Artist> getArtist(@PathVariable String artistId) {
        return artistService.getArtist(artistId);
    }

    @PutMapping
    public Mono<Artist> createArtist(@RequestBody ArtistCreationParamsDto artistCreationParams) {
        return artistService.createArtist(artistCreationParams);
    }

    @PatchMapping("{artistId}")
    public Mono<Artist> updateArtist(@PathVariable String artistId,
                             @RequestBody ArtistUpdateParamsDto artistUpdateParams) {
        return artistService.updateArtist(artistId, artistUpdateParams);
    }

    @DeleteMapping("{artistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteArtist(@PathVariable String artistId) {
        return artistService.deleteArtist(artistId);
    }
}
