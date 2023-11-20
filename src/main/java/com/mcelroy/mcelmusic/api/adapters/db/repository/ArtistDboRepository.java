package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.*;
import com.mcelroy.mcelmusic.api.adapters.db.utils.RepositoryUtils;
import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.repository.ArtistRepository;
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
                        .onItem()
                        .call(artist -> session.fetch(artist.getAliases()))
                        .map(ArtistDbo::toArtist));
        return RepositoryUtils.convert(findOperation);
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

    public Mono<Artist> findNthArtist(int index) {
        var criteriaBuilder = this.sessionFactory.getCriteriaBuilder();
        var artistQuery = criteriaBuilder.createQuery(ArtistDbo.class);
        var root = artistQuery.from(ArtistDbo.class);
        artistQuery.select(root).orderBy(criteriaBuilder.asc(root.get(ArtistDbo_.CREATION_TIME)));

        var queryOperation = this.sessionFactory.withTransaction(session -> session.createQuery(artistQuery)
                .setMaxResults(1).setFirstResult(index)
                .getSingleResult()
                .call(artist -> session.fetch(artist.getAliases()))
                .map(ArtistDbo::toArtist));
        return RepositoryUtils.convert(queryOperation);
    }

    public Mono<Long> count() {
        var criteriaBuilder = this.sessionFactory.getCriteriaBuilder();
        var countQuery = criteriaBuilder.createQuery(Long.class);
        var root = countQuery.from(ArtistDbo.class);
        countQuery.select(criteriaBuilder.count(root));

        var countOperation = this.sessionFactory.withSession(session ->
                session.createQuery(countQuery)
                        .getSingleResult());
        return RepositoryUtils.convert(countOperation);
    }

    public Mono<Void> deleteById(@NonNull String artistId) {
        return RepositoryUtils.deleteById(artistId, ArtistDbo_.ID, ArtistDbo.class, sessionFactory);
    }
}
