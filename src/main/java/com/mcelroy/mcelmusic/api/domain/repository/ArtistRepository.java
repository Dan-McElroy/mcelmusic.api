package com.mcelroy.mcelmusic.api.domain.repository;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.Track;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ArtistRepository {
    Mono<Artist> save(Artist artist);
    Mono<Artist> findBy(String artistId);
    Mono<Void> delete(Artist artist);
}
