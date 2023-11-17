package com.mcelroy.mcelmusic.api.adapters.db;

import jakarta.persistence.Persistence;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {

    @Bean
    public Mutiny.SessionFactory sessionFactory() {
        return Persistence.createEntityManagerFactory("metadata-pu")
                .unwrap(Mutiny.SessionFactory.class);
    }
}
