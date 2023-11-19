package com.mcelroy.mcelmusic.api.adapters.db.utils;

import com.mcelroy.mcelmusic.api.adapters.db.model.Identifiable;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import org.hibernate.reactive.mutiny.Mutiny;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class RepositoryUtils {

    public static <T, TDbo extends Identifiable> Mono<T> createOrUpdate(TDbo entity,
            Mutiny.SessionFactory sessionFactory, Function<TDbo, T> toModel) {
        var saveOperation = (entity.getId() == null)
                ? sessionFactory.withSession(session ->
                session.persist(entity)
                        .chain(session::flush)
                        .replaceWith(entity)
                        .map(toModel))
                : sessionFactory.withTransaction(session ->
                session.merge(entity)
                        .onItem()
                        .call(session::flush)
                        .map(toModel));
        return convert(saveOperation);
    }

    public static <T> Mono<T> convert(Uni<T> operation) {
        return operation.convert().with(UniReactorConverters.toMono());
    }
}
