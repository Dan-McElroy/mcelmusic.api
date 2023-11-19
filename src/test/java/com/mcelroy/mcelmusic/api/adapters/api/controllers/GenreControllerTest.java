package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Genre;
import com.mcelroy.mcelmusic.api.domain.model.dto.GenreCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.GenreUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.InvalidParametersException;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.service.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.CONFLICT;

@WebFluxTest(value = GenreController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@ActiveProfiles("test")
class GenreControllerTest {

    private static final String TEST_GENRE_ID = "Genre ID";

    @Autowired
    private WebTestClient client;

    @MockBean
    private GenreService genreService;

    @Test
    void givenValidGenreCreationParams_whenCreatingGenre_thenReturnOk() {
        var genreCreationParams = GenreCreationParamsDto.builder()
                .name("Test genre")
                .build();

        var expectedGenre = Genre.builder()
                .id(TEST_GENRE_ID)
                .name("Test genre")
                .build();

        given(genreService.createGenre(genreCreationParams))
                .willReturn(Mono.just(expectedGenre));

        client.put()
                .uri("/genre")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(genreCreationParams)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Genre.class)
                .isEqualTo(expectedGenre);
    }

    @Test
    void givenMissingName_whenCreatingGenre_thenReturnRequest() {
        var genreCreationParams = GenreCreationParamsDto.builder()
                .build();

        given(genreService.createGenre(genreCreationParams))
                .willReturn(Mono.error(InvalidParametersException.genre("name")));

        client.put()
                .uri("/genre")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(genreCreationParams)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void givenExistingGenre_whenGettingGenre_thenReturnGenre() {
        var expectedGenre = Genre.builder()
                .id(TEST_GENRE_ID)
                .version(2)
                .name("Reggaeton")
                .build();

        given(genreService.getGenre(TEST_GENRE_ID))
                .willReturn(Mono.just(expectedGenre));

        client.get()
                .uri("/genre/" + TEST_GENRE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Genre.class)
                .isEqualTo(expectedGenre);
    }

    @Test
    void givenNonExistingGenre_whenGettingGenre_thenReturnNotFound() {
        given(genreService.getGenre(TEST_GENRE_ID))
                .willReturn(Mono.error(NotFoundException.genre()));

        client.get()
                .uri("/genre/" + TEST_GENRE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void givenValidUpdateParameters_whenUpdatingGenre_thenReturnUpdatedGenre() {
        var updateParameters = GenreUpdateParamsDto.builder()
                .name("Folk")
                .version(1)
                .build();

        var expectedGenre = Genre.builder()
                .id(TEST_GENRE_ID)
                .version(1)
                .name("Country")
                .build();

        given(genreService.updateGenre(TEST_GENRE_ID, updateParameters))
                .willReturn(Mono.just(expectedGenre));

        client.patch()
                .uri("/genre/" + TEST_GENRE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateParameters)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Genre.class)
                .isEqualTo(expectedGenre);
    }

    @Test
    void givenNonExistingGenreId_whenUpdatingGenre_thenNotFound() {
        var updateParameters = GenreUpdateParamsDto.builder()
                .name("Acid Jazz")
                .version(1)
                .build();

        given(genreService.updateGenre(TEST_GENRE_ID, updateParameters))
                .willReturn(Mono.error(NotFoundException.genre()));

        client.patch()
                .uri("/genre/" + TEST_GENRE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateParameters)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void givenInvalidVersion_whenUpdatingGenre_thenReturnConflict() {
        var updateParameters = GenreUpdateParamsDto.builder()
                .name("Acid Jazz")
                .version(2)
                .build();

        given(genreService.updateGenre(TEST_GENRE_ID, updateParameters))
                .willReturn(Mono.error(VersionConflictException.genre()));

        client.patch()
                .uri("/genre/" + TEST_GENRE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateParameters)
                .exchange()
                .expectStatus()
                .isEqualTo(CONFLICT);
    }

    @Test
    void givenExistingGenre_whenDeletingGenre_thenReturnNoContent() {

        given(genreService.deleteGenre(TEST_GENRE_ID))
                .willReturn(Mono.empty());

        client.delete()
                .uri("/genre/" + TEST_GENRE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}
