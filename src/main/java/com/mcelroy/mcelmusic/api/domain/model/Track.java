package com.mcelroy.mcelmusic.api.domain.model;

import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Track {
    String id;
    int version;
    String title;
    List<String> artistIds;
    String genreId;
    int lengthSeconds;
    String albumId;

    public static Track fromDto(TrackCreationParamsDto dto) {
        return Track.builder()
                .version(1)
                .title(dto.title())
                .artistIds(dto.artistIds())
                .genreId(dto.genreId())
                .lengthSeconds(dto.lengthSeconds())
                .albumId(dto.albumId())
                .build();
    }
}
