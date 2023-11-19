package com.mcelroy.mcelmusic.api.domain.model.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotFoundException extends RuntimeException {

    private static final String TRACK_NOT_FOUND = "Track not found";
    private static final String ARTIST_NOT_FOUND = "Artist not found";
    private static final String GENRE_NOT_FOUND = "Genre not found";

    private final String reason;

    public static NotFoundException track() {
        return new NotFoundException(TRACK_NOT_FOUND);
    }

    public static NotFoundException artist() {
        return new NotFoundException(ARTIST_NOT_FOUND);
    }

    public static NotFoundException genre() {
        return new NotFoundException(GENRE_NOT_FOUND);
    }
}
