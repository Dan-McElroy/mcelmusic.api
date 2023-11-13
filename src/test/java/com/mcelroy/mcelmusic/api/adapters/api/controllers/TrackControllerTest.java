package com.mcelroy.mcelmusic.api.adapters.api.controllers;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import com.mcelroy.mcelmusic.api.domain.model.dto.TrackCreationParamsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@WebFluxTest(TrackController.class)
public class TrackControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    void givenValidTrackCreationParams_whenCreatingTrack_thenReturnOk() {
         var trackCreationParams = TrackCreationParamsDto.builder()
                 .title("Test track")
                 .albumId("Test album ID")
                 .artistIds(List.of("Artist ID 1", "Artist ID 2"))
                 .lengthSeconds(60)
                 .genreId("Test genre ID")
                 .build();

         var expectedTrack = Track.builder()
                 .title("Test track")
                 .albumId("Test album ID")
                 .artists(List.of("Artist ID 1", "Artist ID 2"))
                 .lengthSeconds(60)
                 .genreId("Test genre ID")
                 .build();
         client.put()
                 .uri("/track")
                 .accept(MediaType.APPLICATION_JSON)
                 .contentType(MediaType.APPLICATION_JSON)
                 .bodyValue(trackCreationParams)
                 .exchange()
                 .expectStatus()
                 .isOk()
                 .expectBody(Track.class)
                 .isEqualTo(expectedTrack);
    }
}
