package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.ArtistDbo;
import com.mcelroy.mcelmusic.api.adapters.db.model.GenreDbo;
import com.mcelroy.mcelmusic.api.adapters.db.model.TrackDbo;
import com.mcelroy.mcelmusic.api.adapters.db.utils.RepositoryUtils;
import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.repository.TrackRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
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
                                .call(track -> Mutiny.fetch(track.getArtists()))
                                .call(track -> Mutiny.fetch(track.getGenre()))
                                .map(TrackDbo::toTrack));
        return RepositoryUtils.convert(findOperation);
    }

    public Mono<Void> delete(@NonNull Track track) {
        var deleteOperation = this.sessionFactory.withSession(session ->
                session.remove(TrackDbo.fromTrack(track)).onItem().call(session::flush));
        return RepositoryUtils.convert(deleteOperation);
    }
}
