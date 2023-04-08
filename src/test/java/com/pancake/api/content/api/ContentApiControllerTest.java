package com.pancake.api.content.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.domain.Content;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.pancake.api.content.NetflixConstant.PONYO;
import static com.pancake.api.content.NetflixConstant.TOTORO;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContentApiController.class)
class ContentApiControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;


    @Test
    void postContentApi() throws Exception {
        //given
        var request = new ContentRequest(TOTORO.URL, TOTORO.TITLE);

        given(contentService.save(any())).willReturn(unwatchedContent(128L, request.getUrl(), request.getTitle()));

        //when
        var result = post("/api/contents", asJsonString(request));

        //then
        verify(contentService).save(request);

        result.andExpectAll(
                status().isCreated(),
                jsonPath("$.id").exists()
        );
    }

    @Test
    void getAllContentsApi() throws Exception {
        //given
        given(contentService.getAllContents()).willReturn(List.of(
                unwatchedContent(1L, TOTORO.URL, TOTORO.TITLE),
                unwatchedContent(2L, PONYO.URL, PONYO.TITLE)
        ));

        //when
        var result = get("/api/contents");


        //then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$..url").value(contains(TOTORO.URL, PONYO.URL)),
                jsonPath("$..title").value(contains(TOTORO.TITLE, PONYO.TITLE))
        );
    }

    @Test
    void getUnwatchedContentsApi() throws Exception {
        //given
        given(contentService.getUnwatchedContents()).willReturn(List.of(
                unwatchedContent(1L, TOTORO.URL, TOTORO.TITLE),
                unwatchedContent(2L, PONYO.URL, PONYO.TITLE)
        ));

        //when
        var result = get("/api/contents/unwatched");

        //then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$..url").value(contains(TOTORO.URL, PONYO.URL)),
                jsonPath("$..title").value(contains(TOTORO.TITLE, PONYO.TITLE))
        );
    }

    @Test
    void getWatchedContentsApi() throws Exception {
        //given
        given(contentService.getWatchedContents()).willReturn(List.of(
                watchedContent(1L, TOTORO.URL, TOTORO.TITLE),
                watchedContent(2L, PONYO.URL, PONYO.TITLE)
        ));

        //when
        var result = get("/api/contents/watched");

        //then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$..url").value(contains(TOTORO.URL, PONYO.URL)),
                jsonPath("$..title").value(contains(TOTORO.TITLE, PONYO.TITLE))
        );
    }

    @Test
    void patchWatchedContentApi() throws Exception {
        //given
        given(contentService.watch(anyLong())).willReturn(true);

        //when
        var result = patch("/api/contents/{id}/watch", 6789);

        //then
        verify(contentService).watch(6789);

        result.andExpectAll(
                status().isOk(),
                content().string(equalTo("true"))
        );
    }

    private Content unwatchedContent(long id, String url, String title) {
        return createContent(id, url, title, false);
    }

    private Content watchedContent(long id, String url, String title) {
        return createContent(id, url, title, true);
    }

    private Content createContent(long id, String url, String title, boolean watched) {
        return new Content(id, url, title, watched);
    }

    private ResultActions get(String path, Object... uriVariables) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.get(path, uriVariables)
        );
    }

    private ResultActions post(String path, String inputJsonForCreate) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJsonForCreate)
        );
    }

    protected final ResultActions patch(String urlTemplate, Object... uriVariables) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.patch(urlTemplate, uriVariables)
        );
    }

    private String asJsonString(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

}
