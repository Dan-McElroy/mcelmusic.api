package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.ArtistDbo;
import com.mcelroy.mcelmusic.api.adapters.db.model.GenreDbo;
import com.mcelroy.mcelmusic.api.adapters.db.model.TrackDbo;
import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.repository.TrackRepository;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

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
        var saveOperation = (trackDbo.getId() == null)
                ? this.sessionFactory.withSession(session ->
                session.persist(trackDbo)
                        .chain(session::flush)
                        .replaceWith(trackDbo))
                : sessionFactory.withTransaction(session ->
                session.merge(trackDbo)
                        .onItem()
                        .call(session::flush));
        return convert(saveOperation);
    }

    public Mono<Track> findById(@NonNull String trackId) {
        var findOperation = this.sessionFactory.withSession(session ->
                        session.find(TrackDbo.class, trackId));
        return convert(findOperation);
    }

    public Mono<Void> delete(@NonNull Track track) {
        return this.sessionFactory.withSession(session ->
                session.remove(TrackDbo.fromTrack(track)).onItem().call(session::flush))
                .convert().with(UniReactorConverters.toMono());
    }

    private static Mono<Track> convert(Uni<TrackDbo> operation) {
        return operation.map(TrackDbo::toTrack)
                .convert().with(UniReactorConverters.toMono());
    }
}
