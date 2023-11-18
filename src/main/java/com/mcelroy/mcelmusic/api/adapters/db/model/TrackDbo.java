package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "track")
public final class TrackDbo {

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

    String title;

    @ManyToMany()
    @JoinTable(
            name = "track_artist",
            joinColumns = { @JoinColumn(name = "artist_id") },
            inverseJoinColumns = { @JoinColumn(name = "track_id") }
    )
    Set<ArtistDbo> artists;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id")
    GenreDbo genre;

    int lengthSeconds;


    public static TrackDbo fromTrack(Track track) {
        return TrackDbo.builder()
                .id(track.getId())
                .version(track.getVersion())
                .creationTime(null)
                .build();
    }

    public static Track toTrack(TrackDbo trackDbo) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
