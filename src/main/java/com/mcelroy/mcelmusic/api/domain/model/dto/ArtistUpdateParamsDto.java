package com.mcelroy.mcelmusic.api.domain.model.dto;

import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.List;

@Builder
public record ArtistUpdateParamsDto(
        int version,
        @Nullable
        String name,
        @Nullable
        List<String> aliases,
        @Nullable
        String profilePictureUrl
) {

}
