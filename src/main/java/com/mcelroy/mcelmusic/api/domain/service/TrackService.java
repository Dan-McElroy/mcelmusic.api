package com.mcelroy.mcelmusic.api.domain.service;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.Genre;
import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackUpdateParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.error.InvalidParametersException;
import com.mcelroy.mcelmusic.api.domain.model.error.NotFoundException;
import com.mcelroy.mcelmusic.api.domain.model.error.VersionConflictException;
import com.mcelroy.mcelmusic.api.domain.repository.ArtistRepository;
import com.mcelroy.mcelmusic.api.domain.repository.GenreRepository;
import com.mcelroy.mcelmusic.api.domain.repository.TrackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;

    public Mono<Track> createTrack(TrackCreationParamsDto creationParams) {
        var track = Track.fromDto(creationParams);

        return getArtistsForNewTrack(creationParams)
                .map(artists -> {
                    track.setArtists(artists);
                    return track;
                })
                .then(genreRepository.findById(creationParams.genreId()))
                .map(genre -> {
                    track.setGenre(genre);
                    return track;
                })
                .flatMap(trackRepository::save);
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
                .flatMap(track ->
                        getUpdatedGenre(update, track).map(genre ->
                            track.toBuilder()
                            .id(track.getId())
                            .version(track.getVersion() + 1)
                            .title(update.title() != null ? update.title() : track.getTitle())
                            .genre(genre)
                            .lengthSeconds(update.lengthSeconds() != null
                                    ? update.lengthSeconds()
                                    : track.getLengthSeconds())
                            .build()))
                .flatMap(trackRepository::save);
    }

    public Mono<Void> deleteTrack(String trackId) {
        return trackId != null
                ? trackRepository.deleteById(trackId)
                : Mono.error(InvalidParametersException.track("trackId"));
    }

    private Mono<Track> handleNotFound() {
        return Mono.error(NotFoundException.track());
    }

    private Mono<Set<Artist>> getArtistsForNewTrack(TrackCreationParamsDto creationParams) {
        return artistRepository.findAllById(new HashSet<>(creationParams.artistIds()))
                .filter(artists -> !artists.isEmpty())
                .switchIfEmpty(Mono.error(InvalidParametersException.track("artistIds")));
    }

    /**
     * Retrieves an updated {@link Genre} if updated, or the original if not.
     * @param update Track update parameters potentially containing a genre update
     * @param track The track to be updated
     */
    private Mono<Genre> getUpdatedGenre(TrackUpdateParamsDto update, Track track) {
        return update.genreId() != null
                ? genreRepository.findById(update.genreId())
                : Mono.just(track.getGenre());
    }

}
