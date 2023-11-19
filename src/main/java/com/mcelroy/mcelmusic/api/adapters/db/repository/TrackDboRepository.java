package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.ArtistDbo;
import com.mcelroy.mcelmusic.api.adapters.db.model.GenreDbo;
import com.mcelroy.mcelmusic.api.adapters.db.model.TrackDbo;
import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.repository.TrackRepository;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
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

    @Transactional
    public Mono<Track> save(Track track) {
        var genreDbo = GenreDbo.fromGenre(track.getGenre());

        var artistDbos = track.getArtists().stream()
                .map(ArtistDbo::fromArtist)
                .collect(Collectors.toSet());

        var trackDbo = TrackDbo.fromTrack(track);
        trackDbo.setGenre(genreDbo);
        trackDbo.setArtists(artistDbos);
        var saveOperation = (trackDbo.getId() == null)
                ? this.sessionFactory.withSession(session ->
                session.persist(trackDbo)
                        .chain(session::flush)
                        .replaceWith(trackDbo)
                        .map(TrackDbo::toTrack))
                : sessionFactory.withTransaction(session ->
                session.merge(trackDbo)
                        .onItem()
                        .call(session::flush)
                        .map(TrackDbo::toTrack));
        return convert(saveOperation);
    }

    @Transactional
    public Mono<Track> findById(@NonNull String trackId) {
        var findOperation = this.sessionFactory.withSession(session ->
                        session.find(TrackDbo.class, UUID.fromString(trackId))
                                .call(track -> Mutiny.fetch(track.getArtists()))
                                .call(track -> Mutiny.fetch(track.getGenre()))
                                .map(TrackDbo::toTrack));
        return convert(findOperation);
    }

    @Transactional
    public Mono<Void> delete(@NonNull Track track) {
        return this.sessionFactory.withSession(session ->
                session.remove(TrackDbo.fromTrack(track)).onItem().call(session::flush))
                .convert().with(UniReactorConverters.toMono());
    }

    private Mono<Track> convert(Uni<Track> operation) {
        return operation.convert().with(UniReactorConverters.toMono());
    }
}
