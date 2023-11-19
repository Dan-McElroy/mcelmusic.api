package com.mcelroy.mcelmusic.api.domain.service;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.InvalidParametersException;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.repository.ArtistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    public Mono<Artist> createArtist(ArtistCreationParamsDto creationParams) {
        return artistRepository.save(Artist.fromDto(creationParams));
    }

    public Mono<Artist> getArtist(String artistId) {
        return artistRepository.findById(artistId)
                .switchIfEmpty(handleNotFound());
    }

    public Mono<Artist> getArtistOfTheDay(Instant currentTime) {

        var daysSinceEpoch = ChronoUnit.DAYS.between(Instant.EPOCH, currentTime);

        return artistRepository.count().flatMap(count -> {
            var index = Math.toIntExact(daysSinceEpoch % count);
            return artistRepository.findNthArtist(index);
        });
    }

    public Mono<Artist> updateArtist(String artistId, ArtistUpdateParamsDto update) {
        return artistRepository.findById(artistId)
                .switchIfEmpty(handleNotFound())
                .filter(artist -> artist.getVersion() == update.version())
                .switchIfEmpty(Mono.error(VersionConflictException.artist()))
                .map(artist ->
                        artist.toBuilder()
                        .version(artist.getVersion() + 1)
                        .name(update.name() != null ? update.name() : artist.getName())
                        .aliases(update.aliases() != null
                                ? update.aliases()
                                : artist.getAliases())
                        .profilePictureUrl(update.profilePictureUrl() != null
                                ? update.profilePictureUrl()
                                : artist.getProfilePictureUrl())
                        .build())
                .flatMap(artistRepository::save);
    }

    public Mono<Void> deleteArtist(String artistId) {
        return artistId != null
            ? artistRepository.deleteById(artistId)
            : Mono.error(InvalidParametersException.artist("artistId"));
    }

    private Mono<Artist> handleNotFound() {
        return Mono.error(NotFoundException.artist());
    }
}