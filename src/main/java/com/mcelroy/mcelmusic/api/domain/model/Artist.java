package com.mcelroy.mcelmusic.api.domain.model;

import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistCreationParamsDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Artist {
    String id;
    int version;
    String name;
    List<String> aliases;
    String profilePictureUrl;

    public static Artist fromDto(ArtistCreationParamsDto dto) {
        return Artist.builder()
                .version(1)
                .name(dto.name())
                .aliases(dto.aliases())
                .profilePictureUrl(dto.profilePictureUrl())
                .build();
    }
}
