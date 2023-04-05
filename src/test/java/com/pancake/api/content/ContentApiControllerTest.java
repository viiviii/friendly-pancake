package com.pancake.api.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContentApiController.class)
class ContentApiControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;


    @Test
    void save() throws Exception {
        //given
        var request = new ContentRequest("https://www.netflix.com/watch/60023642?trackId=14234261", "센과 치히로의 행방불명");

        given(contentService.save(any())).willReturn(new Content(1L));

        //when
        var result = post("/api/contents", asJsonString(request));

        //then
        verify(contentService).save(request);

        result.andExpectAll(
                status().isCreated(),
                jsonPath("$.id").exists()
        );
    }

    private ResultActions post(String path, String inputJsonForCreate) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJsonForCreate)
        );
    }

    private String asJsonString(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

}
