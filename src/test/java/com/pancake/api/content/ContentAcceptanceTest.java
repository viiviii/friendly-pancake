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

import static com.pancake.api.content.NetflixConstant.PONYO;
import static com.pancake.api.content.NetflixConstant.TOTORO;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
        var 컨텐츠 = new ContentRequest(TOTORO.URL, TOTORO.TITLE);

        //when
        var 등록된_컨텐츠 = 등록("/api/contents", asJsonString(컨텐츠)).as(ContentResponse.class);

        //then
        assertThat(등록된_컨텐츠.getId()).isNotNull(); // TODO
    }

    @Test
    void 컨텐츠를_모두_조회한다() throws Exception {
        //given
        컨텐츠_등록(TOTORO.URL, TOTORO.TITLE);
        컨텐츠_등록(PONYO.URL, PONYO.TITLE);

        //when
        var 모든_컨텐츠 = 조회("/api/contents").as(ContentResponse[].class);

        //then
        assertThat(모든_컨텐츠).extracting(ContentResponse::getTitle)
                .containsExactly(TOTORO.TITLE, PONYO.TITLE);
    }

    @Test
    void 시청할_컨텐츠를_모두_조회한다() throws Exception {
        //given
        컨텐츠_등록(TOTORO.URL, TOTORO.TITLE);
        컨텐츠_등록(PONYO.URL, PONYO.TITLE);

        //when
        var 시청할_컨텐츠_목록 = 조회("/api/contents/unwatched").as(ContentResponse[].class);

        //then
        assertThat(시청할_컨텐츠_목록).extracting(ContentResponse::getTitle)
                .containsExactly(TOTORO.TITLE, PONYO.TITLE);
    }

    @Test
    void 시청한_컨텐츠를_모두_조회한다() throws Exception {
        //given
        시청(컨텐츠_등록(TOTORO.URL, TOTORO.TITLE).getId());
        시청(컨텐츠_등록(PONYO.URL, PONYO.TITLE).getId());

        //when
        var 시청한_컨텐츠_목록 = 조회("/api/contents/watched").as(ContentResponse[].class);

        //then
        assertThat(시청한_컨텐츠_목록).extracting(ContentResponse::getTitle)
                .containsExactly(TOTORO.TITLE, PONYO.TITLE);
    }

    @Test
    void 컨텐츠를_시청_처리한다() throws Exception {
        //given
        컨텐츠_등록(TOTORO.URL, TOTORO.TITLE);

        var 시청할_컨텐츠 = 컨텐츠_등록(PONYO.URL, PONYO.TITLE);

        //when
        변경("/api/contents/{id}/watch", 시청할_컨텐츠.getId()).as(Boolean.class);

        //then
        assertAll(
                () -> assertThat(시청할_컨텐츠_목록_조회()).extracting(ContentResponse::getTitle)
                        .contains(TOTORO.TITLE)
                        .doesNotContain(PONYO.TITLE),
                () -> assertThat(시청한_컨텐츠_목록_조회()).extracting(ContentResponse::getTitle)
                        .containsExactly(PONYO.TITLE)
        );
    }

    private ContentResponse 컨텐츠_등록(String url, String title) throws Exception {
        return 등록("/api/contents", asJsonString(new ContentRequest(url, title))).as(ContentResponse.class);
    }

    private ContentResponse[] 시청할_컨텐츠_목록_조회() {
        return 조회("/api/contents/unwatched").as(ContentResponse[].class);
    }

    private ContentResponse[] 시청한_컨텐츠_목록_조회() {
        return 조회("/api/contents/watched").as(ContentResponse[].class);
    }

    private void 시청(long 컨텐츠_아이디) {
        변경("/api/contents/{id}/watch", 컨텐츠_아이디).as(Boolean.class);
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
