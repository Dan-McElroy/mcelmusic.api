package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistUpdateParamsDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Implements API endpoints related to an artist.
 *
 * @see "API docs: src/main/resources/public/openapi.yml"
 */
@RestController
@RequestMapping("/artist")
public class ArtistController {

    @GetMapping("today")
    public Mono<Artist> getArtistOfTheDay() {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    @GetMapping("{artistId}")
    public Mono<Artist> getArtist(@PathVariable String artistId) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    @PutMapping
    public Mono<Artist> createArtist(@RequestBody ArtistCreationParamsDto artistCreationParams) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    @PatchMapping("{artistId}")
    public Mono<Artist> updateArtist(@PathVariable String artistId,
                             @RequestBody ArtistUpdateParamsDto artistUpdateParams) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    @DeleteMapping("{artistId}")
    public Mono<Void> deleteArtist(@PathVariable String artistId) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }
}
