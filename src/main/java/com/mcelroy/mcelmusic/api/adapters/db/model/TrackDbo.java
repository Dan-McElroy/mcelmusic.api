package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
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
@Table(name = "track")
public final class TrackDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Version
    int version;

    @Builder.Default
    @Column(name = "creation_time")
    @CreationTimestamp
    Timestamp creationTime = Timestamp.from(Instant.now());

    String title;

    @ManyToMany()
    @JoinTable(
            name = "artist_tracks",
            joinColumns = { @JoinColumn(name = "artist_id") },
            inverseJoinColumns = { @JoinColumn(name = "track_id") }
    )
    Set<ArtistDbo> artists;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id")
    GenreDbo genre;

    @Column(name = "length_seconds")
    @Check(constraints = "length_seconds > 0")
    int lengthSeconds;

    public static TrackDbo fromTrack(Track track) {
        return TrackDbo.builder()
                .id(UUID.fromString(track.getId()))
                .version(track.getVersion())
                .creationTime(null)
                .build();
    }

    public static Track toTrack(TrackDbo trackDbo) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
