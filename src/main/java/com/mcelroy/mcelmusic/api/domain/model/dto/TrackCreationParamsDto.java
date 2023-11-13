package com.mcelroy.mcelmusic.api.domain.model.dto;

import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.List;

@Builder
public record TrackCreationParamsDto(
        @Nullable
        String title,
        @Nullable
        List<String> artistIds,
        @Nullable
        String genreId,
        @Nullable
        Integer lengthSeconds,
        @Nullable
        String albumId) {
}
