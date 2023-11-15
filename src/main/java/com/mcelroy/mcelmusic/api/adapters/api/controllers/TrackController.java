package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.service.TrackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Implements API endpoints related to a track.
 *
 * @see "API docs: src/main/resources/public/openapi.yml"
 */
@RestController
@AllArgsConstructor
@RequestMapping("/track")
public class TrackController {

    private final TrackService trackService;

    @GetMapping("{trackId}")
    public Mono<Track> getTrack(@PathVariable String trackId) {
        return trackService.getTrack(trackId);
    }

    @PutMapping
    public Mono<Track> createTrack(@RequestBody TrackCreationParamsDto trackCreationParams) {
        return trackService.createTrack(trackCreationParams);
    }

    @PatchMapping("{trackId}")
    public Mono<Track> updateTrack(@PathVariable String trackId,
                             @RequestBody TrackUpdateParamsDto trackUpdateParams) {
        return trackService.updateTrack(trackId, trackUpdateParams);
    }

    @DeleteMapping("{trackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTrack(@PathVariable String trackId) {
        return trackService.deleteTrack(trackId);
    }
}
