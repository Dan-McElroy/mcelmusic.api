package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Genre;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
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

    @Column(name = "creation_time", insertable = false)
    @CreationTimestamp(source = SourceType.DB)
    Instant creationTime;

    String name;

    @Builder.Default
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "genre")
    Set<TrackDbo> tracks = new HashSet<>();

    public static GenreDbo fromGenre(Genre genre) {
        return GenreDbo.builder()
                .id(genre.getId() != null ? UUID.fromString(genre.getId()) : null)
                .name(genre.getName())
                .build();
    }

    public static Genre toGenre(GenreDbo genreDbo) {
        return Genre.builder()
                .id(genreDbo.getId().toString())
                .version(genreDbo.getVersion())
                .name(genreDbo.getName())
                .build();
    }
}
