package com.mcelroy.mcelmusic.api.domain.model;

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
}
