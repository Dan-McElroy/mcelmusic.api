package com.mcelroy.mcelmusic.api.domain.model;

import com.mcelroy.mcelmusic.api.domain.model.dto.GenreCreationParamsDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Builder(toBuilder = true)
@Data
public class Genre {
    String id;
    @Nullable
    Integer version;
    @Nullable
    Instant creationTime;
    String name;

    public static Genre fromDto(GenreCreationParamsDto creationParams) {
        return Genre.builder()
                .name(creationParams.name())
                .build();
    }
}
