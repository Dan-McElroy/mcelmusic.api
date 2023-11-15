package com.mcelroy.mcelmusic.api.domain.service;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.repository.ArtistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@AllArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    public Mono<Artist> createArtist(ArtistCreationParamsDto artistCreationParams) {
        // TODO - handle missing parameters
        var newArtist = Artist.fromDto(artistCreationParams);
        return artistRepository.save(newArtist);
    }

    public Mono<Artist> getArtist(String artistId) {
        return artistRepository.findBy(artistId)
                .switchIfEmpty(handleNotFound());
    }

    public Mono<Artist> getArtistOfTheDay(Instant currentTime) {
        // current thinking: order by date added, return daysSinceUnixEpoch % artistRepository.count
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Artist> updateArtist(String artistId, ArtistUpdateParamsDto update) {
        return artistRepository.findBy(artistId)
                .switchIfEmpty(handleNotFound())
                .filter(artist -> artist.getVersion() == update.version())
                .switchIfEmpty(Mono.error(VersionConflictException.artist()))
                .map(artist -> Artist.builder()
                        .id(artist.getId())
                        .version(artist.getVersion() + 1)
                        .name(update.name() != null ? update.name() : artist.getName())
                        .aliases(update.aliases() != null
                                ? update.aliases()
                                : artist.getAliases())
                        .profilePictureUrl(update.profilePictureUrl() != null
                                ? update.profilePictureUrl()
                                : artist.getProfilePictureUrl())
                        .build())
                .flatMap(artist -> artistRepository.save(artist).then(Mono.just(artist)));
    }

    public Mono<Void> deleteArtist(String artistId) {
        return artistRepository.findBy(artistId)
                .switchIfEmpty(handleNotFound())
                .flatMap(artistRepository::delete);
    }

    private Mono<Artist> handleNotFound() {
        return Mono.error(NotFoundException.artist());
    }
}