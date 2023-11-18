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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

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
                .version(1)
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
                .id("ExpectedID")
                .name("Test artist")
                .version(1)
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .build();

        given(artistRepository.findById("ExpectedID"))
                .willReturn(Mono.just(expectedArtist));

        StepVerifier.create(artistService.getArtist("ExpectedID"))
                .expectNext(expectedArtist)
                .verifyComplete();
    }

    @Test
    void givenNoArtist_whenGettingArtist_thenReturnNotFoundException() {

        given(artistRepository.findById("NonExistingID"))
                .willReturn(Mono.empty());

        StepVerifier.create(artistService.getArtist("NonExistingID"))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void givenValidUpdateParams_whenUpdatingArtist_thenReturnUpdatedArtist() {

        var initialArtist = Artist.builder()
                .id("TestID")
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
                .id("TestID")
                .name("New artist name")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .version(3)
                .build();

        given(artistRepository.findById("TestID"))
                .willReturn(Mono.just(initialArtist));

        given(artistRepository.save(expectedArtist))
                .willReturn(Mono.just(expectedArtist));

        StepVerifier.create(artistService.updateArtist("TestID", updateParams))
                .expectNext(expectedArtist)
                .verifyComplete();
    }

    @Test
    void givenNoArtistFound_whenUpdatingArtist_thenThrowNotFoundException() {

        var updateParams = ArtistUpdateParamsDto.builder()
                .version(1)
                .name("New artist name")
                .build();

        given(artistRepository.findById("TestID"))
                .willReturn(Mono.empty());

        StepVerifier.create(artistService.updateArtist("TestID", updateParams))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void givenIncorrectVersion_whenUpdatingArtist_thenReturnUpdatedArtist() {

        var initialArtist = Artist.builder()
                .id("TestID")
                .name("Test artist")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .version(2)
                .build();

        var updateParams = ArtistUpdateParamsDto.builder()
                .version(1)
                .name("New artist name")
                .build();

        given(artistRepository.findById("TestID"))
                .willReturn(Mono.just(initialArtist));

        StepVerifier.create(artistService.updateArtist("TestID", updateParams))
                .expectError(VersionConflictException.class)
                .verify();
    }

    @Test
    void givenExistingArtist_whenDeletingArtist_thenReturnEmpty() {

        var existingArtist = Artist.builder()
                .id("ExistingID")
                .name("Test artist")
                .version(1)
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://test.com")
                .build();

        given(artistRepository.findById("ExistingID"))
                .willReturn(Mono.just(existingArtist));

        given(artistRepository.delete(existingArtist))
                .willReturn(Mono.empty());

        StepVerifier.create(artistService.deleteArtist("ExistingID"))
                .verifyComplete();
    }

    @Test
    void givenNonExistingArtist_whenDeletingArtist_thenReturnNotFoundException() {

        given(artistRepository.findById("NonExistingID"))
                .willReturn(Mono.empty());

        StepVerifier.create(artistService.deleteArtist("NonExistingID"))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void givenSeveralArtists_whenGettingArtistOfTheDay_rotateThroughArtists() {
        // create 4 artists
        // set up 5 Instants for 5 consecutive days
        // call the service for each instance, assert that first(?) is called twice
        
    }

}
