package com.mcelroy.mcelmusic.api.domain.model.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InvalidParametersException extends RuntimeException {

    private static final String INVALID_TRACK_PARAMETERS = "Invalid track parameters";
    private static final String INVALID_ARTIST_PARAMETERS = "Invalid artist parameters";

    private final String reason;

    @Nullable
    private final List<String> invalidParameters;

    public static InvalidParametersException track(@Nullable List<String> invalidParameters) {
        return new InvalidParametersException(INVALID_TRACK_PARAMETERS, invalidParameters);
    }

    public static InvalidParametersException artist(@Nullable List<String> invalidParameters) {
        return new InvalidParametersException(INVALID_ARTIST_PARAMETERS, invalidParameters);
    }
}
