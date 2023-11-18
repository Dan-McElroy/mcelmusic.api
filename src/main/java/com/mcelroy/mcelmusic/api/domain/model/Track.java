package com.mcelroy.mcelmusic.api.domain.model;

import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Builder(toBuilder = true)
@Data
public class Track {
    String id;
    int version;
    Instant creationTime;
    String title;
    Set<Artist> artists;
    Genre genre;
    int lengthSeconds;

    public void addArtist(Artist artist) {
        this.artists.add(artist);
        artist.getTracks().add(this);
    }

    public static Track fromDto(TrackCreationParamsDto dto) {
        return Track.builder()
                .version(1)
                .title(dto.title())
                .lengthSeconds(dto.lengthSeconds())
                .build();
    }
}
