package com.mcelroy.mcelmusic.api.domain.repository;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import reactor.core.publisher.Mono;

public interface TrackRepository {

    Mono<Track> save(Track track);
    Mono<Track> findBy(String trackId);
    Mono<Void> delete(Track track);

}
