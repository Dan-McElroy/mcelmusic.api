package com.mcelroy.mcelmusic.api.domain.repository;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ArtistRepository extends ReactiveCrudRepository<Artist, String> {
}
