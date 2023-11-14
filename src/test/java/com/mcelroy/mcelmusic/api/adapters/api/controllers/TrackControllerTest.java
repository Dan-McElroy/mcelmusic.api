package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackUpdateParamsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;

@WebFluxTest(value = TrackController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@ActiveProfiles("test")
class TrackControllerTest {

    @Autowired
    private WebTestClient client;

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
                 .artists(List.of("Artist ID 1", "Artist ID 2"))
                 .lengthSeconds(60)
                 .genreId("Test genre ID")
                 .build();

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
                .title("Test track")
                .albumId("Test album ID")
                .artists(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .build();

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
        client.get()
                .uri("/track/NonExistingID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void givenValidUpdateParameters_whenUpdatingTrack_thenReturnUpdatedTrack() {
        var existingTrack = Track.builder()
                .id("UpdateID")
                .version(1)
                .title("Test track")
                .albumId("Test album ID")
                .artists(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .build();
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
                .artists(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("New genre ID")
                .build();
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
        var existingTrack = Track.builder()
                .id("UpdateID")
                .version(1)
                .title("Test track")
                .albumId("Test album ID")
                .artists(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .build();
        var updateParameters = TrackUpdateParamsDto.builder()
                .genreId("New genre ID")
                .title("New title")
                .version(1)
                .build();
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
        var existingTrack = Track.builder()
                .id("UpdateID")
                .version(3)
                .title("Test track")
                .albumId("Test album ID")
                .artists(List.of("Artist ID 1", "Artist ID 2"))
                .lengthSeconds(60)
                .genreId("Test genre ID")
                .build();
        var updateParameters = TrackUpdateParamsDto.builder()
                .genreId("New genre ID")
                .title("New title")
                .version(2)
                .build();
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
        // TODO: add existing track
        client.delete()
                .uri("/track/ToBeDeletedID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void givenANonExistingTrackId_whenDeletingTrack_thenReturnNotFound() {
        client.delete()
                .uri("/track/ToBeDeletedID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
