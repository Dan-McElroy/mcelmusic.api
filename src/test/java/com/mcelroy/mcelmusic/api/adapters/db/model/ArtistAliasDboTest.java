package com.mcelroy.mcelmusic.api.adapters.db.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArtistAliasDboTest {

    @Test
    void givenValidAlias_thenDboCreatedSuccessfully() {
        var expectedDbo = ArtistAliasDbo.builder()
                .alias("Test alias")
                .build();
        assertEquals(expectedDbo, ArtistAliasDbo.fromAlias("Test alias"));
    }

    @Test
    void givenNullAlias_thenDboCreatedSuccessfully() {
        var expectedDbo = ArtistAliasDbo.builder().build();
        assertEquals(expectedDbo, ArtistAliasDbo.fromAlias(null));
    }
}
