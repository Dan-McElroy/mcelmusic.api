package com.mcelroy.mcelmusic.api.adapters.db.repository;

import com.mcelroy.mcelmusic.api.adapters.db.model.GenreDbo;
import com.mcelroy.mcelmusic.api.domain.model.Genre;
import com.mcelroy.mcelmusic.api.domain.repository.GenreRepository;
import io.smallrye.mutiny.Uni;
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
        var genreDbo = GenreDbo.fromGenre(genre);
        var saveOperation = (genreDbo.getId() == null)
                ? this.sessionFactory.withSession(session ->
                session.persist(genreDbo)
                        .chain(session::flush)
                        .replaceWith(genreDbo))
                : sessionFactory.withTransaction(session ->
                session.merge(genreDbo)
                        .onItem()
                        .call(session::flush));
        return convert(saveOperation);
    }

    public Mono<Genre> findById(@NonNull String genreId) {
        var findOperation = this.sessionFactory.withSession(session ->
                session.find(GenreDbo.class, UUID.fromString(genreId)));
        return convert(findOperation);
    }

    public Mono<Void> delete(@NonNull Genre genre) {
        return this.sessionFactory.withSession(session ->
                        session.remove(GenreDbo.fromGenre(genre)).onItem().call(session::flush))
                .convert().with(UniReactorConverters.toMono());
    }

    private static Mono<Genre> convert(Uni<GenreDbo> operation) {
        return operation.map(GenreDbo::toGenre)
                .convert().with(UniReactorConverters.toMono());
    }
}
