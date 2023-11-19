package com.mcelroy.mcelmusic.api.domain.model.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InvalidParametersException extends RuntimeException {

    private static final String INVALID_TRACK_PARAMETERS = "Invalid track parameters";
    private static final String INVALID_ARTIST_PARAMETERS = "Invalid artist parameters";
    private static final String INVALID_GENRE_PARAMETERS = "Invalid genre parameters";

    private final String reason;

    private final Set<String> invalidParameters;

    public static InvalidParametersException track(String... invalidParameters) {
        return new InvalidParametersException(INVALID_TRACK_PARAMETERS, Set.of(invalidParameters));
    }

    public static InvalidParametersException artist(String... invalidParameters) {
        return new InvalidParametersException(INVALID_ARTIST_PARAMETERS, Set.of(invalidParameters));
    }

    public static InvalidParametersException genre(String... invalidParameters) {
        return new InvalidParametersException(INVALID_GENRE_PARAMETERS, Set.of(invalidParameters));
    }
}
