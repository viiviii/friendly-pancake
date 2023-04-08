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
    void 컨텐츠를_등록할_수_있다() throws Exception {
        //given
        var 등록할_컨텐츠_주소 = "https://www.netflix.com/watch/60023642?trackId=14234261";
        var 등록할_컨텐츠_제목 = "센과 치히로의 행방불명";

        //when
        var 등록된_컨텐츠 = 컨텐츠를_등록한다(등록할_컨텐츠_주소, 등록할_컨텐츠_제목);

        //then
        assertThat(컨텐츠를_모두_조회한다()).containsExactly(등록된_컨텐츠);
    }

    @Test
    void 컨텐츠를_시청_처리_할_수_있다() throws Exception {
        //given
        var 포뇨 = 포뇨_컨텐츠();

        //when
        컨텐츠를_시청_처리_한다(포뇨.getId());

        //then
        assertThat(시청한_컨텐츠를_모두_조회한다()).containsExactly(포뇨);
    }

    @Test
    void 시청할_수_있는_컨텐츠를_모두_조회할_수_있다() throws Exception {
        //given
        var 토토로 = 토토로_컨텐츠();
        시청한(포뇨_컨텐츠());

        //when
        var 시청할_컨텐츠_목록 = 시청할_컨텐츠를_모두_조회한다();

        //then
        assertThat(시청할_컨텐츠_목록).containsExactly(토토로);
    }

    @Test
    void 이미_시청한_컨텐츠를_모두_조회할_수_있다() throws Exception {
        //given
        토토로_컨텐츠();
        var 포뇨 = 시청한(포뇨_컨텐츠());

        //when
        var 시청한_컨텐츠_목록 = 시청한_컨텐츠를_모두_조회한다();

        //then
        assertThat(시청한_컨텐츠_목록).containsExactly(포뇨);
    }

    @Test
    void 컨텐츠를_모두_조회할_수_있다() throws Exception {
        //given
        var 시청하지_않은_토토로 = 토토로_컨텐츠();
        var 시청한_포뇨 = 시청한(포뇨_컨텐츠());

        //when
        var 모든_컨텐츠_목록 = 컨텐츠를_모두_조회한다();

        //then
        assertThat(모든_컨텐츠_목록).containsExactly(시청하지_않은_토토로, 시청한_포뇨);
    }

    private ContentResponse 토토로_컨텐츠() throws Exception {
        return 컨텐츠를_등록한다(TOTORO.URL, TOTORO.TITLE);
    }

    private ContentResponse 포뇨_컨텐츠() throws Exception {
        return 컨텐츠를_등록한다(PONYO.URL, PONYO.TITLE);
    }

    private ContentResponse 시청한(ContentResponse content) {
        컨텐츠를_시청_처리_한다(content.getId());

        return content;
    }

    private ContentResponse 컨텐츠를_등록한다(String url, String title) throws Exception {
        return post("/api/contents", new ContentRequest(url, title)).as(ContentResponse.class);
    }

    private void 컨텐츠를_시청_처리_한다(long id) {
        patch("/api/contents/{id}/watch", id).as(Boolean.class);
    }

    private ContentResponse[] 컨텐츠를_모두_조회한다() {
        return get("/api/contents").as(ContentResponse[].class);
    }

    private ContentResponse[] 시청할_컨텐츠를_모두_조회한다() {
        return get("/api/contents/unwatched").as(ContentResponse[].class);
    }

    private ContentResponse[] 시청한_컨텐츠를_모두_조회한다() {
        return get("/api/contents/watched").as(ContentResponse[].class);
    }


    protected ExtractableResponse<Response> get(String path, Object... pathParams) {
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

    private ExtractableResponse<Response> post(String path, Object body) throws Exception {
        //@formatter:off
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(asJsonString(body))
        .when()
                .post(path)
        .then()
                .log().all()
                .extract();
        //@formatter:on
    }

    protected ExtractableResponse<Response> patch(String path, Object... pathParams) {
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
