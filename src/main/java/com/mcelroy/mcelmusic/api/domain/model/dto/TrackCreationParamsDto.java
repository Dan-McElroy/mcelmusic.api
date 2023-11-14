package com.mcelroy.mcelmusic.api.domain.model.dto;

import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.List;

@Builder
public record TrackCreationParamsDto(
        String title,
        List<String> artistIds,
        @Nullable
        String genreId,
        Integer lengthSeconds,
        String albumId) {
}
