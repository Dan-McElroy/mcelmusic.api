package com.mcelroy.mcelmusic.api.domain.service;

import com.mcelroy.mcelmusic.api.domain.model.Genre;
import com.mcelroy.mcelmusic.api.domain.model.dto.GenreCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.GenreUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    private static final String TEST_GENRE_ID = "Test Genre ID";

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    @Test
    void givenValidParams_whenCreatingGenre_thenReturnGenre() {

        var genreCreationParams = GenreCreationParamsDto.builder()
                .name("Soul")
                .build();

        var expectedGenre = Genre.builder()
                .name("Soul")
                .build();

        given(genreRepository.save(expectedGenre))
                .willReturn(Mono.just(expectedGenre));

        StepVerifier.create(genreService.createGenre(genreCreationParams))
                .expectNext(expectedGenre)
                .verifyComplete();
    }

    @Test
    void givenExistingGenre_whenGettingGenre_thenReturnGenre() {
        var expectedGenre = Genre.builder()
                .id(TEST_GENRE_ID)
                .name("Jazz")
                .build();


        given(genreRepository.findById(TEST_GENRE_ID))
                .willReturn(Mono.just(expectedGenre));

        StepVerifier.create(genreService.getGenre(TEST_GENRE_ID))
                .expectNext(expectedGenre)
                .verifyComplete();
    }

    @Test
    void givenNoGenre_whenGettingGenre_thenReturnNotFoundException() {

        given(genreRepository.findById(TEST_GENRE_ID))
                .willReturn(Mono.empty());

        StepVerifier.create(genreService.getGenre(TEST_GENRE_ID))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void givenValidUpdateParams_whenUpdatingGenre_thenReturnUpdatedGenre() {

        var initialGenre = Genre.builder()
                .id(TEST_GENRE_ID)
                .name("Blues")
                .version(2)
                .build();

        var updateParams = GenreUpdateParamsDto.builder()
                .version(2)
                .name("Blues 2")
                .build();

        var expectedGenre = Genre.builder()
                .id(TEST_GENRE_ID)
                .name("Blues 2")
                .version(3)
                .build();

        given(genreRepository.findById(TEST_GENRE_ID))
                .willReturn(Mono.just(initialGenre));

        given(genreRepository.save(expectedGenre))
                .willReturn(Mono.just(expectedGenre));

        StepVerifier.create(genreService.updateGenre(TEST_GENRE_ID, updateParams))
                .expectNext(expectedGenre)
                .verifyComplete();
    }

    @Test
    void givenNoGenreFound_whenUpdatingGenre_thenThrowNotFoundException() {

        var updateParams = GenreUpdateParamsDto.builder()
                .version(1)
                .name("Rock")
                .build();

        given(genreRepository.findById(TEST_GENRE_ID))
                .willReturn(Mono.empty());

        StepVerifier.create(genreService.updateGenre(TEST_GENRE_ID, updateParams))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void givenIncorrectVersion_whenUpdatingGenre_thenReturnUpdatedGenre() {

        var initialGenre = Genre.builder()
                .id(TEST_GENRE_ID)
                .name("House")
                .version(2)
                .build();

        var updateParams = GenreUpdateParamsDto.builder()
                .version(1)
                .name("Detroit House")
                .build();

        given(genreRepository.findById(TEST_GENRE_ID))
                .willReturn(Mono.just(initialGenre));

        StepVerifier.create(genreService.updateGenre(TEST_GENRE_ID, updateParams))
                .expectError(VersionConflictException.class)
                .verify();
    }

    @Test
    void givenExistingGenre_whenDeletingGenre_thenReturnEmpty() {

        var existingGenre = Genre.builder()
                .id(TEST_GENRE_ID)
                .name("Pop")
                .version(1)
                .build();

        given(genreRepository.findById(TEST_GENRE_ID))
                .willReturn(Mono.just(existingGenre));

        given(genreRepository.delete(existingGenre))
                .willReturn(Mono.empty());

        StepVerifier.create(genreService.deleteGenre(TEST_GENRE_ID))
                .verifyComplete();
    }

    @Test
    void givenNonExistingGenre_whenDeletingGenre_thenReturnNotFoundException() {

        given(genreRepository.findById(TEST_GENRE_ID))
                .willReturn(Mono.empty());

        StepVerifier.create(genreService.deleteGenre(TEST_GENRE_ID))
                .expectError(NotFoundException.class)
                .verify();

    }
}
