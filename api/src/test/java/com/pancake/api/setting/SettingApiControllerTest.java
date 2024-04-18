package com.pancake.api.setting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.pancake.api.content.domain.Platform.NETFLIX;
import static java.time.LocalDate.parse;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(SettingApiController.class)
@SuppressWarnings("NonAsciiCharacters")
class SettingApiControllerTest {

    @MockBean
    SetEnablePlatform enablePlatform;

    @Autowired
    WebTestClient client;

    @Test
    void 비활성화_날짜로_플랫폼_활성화를_설정한다() {
        //given
        var request = new SettingApiController.EnableRequest(parse("2080-09-01"));

        //when
        var response = client.put()
                .uri("/api/settings/platforms/{name}", NETFLIX)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        verify(enablePlatform).command(NETFLIX, parse("2080-09-01"));
        response.expectAll(
                spec -> spec.expectStatus().isNoContent(),
                spec -> spec.expectBody(Void.class)
        );
    }

}