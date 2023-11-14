package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackUpdateParamsDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Implements API endpoints related to a track.
 *
 * @see "API docs: src/main/resources/public/openapi.yml"
 */
@RestController
@RequestMapping("/track")
public class TrackController {

    @GetMapping("{trackId}")
    public Mono<Track> getTrack(@PathVariable String trackId) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    @PutMapping
    public Mono<Track> createTrack(@RequestBody TrackCreationParamsDto trackCreationParams) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    @PatchMapping("{trackId}")
    public Mono<Track> updateTrack(@PathVariable String trackId,
                             @RequestBody TrackUpdateParamsDto trackUpdateParams) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    @DeleteMapping("{trackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTrack(@PathVariable String trackId) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }
}
