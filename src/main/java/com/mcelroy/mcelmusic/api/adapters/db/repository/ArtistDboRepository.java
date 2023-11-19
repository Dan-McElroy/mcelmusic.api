package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.ArtistAliasDbo;
import com.mcelroy.mcelmusic.api.adapters.db.model.ArtistDbo;
import com.mcelroy.mcelmusic.api.adapters.db.utils.RepositoryUtils;
import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.repository.ArtistRepository;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ArtistDboRepository implements ArtistRepository {

    private Mutiny.SessionFactory sessionFactory;

    public Mono<Artist> save(Artist artist) {
        var artistDbo = ArtistDbo.fromArtist(artist);

        artistDbo.setAliases(artist.getAliases().stream()
                .map(ArtistAliasDbo::fromAlias)
                .collect(Collectors.toSet()));

        return RepositoryUtils.createOrUpdate(artistDbo, sessionFactory, ArtistDbo::toArtist);
    }

    public Mono<Artist> findById(@NonNull String artistId) {
        var findOperation = this.sessionFactory.withSession(session ->
                session.find(ArtistDbo.class, UUID.fromString(artistId))
                        .map(ArtistDbo::toArtist));
        return convert(findOperation);
    }

    public Mono<Set<Artist>> findAllById(@NonNull Set<String> artistIds) {
        var artistUuids = artistIds.stream().map(UUID::fromString).toArray();
        return this.sessionFactory.withSession(session ->
                        session.find(ArtistDbo.class, artistUuids))
                                .map(artistDbos -> artistDbos.stream()
                                        .map(ArtistDbo::toArtist)
                                        .collect(Collectors.toSet()))
                                .convert().with(UniReactorConverters.toMono());
    }

    public Mono<Void> delete(@NonNull Artist artist) {
        return this.sessionFactory.withSession(session ->
                        session.remove(ArtistDbo.fromArtist(artist)).onItem().call(session::flush))
                .convert().with(UniReactorConverters.toMono());
    }

    private static Mono<Artist> convert(Uni<Artist> operation) {
        return operation.convert().with(UniReactorConverters.toMono());
    }
}
