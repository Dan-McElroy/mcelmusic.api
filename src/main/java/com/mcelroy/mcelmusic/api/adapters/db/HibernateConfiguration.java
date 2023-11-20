package com.mcelroy.mcelmusic.api.adapters.db;

import jakarta.persistence.Persistence;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {

    @Bean
    public Mutiny.SessionFactory sessionFactory() {
        try (var entityManagerFactory = Persistence.createEntityManagerFactory("metadata-pu")) {
            return entityManagerFactory.unwrap(Mutiny.SessionFactory.class);
        }
    }
}
