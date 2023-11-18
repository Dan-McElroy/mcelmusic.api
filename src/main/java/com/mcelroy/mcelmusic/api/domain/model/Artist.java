package com.mcelroy.mcelmusic.api.domain.model;

import com.mcelroy.mcelmusic.api.domain.model.dto.ArtistCreationParamsDto;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder(toBuilder = true)
@Data
public class Artist {
    String id;
    int version;
    String name;
    Set<String> aliases;
    String profilePictureUrl;

    public static Artist fromDto(ArtistCreationParamsDto dto) {
        return Artist.builder()
                .name(dto.name())
                .profilePictureUrl(dto.profilePictureUrl())
                .aliases(dto.aliases())
                .build();
    }
}
