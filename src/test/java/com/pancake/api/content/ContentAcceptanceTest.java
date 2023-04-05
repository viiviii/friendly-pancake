package com.pancake.api.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
class ContentAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 컨텐츠를_등록한다() throws Exception {
        //given
        var 컨텐츠 = new ContentRequest("https://www.netflix.com/watch/60023642?trackId=14234261", "센과 치히로의 행방불명");

        //when
        var 응답 = 등록("/api/contents", asJsonString(컨텐츠)).as(ContentResponse.class);

        //then
        assertThat(응답.getId()).isNotNull();
    }

    private ExtractableResponse<Response> 등록(String path, String inputJsonForCreate) {
        //@formatter:off
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(inputJsonForCreate)
        .when()
                .post(path)
        .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        //@formatter:on
    }

    protected String asJsonString(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    private class ContentRequest {

        private final String url;
        private final String title;

        private ContentRequest(String url, String title) {
            this.url = url;
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }
    }

    private class ContentResponse {
        private final Long id;

        private ContentResponse(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
