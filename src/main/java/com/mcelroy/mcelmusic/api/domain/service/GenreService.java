package com.mcelroy.mcelmusic.api.domain.service;

import com.mcelroy.mcelmusic.api.domain.model.Genre;
import com.mcelroy.mcelmusic.api.domain.model.dto.GenreCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.GenreUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class GenreService {

    private GenreRepository genreRepository;

    public Mono<Genre> createGenre(GenreCreationParamsDto creationParams) {
        return genreRepository.save(Genre.fromDto(creationParams))
                .switchIfEmpty(Mono.error(new RuntimeException("Unknown issue occurred when creating a genre.")));
    }

    public Mono<Genre> getGenre(String genreId) {
        return genreRepository.findById(genreId)
                .switchIfEmpty(handleNotFound());
    }

    public Mono<Genre> updateGenre(String genreId, GenreUpdateParamsDto update) {
        return genreRepository.findById(genreId)
                .switchIfEmpty(handleNotFound())
                .filter(genre -> genre.getVersion() == update.version())
                .switchIfEmpty(Mono.error(VersionConflictException.genre()))
                .map(genre -> genre.toBuilder()
                        .name(update.name())
                        .version(genre.getVersion() + 1)
                        .build())
                .flatMap(genre -> genreRepository.save(genre)
                        .switchIfEmpty(
                                Mono.error(new RuntimeException("Unknown issue occurred when updating a genre."))));
    }

    public Mono<Void> deleteGenre(String genreId) {
        return genreRepository.findById(genreId)
                .switchIfEmpty(handleNotFound())
                .flatMap(genreRepository::delete);
    }

    private Mono<Genre> handleNotFound() {
        return Mono.error(NotFoundException.genre());
    }
}
