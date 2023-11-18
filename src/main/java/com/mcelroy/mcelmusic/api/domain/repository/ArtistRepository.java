package com.mcelroy.mcelmusic.api.domain.repository;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface ArtistRepository {
    Mono<Artist> save(Artist artist);
    Mono<Artist> findById(String artistId);
    Mono<Set<Artist>> findAllById(Set<String> artistIds);
    Mono<Void> delete(Artist artist);
}
