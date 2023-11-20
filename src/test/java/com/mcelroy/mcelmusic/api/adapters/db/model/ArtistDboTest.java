package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArtistDboTest {

    private static final String TEST_ID = "4020910a-a1d0-4a8c-b949-e1483960b1ea";

    @Test
    void givenBasicData_thenArtistConvertedToDboSuccessfully() {
        var artist = Artist.builder()
                .id(TEST_ID)
                .name("Test artist name")
                .profilePictureUrl("http://test.com")
                .build();
        var dbo = ArtistDbo.fromArtist(artist);

        assertEquals(UUID.fromString(TEST_ID), dbo.getId());
        assertEquals("Test artist name", dbo.getName());
        assertEquals("http://test.com", dbo.getProfilePictureUrl());
    }

    @Test
    void givenAliases_thenArtistAliasesNotConverted() {
        var artist = Artist.builder()
                .id(TEST_ID)
                .name("Test artist name")
                .profilePictureUrl("http://test.com")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .build();
        var dbo = ArtistDbo.fromArtist(artist);

        assertEquals(0, dbo.getAliases().size());
    }

    @Test
    void givenValidData_thenDtoConvertedToArtistSuccessfully() {

        var aliases = Set.of(
                ArtistAliasDbo.builder().alias("Alias 1").build(),
                ArtistAliasDbo.builder().alias("Alias 2").build());

        var dbo = ArtistDbo.builder()
                .id(UUID.fromString(TEST_ID))
                .name("Test artist name")
                .creationTime(Timestamp.from(Instant.now()))
                .aliases(aliases)
                .profilePictureUrl("http://test.com")
                .build();
        var artist = ArtistDbo.toArtist(dbo);

        assertEquals(TEST_ID, artist.getId());
        assertEquals("Test artist name", artist.getName());
        assertEquals("http://test.com", artist.getProfilePictureUrl());
        assertEquals(Set.of("Alias 1", "Alias 2"), artist.getAliases());
    }
}
