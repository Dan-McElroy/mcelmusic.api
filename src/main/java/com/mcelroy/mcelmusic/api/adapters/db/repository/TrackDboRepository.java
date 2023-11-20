package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.*;
import com.mcelroy.mcelmusic.api.adapters.db.utils.RepositoryUtils;
import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.repository.TrackRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@Slf4j
public class TrackDboRepository implements TrackRepository {

    private Mutiny.SessionFactory sessionFactory;

    public Mono<Track> save(Track track) {
        var genreDbo = GenreDbo.fromGenre(track.getGenre());

        var artistDbos = track.getArtists().stream()
                .map(ArtistDbo::fromArtist)
                .collect(Collectors.toSet());

        var trackDbo = TrackDbo.fromTrack(track);
        trackDbo.setGenre(genreDbo);
        trackDbo.setArtists(artistDbos);
        return RepositoryUtils.createOrUpdate(trackDbo, sessionFactory, TrackDbo::toTrack);
    }

    public Mono<Track> findById(@NonNull String trackId) {
        var findOperation = this.sessionFactory.withSession(session ->
                        session.find(TrackDbo.class, UUID.fromString(trackId))
                                .call(track -> Mutiny.fetch(track.getGenre()))
                                .call(track -> Mutiny.fetch(track.getArtists()).log())
                                .map(TrackDbo::toTrack));
        return RepositoryUtils.convert(findOperation);
    }

    public Mono<Void> deleteById(@NonNull String trackId) {
        return RepositoryUtils.deleteById(trackId, TrackDbo_.ID, TrackDbo.class, sessionFactory);
    }
}
