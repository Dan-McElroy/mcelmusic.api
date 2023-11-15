package com.mcelroy.mcelmusic.api.domain.service;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistCreationParamsDto;
import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistUpdateParamsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;

@Service
@AllArgsConstructor
public class ArtistService {

    public Mono<Artist> createArtist(ArtistCreationParamsDto artistCreationParams) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Artist> getArtist(String artistId) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Artist> getArtistOfTheDay(Instant currentTime) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Artist> updateArtist(String artistId, ArtistUpdateParamsDto artistUpdateParam) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Void> deleteArtist(String artistId) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }
}