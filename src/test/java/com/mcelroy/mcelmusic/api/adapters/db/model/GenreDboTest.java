package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Genre;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenreDboTest {

    private static final String TEST_ID = "4020910a-a1d0-4a8c-b949-e1483960b1ea";

    @Test
    void givenBasicData_thenGenreConvertedToDboSuccessfully() {
        var genre = Genre.builder()
                .id(TEST_ID)
                .name("Test genre name")
                .build();
        var dbo = GenreDbo.fromGenre(genre);

        assertEquals(UUID.fromString(TEST_ID), dbo.getId());
        assertEquals("Test genre name", dbo.getName());
    }

    @Test
    void givenValidData_thenDtoConvertedToGenreSuccessfully() {

        var dbo = GenreDbo.builder()
                .id(UUID.fromString(TEST_ID))
                .name("Test genre name")
                .creationTime(Instant.now())
                .build();
        var genre = GenreDbo.toGenre(dbo);

        assertEquals(TEST_ID, genre.getId());
        assertEquals("Test genre name", genre.getName());
    }
}
