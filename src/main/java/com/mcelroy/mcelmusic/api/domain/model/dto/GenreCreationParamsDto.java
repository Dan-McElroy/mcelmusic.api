package com.mcelroy.mcelmusic.api.domain.model.dto;

import lombok.Builder;

@Builder
public record GenreCreationParamsDto(String name) {
}