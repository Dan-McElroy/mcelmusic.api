package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.InvalidParametersException;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.service.TrackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.CONFLICT;

@WebFluxTest(value = TrackController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@ActiveProfiles("test")
class TrackControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private TrackService trackService;

    @BeforeEach
    void setup() {
        // TODO: set up mocks for service
    }

    @Test
    void givenValidTrackCreationParams_whenCreatingTrack_thenReturnOk() {
         var trackCreationParams = TrackCreationParamsDto.builder()
                 .title("Test track")
                 .albumId("Test album ID")
                 .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                 .lengthSeconds(60)
                 .genreId("Test genre ID")
                 .build();

         var expectedTrack = Track.builder()
                 .title("Test track")
                 .albumId("Test album ID")
                 .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                 .lengthSeconds(60)
                 .genreId("Test genre ID")
                 .version(1)
                 .build();

         given(trackService.createTrack(trackCreationParams))
                 .willReturn(Mono.just(expectedTrack));

        client.put()
                 .uri("/track")
                 .accept(MediaType.APPLICATION_JSON)
                 .contentType(MediaType.APPLICATION_JSON)
                 .bodyValue(trackCreationParams)
                 .exchange()
                 .expectStatus()
                 .isOk()
                 .expectBody(Track.class)
                 .isEqualTo(expectedTrack);
    }

    @Test
    void givenMissingArtist_whenCreatingTrack_thenReturnRequest() {
        var trackCreationParams = TrackCreationParamsDto.builder()
                .title("Test track")
                .albumId("Test album ID")
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .build();

        given(trackService.createTrack(trackCreationParams))
                .willReturn(Mono.error(InvalidParametersException.track(List.of("artists"))));

        client.put()
                .uri("/track")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(trackCreationParams)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void givenExistingTrack_whenGettingTrack_thenReturnTrack() {
        var expectedTrack = Track.builder()
                .id("ExistingID")
                .version(2)
                .title("Test track")
                .albumId("Test album ID")
                .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .build();

        given(trackService.getTrack("ExistingID"))
                .willReturn(Mono.just(expectedTrack));

        client.get()
                .uri("/track/ExistingID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Track.class)
                .isEqualTo(expectedTrack);
    }

    @Test
    void givenNonExistingTrack_whenGettingTrack_thenReturnNotFound() {
        given(trackService.getTrack("NonExistingID"))
                .willReturn(Mono.error(NotFoundException.track()));

        client.get()
                .uri("/track/NonExistingID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void givenValidUpdateParameters_whenUpdatingTrack_thenReturnUpdatedTrack() {
        var updateParameters = TrackUpdateParamsDto.builder()
                .genreId("New genre ID")
                .title("New title")
                .version(1)
                .build();

        var expectedTrack = Track.builder()
                .id("UpdateID")
                .version(2)
                .title("New title")
                .albumId("Test album ID")
                .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("New genre ID")
                .build();

        given(trackService.updateTrack("UpdateID", updateParameters))
                .willReturn(Mono.just(expectedTrack));

        client.patch()
                .uri("/track/UpdateID")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateParameters)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Track.class)
                .isEqualTo(expectedTrack);
    }

    @Test
    void givenNonExistingTrackId_whenUpdatingTrack_thenNotFound() {
        var updateParameters = TrackUpdateParamsDto.builder()
                .genreId("New genre ID")
                .title("New title")
                .version(1)
                .build();

        given(trackService.updateTrack("NonExistingID", updateParameters))
                .willReturn(Mono.error(NotFoundException.track()));

        client.patch()
                .uri("/track/NonExistingID")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateParameters)
                .exchange()
                .expectStatus()
                .isOk()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void givenInvalidVersion_whenUpdatingTrack_thenReturnConflict() {
        var updateParameters = TrackUpdateParamsDto.builder()
                .genreId("New genre ID")
                .title("New title")
                .version(2)
                .build();

        given(trackService.updateTrack("UpdateID", updateParameters))
                .willReturn(Mono.error(VersionConflictException.track()));

        client.patch()
                .uri("/track/UpdateID")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateParameters)
                .exchange()
                .expectStatus()
                .isEqualTo(CONFLICT);
    }

    @Test
    void givenExistingTrack_whenDeletingTrack_thenReturnNoContent() {

        given(trackService.deleteTrack("ToBeDeletedID"))
                .willReturn(Mono.empty());

        client.delete()
                .uri("/track/ToBeDeletedID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void givenANonExistingTrackId_whenDeletingTrack_thenReturnNotFound() {

        given(trackService.deleteTrack("ToBeDeletedID"))
                .willReturn(Mono.error(NotFoundException.track()));
        client.delete()
                .uri("/track/ToBeDeletedID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
