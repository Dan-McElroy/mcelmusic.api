package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackUpdateParamsDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/track")
public class TrackController {

    @GetMapping("{trackId}")
    public Track getTrack(@PathVariable String trackId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @PutMapping
    public Track createTrack(@RequestBody TrackCreationParamsDto trackCreationParams) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @PatchMapping("{trackId}")
    public Track updateTrack(@PathVariable String trackId,
                             @RequestBody TrackUpdateParamsDto trackUpdateParams) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @DeleteMapping("{trackId}")
    public void deleteTrack(@PathVariable String trackId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
