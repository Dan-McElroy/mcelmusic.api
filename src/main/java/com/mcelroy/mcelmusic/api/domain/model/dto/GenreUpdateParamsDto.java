package com.mcelroy.mcelmusic.api.domain.model.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record GenreUpdateParamsDto(
        int version,
        @NonNull
        String name) {
}