package com.pancake.api.content.api;

import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.domain.Content;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import java.util.List;

import static com.pancake.api.content.NetflixConstant.PONYO;
import static com.pancake.api.content.NetflixConstant.TOTORO;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(ContentApiController.class)
class ContentApiControllerTest {

    @MockBean
    ContentService contentService;

    @Autowired
    WebTestClient client;

    @Test
    void postContentApi() {
        //given
        var request = new ContentRequest(TOTORO.URL, TOTORO.TITLE, TOTORO.DESCRIPTION, TOTORO.IMAGE_URL);
        var content = content(128, TOTORO.URL, TOTORO.TITLE, TOTORO.DESCRIPTION, TOTORO.IMAGE_URL);

        given(contentService.save(any())).willReturn(content);

        //when
        var result = post("/api/contents", request);

        //then
        result
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").value(equalTo(128))
                .jsonPath("$.url").value(equalTo(TOTORO.URL))
                .jsonPath("$.title").value(equalTo(TOTORO.TITLE))
                .jsonPath("$.description").value(equalTo(TOTORO.DESCRIPTION))
                .jsonPath("$.imageUrl").value(equalTo(TOTORO.IMAGE_URL));
    }

    @Test
    void getAllContentsApi() {
        //given
        given(contentService.getAllContents()).willReturn(List.of(
                content(1001, TOTORO.URL, TOTORO.TITLE, TOTORO.DESCRIPTION, TOTORO.IMAGE_URL),
                content(1002, PONYO.URL, PONYO.TITLE, PONYO.DESCRIPTION, PONYO.IMAGE_URL)
        ));

        //when
        var result = get("/api/contents");

        //then
        result
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$..id").value(contains(1001, 1002))
                .jsonPath("$..url").value(contains(TOTORO.URL, PONYO.URL))
                .jsonPath("$..title").value(contains(TOTORO.TITLE, PONYO.TITLE))
                .jsonPath("$..description").value(contains(TOTORO.DESCRIPTION, PONYO.DESCRIPTION))
                .jsonPath("$..imageUrl").value(contains(TOTORO.IMAGE_URL, PONYO.IMAGE_URL));
    }

    @Test
    void patchWatchContentApi() throws Exception {
        //given
        given(contentService.watch(anyLong())).willReturn(true);

        //when
        var result = patch("/api/contents/{id}/watch", 1234);

        //then
        result
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(equalTo(true));
    }

    private Content content(long id, String url, String title, String description, String imageUrl) {
        return new Content(id, url, title, description, imageUrl, false);
    }

    private ResponseSpec get(String path, Object... uriVariables) {
        return client.get().uri(path, uriVariables).exchange();
    }

    private ResponseSpec post(String path, Object body) {
        return client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue(body)
                .exchange();
    }

    private ResponseSpec patch(String path, Object... uriVariables) {
        return client.patch().uri(path, uriVariables).exchange();
    }
}
