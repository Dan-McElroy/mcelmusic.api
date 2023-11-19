package com.mcelroy.mcelmusic.api.domain.service;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.InvalidParametersException;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    private static final String TEST_ARTIST_ID = "Artist ID";

    @Mock
    private ArtistRepository artistRepository;

    @Captor
    private ArgumentCaptor<Integer> artistIndexCaptor;

    @InjectMocks
    private ArtistService artistService;
    @Test
    void givenValidParams_whenCreatingArtist_thenReturnArtist() {

        var artistCreationParams = ArtistCreationParamsDto.builder()
                .name("Test artist")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .build();

        var expectedArtist = Artist.builder()
                .name("Test artist")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .build();

        given(artistRepository.save(expectedArtist))
                .willReturn(Mono.just(expectedArtist));

        StepVerifier.create(artistService.createArtist(artistCreationParams))
                .expectNext(expectedArtist)
                .verifyComplete();
    }

    @Test
    void givenInvalidParams_whenCreatingArtist_thenThrowInvalidParametersException() {

        var artistCreationParams = ArtistCreationParamsDto.builder()
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .build();

        given(artistRepository.save(any(Artist.class)))
                .willReturn(Mono.error(InvalidParametersException.artist()));

        StepVerifier.create(artistService.createArtist(artistCreationParams))
                .expectError(InvalidParametersException.class)
                .verify();
    }

    @Test
    void givenExistingArtist_whenGettingArtist_thenReturnArtist() {
        var expectedArtist = Artist.builder()
                .id(TEST_ARTIST_ID)
                .name("Test artist")
                .version(1)
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .build();

        given(artistRepository.findById(TEST_ARTIST_ID))
                .willReturn(Mono.just(expectedArtist));

        StepVerifier.create(artistService.getArtist(TEST_ARTIST_ID))
                .expectNext(expectedArtist)
                .verifyComplete();
    }

    @Test
    void givenNoArtist_whenGettingArtist_thenReturnNotFoundException() {

        given(artistRepository.findById(TEST_ARTIST_ID))
                .willReturn(Mono.empty());

        StepVerifier.create(artistService.getArtist(TEST_ARTIST_ID))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void givenValidUpdateParams_whenUpdatingArtist_thenReturnUpdatedArtist() {

        var initialArtist = Artist.builder()
                .id(TEST_ARTIST_ID)
                .name("Test artist")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .version(2)
                .build();

        var updateParams = ArtistUpdateParamsDto.builder()
                .version(2)
                .name("New artist name")
                .build();

        var expectedArtist = Artist.builder()
                .id(TEST_ARTIST_ID)
                .name("New artist name")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .version(3)
                .build();

        given(artistRepository.findById(TEST_ARTIST_ID))
                .willReturn(Mono.just(initialArtist));

        given(artistRepository.save(expectedArtist))
                .willReturn(Mono.just(expectedArtist));

        StepVerifier.create(artistService.updateArtist(TEST_ARTIST_ID, updateParams))
                .expectNext(expectedArtist)
                .verifyComplete();
    }

    @Test
    void givenNoArtistFound_whenUpdatingArtist_thenThrowNotFoundException() {

        var updateParams = ArtistUpdateParamsDto.builder()
                .version(1)
                .name("New artist name")
                .build();

        given(artistRepository.findById(TEST_ARTIST_ID))
                .willReturn(Mono.empty());

        StepVerifier.create(artistService.updateArtist(TEST_ARTIST_ID, updateParams))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void givenIncorrectVersion_whenUpdatingArtist_thenReturnUpdatedArtist() {

        var initialArtist = Artist.builder()
                .id(TEST_ARTIST_ID)
                .name("Test artist")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .version(2)
                .build();

        var updateParams = ArtistUpdateParamsDto.builder()
                .version(1)
                .name("New artist name")
                .build();

        given(artistRepository.findById(TEST_ARTIST_ID))
                .willReturn(Mono.just(initialArtist));

        StepVerifier.create(artistService.updateArtist(TEST_ARTIST_ID, updateParams))
                .expectError(VersionConflictException.class)
                .verify();
    }

    @Test
    void givenValidId_whenDeletingTrack_thenReturnEmpty() {

        given(artistRepository.deleteById(TEST_ARTIST_ID))
                .willReturn(Mono.empty());

        StepVerifier.create(artistService.deleteArtist(TEST_ARTIST_ID))
                .verifyComplete();
    }

    @Test
    void givenNullId_whenDeletingTrack_thenReturnInvalidParametersException() {

        StepVerifier.create(artistService.deleteArtist(null))
                .expectError(InvalidParametersException.class)
                .verify();
    }

    @ParameterizedTest
    @MethodSource("getArtistOfTheDayData")
    void givenTime_whenGettingArtistOfTheDay_passCorrectIndex(long artistCount, Instant instant, int expectedArtistIndex) {
        given(artistRepository.count()).willReturn(Mono.just(artistCount));

        given(artistRepository.findNthArtist(expectedArtistIndex))
                .willReturn(Mono.just(Artist.builder().id(TEST_ARTIST_ID).build()));

        StepVerifier.create(artistService.getArtistOfTheDay(instant))
                .expectNextCount(1)
                .verifyComplete();

        then(artistRepository).should().findNthArtist(artistIndexCaptor.capture());

        assertEquals(expectedArtistIndex, artistIndexCaptor.getValue());
    }

    static Stream<Arguments> getArtistOfTheDayData() {
        return Stream.of(
                Arguments.of(1L, Instant.EPOCH.plus(30, ChronoUnit.DAYS), 0),
                Arguments.of(5L, Instant.EPOCH.plus(4978, ChronoUnit.DAYS), 3),
                Arguments.of(5498L, Instant.EPOCH.plus(937, ChronoUnit.DAYS), 937),
                Arguments.of(948L, Instant.parse("2023-11-19T23:26:43Z"), 720)
        );
    }

}
