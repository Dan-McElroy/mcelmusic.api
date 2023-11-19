package com.mcelroy.mcelmusic.api.adapters.db.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "artist_alias")
public class ArtistAliasDbo implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "artist_id", nullable = false)
    private ArtistDbo artist;

    String alias;

    public static ArtistAliasDbo fromAlias(String alias) {
        return ArtistAliasDbo.builder().alias(alias).build();
    }
}
