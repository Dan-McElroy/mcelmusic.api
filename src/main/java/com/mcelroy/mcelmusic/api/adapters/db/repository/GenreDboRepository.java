package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.GenreDbo;
import com.mcelroy.mcelmusic.api.adapters.db.utils.RepositoryUtils;
import com.mcelroy.mcelmusic.api.domain.model.Genre;
import com.mcelroy.mcelmusic.api.domain.repository.GenreRepository;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@AllArgsConstructor
public class GenreDboRepository implements GenreRepository {

    private Mutiny.SessionFactory sessionFactory;

    public Mono<Genre> save(Genre genre) {
        return RepositoryUtils.createOrUpdate(GenreDbo.fromGenre(genre),
                sessionFactory, GenreDbo::toGenre);
    }

    public Mono<Genre> findById(@NonNull String genreId) {
        var findOperation = this.sessionFactory.withSession(session ->
                session.find(GenreDbo.class, UUID.fromString(genreId))
                        .map(GenreDbo::toGenre));
        return RepositoryUtils.convert(findOperation);
    }

    public Mono<Void> delete(@NonNull Genre genre) {
        return this.sessionFactory.withSession(session ->
                        session.remove(GenreDbo.fromGenre(genre)).onItem().call(session::flush))
                .convert().with(UniReactorConverters.toMono());
    }
}
