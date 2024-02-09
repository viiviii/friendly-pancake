package com.pancake.api.watch;

import com.pancake.api.content.domain.Playback;
import com.pancake.api.content.domain.PlaybackUrl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

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
        given(loadPlayback.query(anyLong())).willReturn(playback("https://www.netflix.com/watch/123"));

        //when
        var response = client.get().uri("/api/watch/{id}", anyLong()).exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isSeeOther(),
                spec -> spec.expectHeader().location("https://www.netflix.com/watch/123"),
                spec -> spec.expectBody(Void.class)
        );
    }

    private Playback playback(String url) {
        final var playbackUrl = new PlaybackUrl(url);

        return new Playback(playbackUrl);
    }
}