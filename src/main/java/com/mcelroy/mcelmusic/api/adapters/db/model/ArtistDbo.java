package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Builder.Default
    int version = 1;

    @Column(name = "name")
    String name;

    @Column(name = "profile_picture_url")
    String profilePictureUrl;

    @Builder.Default
    @Column(name = "creation_time")
    @CreationTimestamp
    Timestamp creationTime = Timestamp.from(Instant.now());

    @OneToMany(mappedBy = "artist")
    Set<ArtistAliasDbo> aliases;

    @ManyToMany(cascade = { CascadeType.ALL }, mappedBy = "artists")
    Set<TrackDbo> tracks;

    /**
     * Converts an {@link Artist} domain model into a DBO, excluding association fields.
     * @param artist Domain model of an artist
     */
    public static ArtistDbo fromArtist(Artist artist) {
        return ArtistDbo.builder()
                .id(artist.getId() != null ? UUID.fromString(artist.getId()) : null)
                .name(artist.getName())
                .profilePictureUrl(artist.getProfilePictureUrl())
                .build();
    }

    /**
     * Converts an Artist DBO into a domain model {@link Artist},
     * @param artistDbo DBO of an artist
     */
    public static Artist toArtist(ArtistDbo artistDbo) {
        return Artist.builder()
                .id(artistDbo.getId().toString())
                .version(artistDbo.getVersion())
                .profilePictureUrl(artistDbo.getProfilePictureUrl())
                .aliases(artistDbo.getAliases().stream().map(ArtistAliasDbo::getAlias).collect(Collectors.toSet()))
                .build();
    }
}
