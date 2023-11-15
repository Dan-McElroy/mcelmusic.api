package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.TrackDbo;
import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.repository.TrackRepository;
import lombok.NonNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TrackCrudRepository extends ReactiveCrudRepository<TrackDbo, String>, TrackRepository {
    @Override
    @NonNull
    default Mono<Track> save(@NonNull Track track) {
        return this.save(TrackDbo.fromTrack(track))
                .map(TrackDbo::toTrack);
    }

    @Override
    @NonNull
    default Mono<Track> findBy(@NonNull String trackId) {
        return this.findById(trackId).map(TrackDbo::toTrack);
    }


    @Override
    @NonNull
    default Mono<Void> delete(@NonNull Track track) {
        return this.delete(TrackDbo.fromTrack(track));
    }
}
