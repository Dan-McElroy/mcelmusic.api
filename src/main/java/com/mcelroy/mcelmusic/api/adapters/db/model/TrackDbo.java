package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.Track;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "track")
public final class TrackDbo implements Identifiable {

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

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "artist_tracks",
            joinColumns = { @JoinColumn(name = "track_id") },
            inverseJoinColumns = { @JoinColumn(name = "artist_id") }
    )
    Set<ArtistDbo> artists = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "genre_id")
    GenreDbo genre;

    @Column(name = "length_seconds")
    @Check(constraints = "length_seconds > 0")
    int lengthSeconds;

    /**
     * Converts a {@link Track} domain model into a DBO, excluding association fields.
     * @param track Domain model of a track
     */
    public static TrackDbo fromTrack(Track track) {
        return TrackDbo.builder()
                .id(track.getId() != null ? UUID.fromString(track.getId()) : null)
                .title(track.getTitle())
                .lengthSeconds(track.getLengthSeconds())
                .build();
    }

    /**
     * Converts a Track DBO into a domain model {@link Track},
     * @param trackDbo DBO of a track
     */
    public static Track toTrack(TrackDbo trackDbo) {
        if (trackDbo == null) {
            return null;
        }
        var genre = trackDbo.getGenre() != null
                ? GenreDbo.toGenre(trackDbo.getGenre())
                : null;
        var artists = trackDbo.getArtists()
                .stream().map(ArtistDbo::toArtist)
                .collect(Collectors.toSet());
        return Track.builder()
                .id(trackDbo.getId().toString())
                .version(trackDbo.getVersion())
                .title(trackDbo.getTitle())
                .lengthSeconds(trackDbo.getLengthSeconds())
                .genre(genre)
                .artists(artists)
                .build();
    }
}
