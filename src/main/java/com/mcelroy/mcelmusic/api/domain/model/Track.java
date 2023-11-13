package com.mcelroy.mcelmusic.api.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Track {
    String id;
    int version;
    String title;
    List<String> artists;
    String genreId;
    int lengthSeconds;
    String albumId;
}
