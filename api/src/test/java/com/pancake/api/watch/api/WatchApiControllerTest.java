package com.pancake.api.watch.api;

import com.pancake.api.watch.application.Catalog;
import com.pancake.api.watch.application.GetContentsToWatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.pancake.api.watch.Builders.aWatchContent;
import static org.mockito.BDDMockito.given;

@WebMvcTest(WatchApiController.class)
class WatchApiControllerTest {

    @MockBean
    GetContentsToWatch getContentsToWatch;

    @Autowired
    WebTestClient client;

    @Test
    void get() {
        //given
        var expected = new Catalog(List.of(aWatchContent().build()));

        given(getContentsToWatch.query()).willReturn(expected);

        //when
        var response = client.get().uri("/api/watches").exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBody(Catalog.class).isEqualTo(expected)
        );
    }
}
