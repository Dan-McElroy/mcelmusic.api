package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "artist")
public class ArtistDbo {

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

    @OneToMany(mappedBy = "artist")
    Set<ArtistAliasDbo> aliases;

    @ManyToMany(cascade = { CascadeType.ALL })
    Set<TrackDbo> tracks;

    public static ArtistDbo fromArtist(Artist artist) {
        // DON'T DO TRACKS IN HERE
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Artist toArtist(ArtistDbo artist) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
