package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.ArtistAliasDbo;
import com.mcelroy.mcelmusic.api.domain.repository.ArtistAliasRepository;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.AllArgsConstructor;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ArtistAliasDboRepository implements ArtistAliasRepository {

    private Mutiny.SessionFactory sessionFactory;

    @Override
    public Mono<Set<String>> findAllByArtist(String artistId) {
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(ArtistAliasDbo.class);
        var root = query.from(ArtistAliasDbo.class);
        query.select(root).where(criteriaBuilder.equal(root.get("artist_id"), artistId));

        return sessionFactory.withSession(session ->
                session.createQuery(query)
                        .getResultList()
                        .map(aliases ->
                                aliases.stream()
                                        .map(ArtistAliasDbo::getAlias)
                                        .collect(Collectors.toSet())))
                .convert().with(UniReactorConverters.toMono());
    }

    @Override
    public Mono<Void> createOrUpdate(String artistId, String alias) {
        return null;
    }
}
