package com.mcelroy.mcelmusic.api.domain.service;


import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.InvalidParametersException;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.repository.TrackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TrackServiceTest {

    @Mock
    private TrackRepository trackRepository;

    @InjectMocks
    private TrackService trackService;

    @Test
    void givenValidParams_whenCreatingTrack_thenReturnTrack() {

        var trackCreationParams = TrackCreationParamsDto.builder()
                .title("Test track")
                .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .build();

        var expectedTrack = Track.builder()
                .title("Test track")
                .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .version(1)
                .build();

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
                .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .build();

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
                .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
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
                .id("TestID")
                .title("Test track")
                .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .version(2)
                .build();

        var updateParams = TrackUpdateParamsDto.builder()
                .version(2)
                .title("New track title")
                .build();

        var expectedTrack = Track.builder()
                .id("TestID")
                .title("New track title")
                .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .version(3)
                .build();

        given(trackRepository.findById("TestID"))
                .willReturn(Mono.just(initialTrack));

        given(trackRepository.save(expectedTrack))
                .willReturn(Mono.just(expectedTrack));

        StepVerifier.create(trackService.updateTrack("TestID", updateParams))
                .expectNext(expectedTrack)
                .verifyComplete();
    }

    @Test
    void givenNoTrackFound_whenUpdatingTrack_thenThrowNotFoundException() {

        var updateParams = TrackUpdateParamsDto.builder()
                .version(1)
                .title("New track title")
                .build();

        given(trackRepository.findById("TestID"))
                .willReturn(Mono.empty());

        StepVerifier.create(trackService.updateTrack("TestID", updateParams))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void givenIncorrectVersion_whenUpdatingTrack_thenReturnUpdatedTrack() {

        var initialTrack = Track.builder()
                .id("TestID")
                .title("Test track")
                .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
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
                .id("ExistingID")
                .title("Test track")
                .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .version(1)
                .build();

        given(trackRepository.findById("ExistingID"))
                .willReturn(Mono.just(existingTrack));

        given(trackRepository.delete(existingTrack))
                .willReturn(Mono.empty());

        StepVerifier.create(trackService.deleteTrack("ExistingID"))
                .verifyComplete();
    }

    @Test
    void givenNonExistingTrack_whenDeletingTrack_thenReturnNotFoundException() {

        given(trackRepository.findById("NonExistingID"))
                .willReturn(Mono.empty());

        StepVerifier.create(trackService.deleteTrack("NonExistingID"))
                .expectError(NotFoundException.class)
                .verify();

    }

}
