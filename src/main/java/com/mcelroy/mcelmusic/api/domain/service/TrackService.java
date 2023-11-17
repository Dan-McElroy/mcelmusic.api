package com.mcelroy.mcelmusic.api.domain.service;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.repository.TrackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;

    public Mono<Track> createTrack(TrackCreationParamsDto creationParamsDto) {
        // TODO - handle missing parameters
        var newTrack = Track.fromDto(creationParamsDto);
        return trackRepository.save(newTrack);
    }

    public Mono<Track> getTrack(String trackId) {
        return trackRepository.findById(trackId)
                .switchIfEmpty(handleNotFound());
    }

    public Mono<Track> updateTrack(String trackId, TrackUpdateParamsDto update) {
        return trackRepository.findById(trackId)

                .switchIfEmpty(handleNotFound())
                .filter(track -> track.getVersion() == update.version())
                .switchIfEmpty(Mono.error(VersionConflictException.track()))
                .map(track -> Track.builder()
                        .id(track.getId())
                        .version(track.getVersion() + 1)
                        .title(update.title() != null ? update.title() : track.getTitle())
                        .artistIds(update.artistIds() != null ? update.artistIds() : track.getArtistIds())
                        .genreId(update.genreId() != null ? update.genreId() : track.getGenreId())
                        .albumId(update.albumId() != null ? update.albumId() : track.getAlbumId())
                        .lengthSeconds(update.lengthSeconds() != null
                                ? update.lengthSeconds()
                                : track.getLengthSeconds())
                        .build())
                .flatMap(track -> trackRepository.save(track).then(Mono.just(track)));
    }

    public Mono<Void> deleteTrack(String trackId) {
        return trackRepository.findById(trackId)
                .switchIfEmpty(handleNotFound())
                .flatMap(trackRepository::delete);
    }

    private Mono<Track> handleNotFound() {
        return Mono.error(NotFoundException.track());
    }
}
