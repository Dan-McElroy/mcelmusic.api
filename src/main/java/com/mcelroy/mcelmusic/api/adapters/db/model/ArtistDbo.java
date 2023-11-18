package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
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
@Table(name = "artist")
public class ArtistDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Version
    int version;

    @Builder.Default
    @Column(name = "creation_time")
    @CreationTimestamp
    Timestamp creationTime = Timestamp.from(Instant.now());

    @OneToMany(mappedBy = "artist")
    Set<ArtistAliasDbo> aliases;

    @ManyToMany(cascade = { CascadeType.ALL }, mappedBy = "artists")
    Set<TrackDbo> tracks;

    public static ArtistDbo fromArtist(Artist artist) {
        // DON'T DO TRACKS IN HERE
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Artist toArtist(ArtistDbo artist) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
