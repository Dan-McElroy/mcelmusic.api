package com.mcelroy.mcelmusic.api.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class Genre {
    String id;
    int version;
    Instant creationTime;
    String name;
}
