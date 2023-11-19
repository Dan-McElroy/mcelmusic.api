package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Genre;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "genre")
public class GenreDbo implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Version
    int version;

    @Builder.Default
    @Column(name = "creation_time")
    @CreationTimestamp
    Timestamp creationTime = Timestamp.from(Instant.now());

    String name;

    @OneToMany(mappedBy = "genre")
    Set<TrackDbo> tracks;

    public static GenreDbo fromGenre(Genre genre) {
        return GenreDbo.builder()
                .id(genre.getId() != null ? UUID.fromString(genre.getId()) : null)
                .version(genre.getVersion())
                .name(genre.getName())
                .build();
    }

    public static Genre toGenre(GenreDbo genreDbo) {
        return Genre.builder()
                .id(genreDbo.getId().toString())
                .name(genreDbo.getName())
                .build();
    }
}
