package com.mcelroy.mcelmusic.api.adapters.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "artist_alias")
public class ArtistAliasDbo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid")
    String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "artist_id", nullable = false)
    private ArtistDbo artist;

    String alias;

    public static ArtistAliasDbo fromAlias(String alias) {
        return ArtistAliasDbo.builder().alias(alias).build();
    }
}
