package com.pancake.api.bookmark;

import com.pancake.api.bookmark.api.BookmarkCustomApiController;
import com.pancake.api.bookmark.application.BookmarkCustom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.pancake.api.bookmark.Builders.aBookmarkCustom;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(BookmarkCustomApiController.class)
class BookmarkCustomApiControllerTest {

    @Autowired
    WebTestClient client;

    @MockBean
    BookmarkCustom bookmarkCustom;

    @Test
    void post() {
        //given
        var command = aBookmarkCustom().build();

        //when
        var response = client.post().uri("/api/bookmarks/customs")
                .contentType(APPLICATION_JSON)
                .bodyValue(command)
                .exchange();

        //then
        verify(bookmarkCustom).command(command);

        response.expectAll(
                spec -> spec.expectStatus().isCreated()
        );
    }
}
