package com.mcelroy.mcelmusic.api.domain.repository;

import reactor.core.publisher.Mono;

import java.util.Set;

public interface ArtistAliasRepository {

    Mono<Set<String>> findAllByArtist(String artistId);

    Mono<Void> createOrUpdate(String artistId, String alias);

}
