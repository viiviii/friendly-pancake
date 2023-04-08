package com.pancake.api.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.application.dto.ContentResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql("/database_cleanup.sql")
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
        var 등록된_컨텐츠 = 등록("/api/contents", asJsonString(컨텐츠)).as(ContentResponse.class);

        //then
        assertThat(등록된_컨텐츠.getId()).isNotNull();
    }

    @Test
    void 컨텐츠를_모두_조회한다() throws Exception {
        //given
        컨텐츠_등록("https://www.netflix.com/watch/70028883?trackId=255824129", "하울의 움직이는 성");
        컨텐츠_등록("https://www.netflix.com/watch/60032294?trackId=254245392", "이웃집 토토로");

        //when
        var 모든_컨텐츠 = 조회("/api/contents").as(ContentResponse[].class);

        //then
        assertThat(모든_컨텐츠)
                .extracting(ContentResponse::getTitle)
                .containsExactly("하울의 움직이는 성", "이웃집 토토로");
    }

    @Test
    void 시청할_컨텐츠를_모두_조회한다() throws Exception {
        //given
        컨텐츠_등록("https://www.netflix.com/watch/70028883?trackId=255824129", "하울의 움직이는 성");
        컨텐츠_등록("https://www.netflix.com/watch/60032294?trackId=254245392", "이웃집 토토로");

        //when
        var 모든_컨텐츠 = 조회("/api/contents/unwatched").as(ContentResponse[].class);

        //then
        assertThat(모든_컨텐츠)
                .extracting(ContentResponse::getTitle)
                .containsExactly("하울의 움직이는 성", "이웃집 토토로");
    }

    @Test
    void 컨텐츠를_시청_처리한다() throws Exception {
        //given
        var 등록된_컨텐츠_아이디 = 등록된_컨텐츠().getId();

        //when
        var 시청_처리 = 변경("/api/contents/{id}/watch", 등록된_컨텐츠_아이디).as(Boolean.class);

        //then
        assertThat(시청_처리).isTrue(); // TODO
    }

    private void 컨텐츠_등록(String url, String title) throws Exception {
        등록("/api/contents", asJsonString(new ContentRequest(url, title))).as(ContentResponse.class);
    }

    private ContentResponse 등록된_컨텐츠() throws Exception {
        var 컨텐츠 = new ContentRequest("https://www.netflix.com/watch/60023642?trackId=14234261", "센과 치히로의 행방불명");

        return 등록("/api/contents", asJsonString(컨텐츠)).as(ContentResponse.class);
    }


    protected ExtractableResponse<Response> 조회(String path, Object... pathParams) {
        //@formatter:off
        return given()
                .log().all()
        .when()
                .get(path, pathParams)
        .then()
                .log().all()
                .extract();
        //@formatter:on
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
                .extract();
        //@formatter:on
    }

    protected ExtractableResponse<Response> 변경(String path, Object... pathParams) {
        //@formatter:off
        return given()
                .log().all()
        .when()
                .patch(path, pathParams)
        .then()
                .log().all()
                .extract();
        //@formatter:on
    }

    protected String asJsonString(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
}
