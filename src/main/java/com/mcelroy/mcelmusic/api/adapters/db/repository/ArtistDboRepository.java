package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.ArtistDbo;
import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.repository.ArtistRepository;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class ArtistDboRepository implements ArtistRepository {

    private Mutiny.SessionFactory sessionFactory;

    public Mono<Artist> save(Artist artist) {
        var artistDbo = ArtistDbo.fromArtist(artist);
        var saveOperation = (artistDbo.getId() == null)
                ? this.sessionFactory.withSession(session ->
                session.persist(artistDbo)
                        .chain(session::flush)
                        .replaceWith(artistDbo))
                : sessionFactory.withTransaction(session ->
                session.merge(artistDbo)
                        .onItem()
                        .call(session::flush));
        return convert(saveOperation);
    }

    public Mono<Artist> findById(@NonNull String artistId) {
        var findOperation = this.sessionFactory.withSession(session ->
                session.find(ArtistDbo.class, artistId));
        return convert(findOperation);
    }

    public Mono<Void> delete(@NonNull Artist artist) {
        return this.sessionFactory.withSession(session ->
                        session.remove(ArtistDbo.fromArtist(artist)).onItem().call(session::flush))
                .convert().with(UniReactorConverters.toMono());
    }

    private static Mono<Artist> convert(Uni<ArtistDbo> operation) {
        return operation.map(ArtistDbo::toArtist)
                .convert().with(UniReactorConverters.toMono());
    }
}
