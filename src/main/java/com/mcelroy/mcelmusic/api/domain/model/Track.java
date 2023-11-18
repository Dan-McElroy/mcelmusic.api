package com.mcelroy.mcelmusic.api.domain.model;

import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder(toBuilder = true)
@Data
public class Track {
    String id;
    int version;
    String title;
    Set<Artist> artists;
    Genre genre;
    int lengthSeconds;

    public static Track fromDto(TrackCreationParamsDto dto) {
        return Track.builder()
                .title(dto.title())
                .lengthSeconds(dto.lengthSeconds())
                .build();
    }
}
