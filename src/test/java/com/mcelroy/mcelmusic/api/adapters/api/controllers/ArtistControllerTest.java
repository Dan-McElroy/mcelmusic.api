package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistUpdateParamsDto;
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

@WebFluxTest(value = ArtistController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@ActiveProfiles("test")
class ArtistControllerTest {

    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setup() {
        // TODO: set up mocks for service
    }

    @Test
    void givenValidArtistCreationParams_whenCreatingArtist_thenReturnOk() {
        var artistCreationParams = ArtistCreationParamsDto.builder()
                .name("Test artist")
                .aliases(List.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        var expectedArtist = Artist.builder()
                .version(1)
                .name("Test artist")
                .aliases(List.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        client.put()
                .uri("/artist")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(artistCreationParams)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Artist.class)
                .isEqualTo(expectedArtist);
    }

    @Test
    void givenMissingName_whenCreatingArtist_thenReturnRequest() {
        var artistCreationParams = ArtistCreationParamsDto.builder()
                .aliases(List.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        client.put()
                .uri("/artist")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(artistCreationParams)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void givenExistingArtist_whenGettingArtist_thenReturnArtist() {
        var expectedArtist = Artist.builder()
                .id("ExistingID")
                .version(2)
                .name("Test artist")
                .aliases(List.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        client.get()
                .uri("/artist/ExistingID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Artist.class)
                .isEqualTo(expectedArtist);
    }

    @Test
    void givenNonExistingArtist_whenGettingArtist_thenReturnNotFound() {
        client.get()
                .uri("/artist/NonExistingID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void givenValidUpdateParameters_whenUpdatingArtist_thenReturnUpdatedArtist() {
        var existingArtist = Artist.builder()
                .id("UpdateID")
                .version(2)
                .name("Test artist")
                .aliases(List.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();
        var updateParameters = ArtistUpdateParamsDto.builder()
                .version(2)
                .profilePictureUrl("http://bing.com")
                .build();
        var expectedArtist = Artist.builder()
                .id("UpdateID")
                .version(3)
                .name("Test artist")
                .aliases(List.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://bing.com")
                .build();
        client.patch()
                .uri("/artist/UpdateID")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateParameters)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Artist.class)
                .isEqualTo(expectedArtist);
    }

    @Test
    void givenNonExistingArtistId_whenUpdatingArtist_thenNotFound() {
        var existingArtist = Artist.builder()
                .id("UpdateID")
                .version(2)
                .name("Test artist")
                .aliases(List.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();
        var updateParameters = ArtistUpdateParamsDto.builder()
                .version(2)
                .profilePictureUrl("http://bing.com")
                .build();
        client.patch()
                .uri("/artist/NonExistingID")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateParameters)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void givenInvalidVersion_whenUpdatingArtist_thenReturnConflict() {
        var existingArtist = Artist.builder()
                .id("UpdateID")
                .version(2)
                .name("Test artist")
                .aliases(List.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();
        var updateParameters = ArtistUpdateParamsDto.builder()
                .version(4)
                .profilePictureUrl("http://bing.com")
                .build();
        client.patch()
                .uri("/artist/UpdateID")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateParameters)
                .exchange()
                .expectStatus()
                .isEqualTo(CONFLICT);
    }

    @Test
    void givenValidDate_whenGettingArtistOfTheDay_thenReturnArtist() {
        var expectedArtist = Artist.builder()
                .id("DailyID")
                .version(2)
                .name("Test artist")
                .aliases(List.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        client.get()
                .uri("/artist/today")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Artist.class)
                .isEqualTo(expectedArtist);
    }

    @Test
    void givenExistingArtist_whenDeletingArtist_thenReturnNoContent() {
        var expectedArtist = Artist.builder()
                .id("ToBeDeletedID")
                .version(2)
                .name("Test artist")
                .aliases(List.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        client.delete()
                .uri("/artist/ToBeDeletedID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void givenANonExistingArtistId_whenDeletingArtist_thenReturnNotFound() {
        client.delete()
                .uri("/artist/NonExistingID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}

