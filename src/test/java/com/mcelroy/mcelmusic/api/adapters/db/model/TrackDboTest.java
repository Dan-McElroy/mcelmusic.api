package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.Genre;
import com.mcelroy.mcelmusic.api.domain.model.Track;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TrackDboTest {

    private static final String TEST_ID = "4020910a-a1d0-4a8c-b949-e1483960b1ea";
    private static final String TEST_ARTIST_ID = "4020910a-a1d0-4a8c-b949-e1483960b1ea";
    private static final String TEST_GENRE_ID = "4020910a-a1d0-4a8c-b949-e1483960b1ea";

    @Test
    void givenBasicData_thenTrackConvertedToDboSuccessfully() {
        var track = Track.builder()
                .id(TEST_ID)
                .title("Test track name")
                .lengthSeconds(450)
                .version(3)
                .build();
        var dbo = TrackDbo.fromTrack(track);

        assertEquals(UUID.fromString(TEST_ID), dbo.getId());
        assertEquals("Test track name", dbo.getTitle());
        assertEquals(450, dbo.getLengthSeconds());
        assertEquals(3, dbo.getVersion());
    }

    @Test
    void givenArtists_thenTrackArtistsNotConverted() {
        var track = Track.builder()
                .id(TEST_ID)
                .title("Test track name")
                .lengthSeconds(450)
                .version(3)
                .artists(Set.of(
                        Artist.builder().id(TEST_ARTIST_ID).build()
                ))
                .build();
        var dbo = TrackDbo.fromTrack(track);

        assertEquals(0, dbo.getArtists().size());
    }

    @Test
    void givenGenre_thenTrackGenresNotConverted() {
        var track = Track.builder()
                .id(TEST_ID)
                .title("Test track name")
                .lengthSeconds(450)
                .version(3)
                .genre(Genre.builder().id(TEST_GENRE_ID).build())
                .build();
        var dbo = TrackDbo.fromTrack(track);

        assertNull(dbo.getGenre());
    }

    @Test
    void givenValidData_thenDtoConvertedToTrackSuccessfully() {

        var artistDbo = ArtistDbo.builder()
                .id(UUID.fromString(TEST_ARTIST_ID))
                .name("Test Artist")
                .build();
        var genreDbo = GenreDbo.builder()
                .id(UUID.fromString(TEST_GENRE_ID))
                .name("Test Genre")
                .build();

        var dbo = TrackDbo.builder()
                .id(UUID.fromString(TEST_ID))
                .version(3)
                .title("Test track name")
                .creationTime(Timestamp.from(Instant.now()))
                .artists(Set.of(artistDbo))
                .genre(genreDbo)
                .lengthSeconds(450)
                .build();
        var track = TrackDbo.toTrack(dbo);

        assertEquals(TEST_ID, track.getId());
        assertEquals("Test track name", track.getTitle());
        assertEquals(450, track.getLengthSeconds());
        assertEquals(3, track.getVersion());
        assertEquals(Set.of(ArtistDbo.toArtist(artistDbo)), track.getArtists());
        assertEquals(GenreDbo.toGenre(genreDbo), track.getGenre());
    }
}
