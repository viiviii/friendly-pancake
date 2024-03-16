package com.pancake.api.watch.api;

import com.pancake.api.watch.application.GetWatchUrl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@WebMvcTest(WatchRedirectController.class)
class WatchRedirectControllerTest {

    @MockBean
    GetWatchUrl getWatchUrl;

    @Autowired
    WebTestClient client;

    @Test
    void redirectToUrl() {
        //given
        given(getWatchUrl.query(anyLong())).willReturn("https://www.netflix.com/watch/1");

        //when
        var response = client.get().uri("/api/watch/{id}", anyLong()).exchange(); // TODO : {id}

        //then
        response.expectAll(
                spec -> spec.expectStatus().isSeeOther(),
                spec -> spec.expectHeader().location("https://www.netflix.com/watch/1"),
                spec -> spec.expectBody(Void.class)
        );
    }
}