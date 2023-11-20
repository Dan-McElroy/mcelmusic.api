package com.mcelroy.mcelmusic.api.adapters.db.utils;

import com.mcelroy.mcelmusic.api.adapters.db.model.Identifiable;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.reactive.mutiny.Mutiny;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepositoryUtils {

    public static <M, E extends Identifiable> Mono<M> createOrUpdate(E entity,
            Mutiny.SessionFactory sessionFactory, Function<E, M> toModel) {
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
