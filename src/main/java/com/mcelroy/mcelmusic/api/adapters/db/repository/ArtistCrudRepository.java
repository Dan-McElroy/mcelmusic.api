package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.ArtistDbo;
import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.repository.ArtistRepository;
import lombok.NonNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ArtistCrudRepository extends ReactiveCrudRepository<ArtistDbo, String>, ArtistRepository {

    @Override
    @NonNull
    default Mono<Artist> save(@NonNull Artist artist) {
        return this.save(ArtistDbo.fromArtist(artist))
                .map(ArtistDbo::toArtist);
    }

    @Override
    @NonNull
    default Mono<Artist> findBy(@NonNull String artistId) {
        return this.findById(artistId).map(ArtistDbo::toArtist);
    }

    @Override
    @NonNull
    default Mono<Void> delete(@NonNull Artist artist) {
        return this.delete(ArtistDbo.fromArtist(artist));
    }
}
