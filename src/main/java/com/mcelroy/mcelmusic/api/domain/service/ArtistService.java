package com.mcelroy.mcelmusic.api.domain.service;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@AllArgsConstructor
public class ArtistService {

    public Mono<Artist> createArtist(Artist artist) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Artist> getArtist(String artistId) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Artist> getArtistOfTheDay(Date date) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Artist> updateArtist(Artist artist) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

    public Mono<Artist> deleteArtist(String artistId) {
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }
}