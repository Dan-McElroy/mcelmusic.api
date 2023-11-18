package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "genre")
public class GenreDbo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid")
    String id;

    @Version
    int version;

    @Builder.Default
    @Column(name = "creation_time")
    @CreationTimestamp
    Timestamp creationTime = Timestamp.from(Instant.now());

    String name;

    public static GenreDbo fromGenre(Genre genre) {
        return null;
    }

    public static Genre toGenre(GenreDbo genreDbo) {
        return null;
    }
}
