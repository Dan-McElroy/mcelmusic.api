package com.mcelroy.mcelmusic.api.domain.model.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VersionConflictException extends RuntimeException {

    private static final String TRACK_VERSION_CONFLICT = "Track version conflict";
    private static final String ARTIST_VERSION_CONFLICT = "Artist version conflict";

    private final String reason;

    public static VersionConflictException track() {
        return new VersionConflictException(TRACK_VERSION_CONFLICT);
    }

    public static VersionConflictException artist() {
        return new VersionConflictException(ARTIST_VERSION_CONFLICT);
    }
}