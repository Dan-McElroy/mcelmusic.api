package com.mcelroy.mcelmusic.api.domain.model.dto;

import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.Set;

@Builder
public record ArtistCreationParamsDto(
        String name,
        @Nullable
        Set<String> aliases,
        @Nullable
        String profilePictureUrl
) {

}
