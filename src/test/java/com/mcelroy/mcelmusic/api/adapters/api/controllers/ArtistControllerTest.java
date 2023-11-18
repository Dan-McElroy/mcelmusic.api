package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.InvalidParametersException;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.service.ArtistService;
import org.assertj.core.condition.Not;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.CONFLICT;

@WebFluxTest(value = ArtistController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ActiveProfiles("test")
class ArtistControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ArtistService artistService;

    @Test
    void givenValidArtistCreationParams_whenCreatingArtist_thenReturnOk() {
        var artistCreationParams = ArtistCreationParamsDto.builder()
                .name("Test artist")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        var expectedArtist = Artist.builder()
                .id("CreatedID")
                .version(1)
                .name("Test artist")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        given(artistService.createArtist(artistCreationParams))
                .willReturn(Mono.just(expectedArtist));

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
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        given(artistService.createArtist(artistCreationParams))
                .willReturn(Mono.error(InvalidParametersException.artist("name")));

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
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        given(artistService.getArtist("ExistingID"))
                .willReturn(Mono.just(expectedArtist));

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
        given(artistService.getArtist("NonExistingID"))
                .willReturn(Mono.error(NotFoundException.artist()));

        client.get()
                .uri("/artist/NonExistingID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void givenValidUpdateParameters_whenUpdatingArtist_thenReturnUpdatedArtist() {
        var updateParameters = ArtistUpdateParamsDto.builder()
                .version(2)
                .profilePictureUrl("http://bing.com")
                .build();

        var expectedArtist = Artist.builder()
                .id("UpdateID")
                .version(3)
                .name("Test artist")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://bing.com")
                .build();

        given(artistService.updateArtist("UpdateID", updateParameters))
                .willReturn(Mono.just(expectedArtist));

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
        var updateParameters = ArtistUpdateParamsDto.builder()
                .version(2)
                .profilePictureUrl("http://bing.com")
                .build();

        given(artistService.updateArtist("NonExistingID", updateParameters))
                .willReturn(Mono.error(NotFoundException.artist()));

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
        var updateParameters = ArtistUpdateParamsDto.builder()
                .version(4)
                .profilePictureUrl("http://bing.com")
                .build();

        given(artistService.updateArtist("UpdateID", updateParameters))
                .willReturn(Mono.error(VersionConflictException.artist()));

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
    void whenGettingArtistOfTheDay_thenReturnArtist() {
        var expectedArtist = Artist.builder()
                .id("DailyID")
                .version(2)
                .name("Test artist")
                .aliases(Set.of("Alias 1", "Alias 2"))
                .profilePictureUrl("http://google.com")
                .build();

        given(artistService.getArtistOfTheDay(any(Instant.class)))
                .willReturn(Mono.just(expectedArtist));

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
        given(artistService.deleteArtist("ToBeDeletedID"))
                .willReturn(Mono.empty());

        client.delete()
                .uri("/artist/ToBeDeletedID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void givenANonExistingArtistId_whenDeletingArtist_thenReturnNotFound() {
        given(artistService.deleteArtist("NonExistingID"))
                .willReturn(Mono.error(NotFoundException.artist()));

        client.delete()
                .uri("/artist/NonExistingID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}

