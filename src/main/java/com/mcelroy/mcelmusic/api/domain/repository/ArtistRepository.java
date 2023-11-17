package com.mcelroy.mcelmusic.api.domain.repository;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import reactor.core.publisher.Mono;

public interface ArtistRepository {
    Mono<Artist> save(Artist artist);
    Mono<Artist> findById(String artistId);
    Mono<Void> delete(Artist artist);
}
