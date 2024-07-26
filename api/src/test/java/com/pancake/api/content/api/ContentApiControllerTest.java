package com.pancake.api.content.api;

import com.pancake.api.content.application.AddPlayback;
import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.domain.ContentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.pancake.api.content.Builders.aContentSaveCommand;
import static com.pancake.api.content.Builders.aStreaming;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(ContentApiController.class)
class ContentApiControllerTest {

    @MockBean
    ContentService contentService;

    @MockBean
    ContentRepository repository;

    @MockBean
    AddPlayback addPlayback;

    @Autowired
    WebTestClient client;

    @Test
    void saveContent() {
        //given
        var request = aContentSaveCommand().build();
        var content = request.toContent();

        given(contentService.save(request)).willReturn(content);

        //when
        var response = client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isCreated(),
                spec -> spec.expectBody(ContentResponse.class).isEqualTo(new ContentResponse(content))
        );
    }

    @Test
    void addPlayback() {
        //given
        var request = aStreaming().build();

        //when
        var response = client.post().uri("/api/contents/{id}/playbacks", 1L)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isNoContent(),
                spec -> spec.expectBody(Void.class)
        );
    }

    @Test
    void changeToWatched() {
        //when
        var response = client.patch().uri("/api/contents/{id}/watched", 1234).exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isNoContent(),
                spec -> spec.expectBody(Void.class)
        );
    }
}
