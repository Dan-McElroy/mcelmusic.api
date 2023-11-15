package com.mcelroy.mcelmusic.api.domain.service;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackUpdateParamsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TrackService {

    public Mono<Track> createTrack(TrackCreationParamsDto creationParamsDto) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Track> getTrack(String trackId) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Track> updateTrack(String trackId, TrackUpdateParamsDto updateParamsDto) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Void> deleteTrack(String trackId) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }
}
