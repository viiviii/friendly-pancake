package com.pancake.api.watch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.pancake.api.content.application.Builders.aPlayback;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@WebMvcTest(GetWatchApi.class)
class GetWatchApiTest {

    @MockBean
    LoadPlayback loadPlayback;

    @Autowired
    WebTestClient client;

    @Test
    void redirectToPlaybackUrl() {
        //given
        var playback = aPlayback().build();
        given(loadPlayback.query(anyLong())).willReturn(playback);

        //when
        var response = client.get().uri("/api/watch/{id}", anyLong()).exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isSeeOther(),
                spec -> spec.expectHeader().location(playback.getUrl()),
                spec -> spec.expectBody(Void.class)
        );
    }
}