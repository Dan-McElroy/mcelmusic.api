package com.mcelroy.mcelmusic.api.domain.service;


import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.Genre;
import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.InvalidParametersException;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.repository.ArtistRepository;
import com.mcelroy.mcelmusic.api.domain.repository.GenreRepository;
import com.mcelroy.mcelmusic.api.domain.repository.TrackRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TrackServiceTest {

    private static final String TEST_ARTIST_ID_1 = "Artist ID 1";
    private static final String TEST_ARTIST_ID_2 = "Artist ID 2";
    private static final String TEST_GENRE_ID = "Test Genre ID";
    private static final String TEST_TRACK_ID = "Test Track ID";

    private static final Artist TEST_ARTIST_1 = Artist.builder()
            .id(TEST_ARTIST_ID_1).name("Four Tet")
            .build();

    private static final Artist TEST_ARTIST_2 = Artist.builder()
            .id(TEST_ARTIST_ID_2).name("Gil Scott-Heron")
            .build();

    private static final Genre TEST_GENRE = Genre.builder()
            .id(TEST_GENRE_ID).name("Soul")
            .build();

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private TrackService trackService;

    @Test
    void givenValidParams_whenCreatingTrack_thenReturnTrack() {

        var trackCreationParams = TrackCreationParamsDto.builder()
                .title("Test track")
                .artistIds(List.of(TEST_ARTIST_ID_1, TEST_ARTIST_ID_2))
                .lengthSeconds(60)
                .genreId(TEST_GENRE_ID)
                .build();

        var expectedTrack = Track.builder()
                .title("Test track")
                .artists(Set.of(TEST_ARTIST_1, TEST_ARTIST_2))
                .lengthSeconds(60)
                .genre(TEST_GENRE)
                .version(1)
                .build();

        setupArtistsAndGenre();

        given(trackRepository.save(expectedTrack))
                .willReturn(Mono.just(expectedTrack));

        StepVerifier.create(trackService.createTrack(trackCreationParams))
                .expectNext(expectedTrack)
                .verifyComplete();
    }

    @Test
    void givenInvalidParams_whenCreatingTrack_thenThrowInvalidParametersException() {

        var trackCreationParams = TrackCreationParamsDto.builder()
                .title("Test track")
                .artistIds(List.of(TEST_ARTIST_ID_1, TEST_ARTIST_ID_2))
                .lengthSeconds(60)
                .genreId(TEST_GENRE_ID)
                .build();

        setupArtistsAndGenre();

        given(trackRepository.save(any(Track.class)))
                .willReturn(Mono.error(InvalidParametersException.track()));

        StepVerifier.create(trackService.createTrack(trackCreationParams))
                .expectError(InvalidParametersException.class)
                .verify();
    }

    @Test
    void givenExistingTrack_whenGettingTrack_thenReturnTrack() {
        var expectedTrack = Track.builder()
                .id("ExpectedID")
                .title("Test track")
                .artists(Set.of(TEST_ARTIST_1, TEST_ARTIST_2))
                .lengthSeconds(60)
                .genre(TEST_GENRE)
                .version(1)
                .build();


        given(trackRepository.findById("ExpectedID"))
                .willReturn(Mono.just(expectedTrack));

        StepVerifier.create(trackService.getTrack("ExpectedID"))
                .expectNext(expectedTrack)
                .verifyComplete();
    }

    @Test
    void givenNoTrack_whenGettingTrack_thenReturnNotFoundException() {

        given(trackRepository.findById("NonExistingID"))
                .willReturn(Mono.empty());

        StepVerifier.create(trackService.getTrack("NonExistingID"))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void givenValidUpdateParams_whenUpdatingTrack_thenReturnUpdatedTrack() {

        var initialTrack = Track.builder()
                .id(TEST_TRACK_ID)
                .title("Test track")
                .artists(Set.of(TEST_ARTIST_1, TEST_ARTIST_2))
                .lengthSeconds(60)
                .genre(TEST_GENRE)
                .version(2)
                .build();

        var updateParams = TrackUpdateParamsDto.builder()
                .version(2)
                .title("New track title")
                .build();

        var expectedTrack = Track.builder()
                .id(TEST_TRACK_ID)
                .title("New track title")
                .artists(Set.of(TEST_ARTIST_1, TEST_ARTIST_2))
                .lengthSeconds(60)
                .genre(TEST_GENRE)
                .version(3)
                .build();

        given(trackRepository.findById(TEST_TRACK_ID))
                .willReturn(Mono.just(initialTrack));

        given(trackRepository.save(expectedTrack))
                .willReturn(Mono.just(expectedTrack));

        StepVerifier.create(trackService.updateTrack(TEST_TRACK_ID, updateParams))
                .expectNext(expectedTrack)
                .verifyComplete();
    }

    @Test
    void givenNoTrackFound_whenUpdatingTrack_thenThrowNotFoundException() {

        var updateParams = TrackUpdateParamsDto.builder()
                .version(1)
                .title("New track title")
                .build();

        given(trackRepository.findById(TEST_TRACK_ID))
                .willReturn(Mono.empty());

        StepVerifier.create(trackService.updateTrack(TEST_TRACK_ID, updateParams))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void givenIncorrectVersion_whenUpdatingTrack_thenReturnUpdatedTrack() {

        var initialTrack = Track.builder()
                .id(TEST_TRACK_ID)
                .title("Test track")
                .artists(Set.of(TEST_ARTIST_1, TEST_ARTIST_2))
                .lengthSeconds(60)
                .genre(TEST_GENRE)
                .version(2)
                .build();

        var updateParams = TrackUpdateParamsDto.builder()
                .version(1)
                .title("New track title")
                .build();

        given(trackRepository.findById("TestID"))
                .willReturn(Mono.just(initialTrack));

        StepVerifier.create(trackService.updateTrack("TestID", updateParams))
                .expectError(VersionConflictException.class)
                .verify();
    }

    @Test
    void givenExistingTrack_whenDeletingTrack_thenReturnEmpty() {

        var existingTrack = Track.builder()
                .id(TEST_TRACK_ID)
                .title("Test track")
                .artists(Set.of(TEST_ARTIST_1, TEST_ARTIST_2))
                .lengthSeconds(60)
                .genre(TEST_GENRE)
                .version(1)
                .build();

        given(trackRepository.findById(TEST_TRACK_ID))
                .willReturn(Mono.just(existingTrack));

        given(trackRepository.delete(existingTrack))
                .willReturn(Mono.empty());

        StepVerifier.create(trackService.deleteTrack(TEST_TRACK_ID))
                .verifyComplete();
    }

    @Test
    void givenNonExistingTrack_whenDeletingTrack_thenReturnNotFoundException() {

        given(trackRepository.findById(TEST_TRACK_ID))
                .willReturn(Mono.empty());

        StepVerifier.create(trackService.deleteTrack(TEST_TRACK_ID))
                .expectError(NotFoundException.class)
                .verify();

    }

    private void setupArtistsAndGenre() {
        given(artistRepository.findAllById(Set.of(TEST_ARTIST_ID_1, TEST_ARTIST_ID_2)))
                .willReturn(Mono.just(Set.of(TEST_ARTIST_1, TEST_ARTIST_2)));

        given(genreRepository.findById(TEST_GENRE_ID))
                .willReturn(Mono.just(TEST_GENRE));
    }
}
