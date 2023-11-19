package com.mcelroy.mcelmusic.api.domain.repository;

import com.mcelroy.mcelmusic.api.domain.model.Genre;
import reactor.core.publisher.Mono;

public interface GenreRepository {
    Mono<Genre> save(Genre genre);
    Mono<Genre> findById(String genreId);
    Mono<Void> deleteById(String genreId);
}
