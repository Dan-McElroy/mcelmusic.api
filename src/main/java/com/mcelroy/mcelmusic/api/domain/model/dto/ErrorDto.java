package com.mcelroy.mcelmusic.api.domain.model.dto;


import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Map;

@Value
@Builder
public class ErrorDto {
    @Builder.Default
    Instant timeOccurred = Instant.now();

    String failureReason;

    Map<String, ?> details;
}
