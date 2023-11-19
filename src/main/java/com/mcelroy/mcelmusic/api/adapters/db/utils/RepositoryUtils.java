package com.mcelroy.mcelmusic.api.adapters.db.utils;

import com.mcelroy.mcelmusic.api.adapters.db.model.GenreDbo;
import com.mcelroy.mcelmusic.api.adapters.db.model.GenreDbo_;
import com.mcelroy.mcelmusic.api.adapters.db.model.Identifiable;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import org.hibernate.reactive.mutiny.Mutiny;
import reactor.core.publisher.Mono;

import java.util.UUID;
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

    public static <T> Mono<Void> deleteById(String entityId, String idAttributeName,
            Class<T> entityClass, Mutiny.SessionFactory sessionFactory) {
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();
        var delete = criteriaBuilder.createCriteriaDelete(entityClass);
        var root = delete.from(entityClass);
        delete.where(criteriaBuilder.equal(root.get(idAttributeName), UUID.fromString(entityId)));
        var deleteOperation = sessionFactory
                .withSession(session -> session.createQuery(delete)
                        .executeUpdate().replaceWithVoid());
        return convert(deleteOperation);
    }

    public static <T> Mono<T> convert(Uni<T> operation) {
        return operation.convert().with(UniReactorConverters.toMono());
    }
}
