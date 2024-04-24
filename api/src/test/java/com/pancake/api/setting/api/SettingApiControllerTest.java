package com.pancake.api.setting.api;

import com.pancake.api.setting.api.SettingApiController.PlatformSettingResponse;
import com.pancake.api.setting.application.GetPlatformSettings;
import com.pancake.api.setting.application.SetEnablePlatform;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.pancake.api.content.domain.Platform.NETFLIX;
import static com.pancake.api.setting.Builders.aSetting;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(SettingApiController.class)
@SuppressWarnings("NonAsciiCharacters")
class SettingApiControllerTest {

    @MockBean
    SetEnablePlatform setEnablePlatform;

    @MockBean
    GetPlatformSettings getPlatformSettings;

    @Autowired
    WebTestClient client;

    @Test
    void null_값으로_플랫폼_활성화를_설정한다() {
        //given
        var request = "{\"disableFrom\": null}";

        //when
        var response = client.put()
                .uri("/api/settings/platforms/{name}", NETFLIX)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        verify(setEnablePlatform).command(NETFLIX, disableFrom(null));
        response.expectAll(
                spec -> spec.expectStatus().isNoContent(),
                spec -> spec.expectBody(Void.class)
        );
    }

    @Test
    void 비활성화_날짜로_플랫폼_활성화를_설정한다() {
        //given
        var request = Map.of("disableFrom", "2080-09-01T00:00:00Z");

        //when
        var response = client.put()
                .uri("/api/settings/platforms/{name}", NETFLIX)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        verify(setEnablePlatform).command(NETFLIX, disableFrom("2080-09-01T00:00:00Z"));
        response.expectAll(
                spec -> spec.expectStatus().isNoContent(),
                spec -> spec.expectBody(Void.class)
        );
    }

    @Test
    void 플랫폼_설정을_조회한다() {
        //given
        var setting = aSetting().build();
        given(getPlatformSettings.query()).willReturn(List.of(setting));

        //when
        var response = client.get().uri("/api/settings/platforms").exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBodyList(PlatformSettingResponse.class)
                        .isEqualTo(List.of(new PlatformSettingResponse(setting)))
        );
    }

    private Instant disableFrom(String value) {
        if (value == null) {
            return null;
        }
        return Instant.parse(value);
    }
}