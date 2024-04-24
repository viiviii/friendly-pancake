package com.pancake.api.setting.api;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.setting.application.GetPlatformSettings;
import com.pancake.api.setting.application.SetEnablePlatform;
import com.pancake.api.setting.domain.Setting;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingApiController {

    private final GetPlatformSettings getPlatformSettings;
    private final SetEnablePlatform setEnablePlatform;

    @GetMapping("platforms")
    public ResponseEntity<List<PlatformSettingResponse>> enablePlatform() {
        final var response = getPlatformSettings.query().stream()
                .map(PlatformSettingResponse::new)
                .toList();

        return status(OK).body(response);
    }

    @PutMapping("platforms/{platform}")
    public ResponseEntity<Void> enablePlatform(@PathVariable Platform platform, @RequestBody EnableRequest request) {
        setEnablePlatform.command(platform, request.disableFrom());

        return status(NO_CONTENT).build();
    }

    public record EnableRequest(Instant disableFrom) {
    }

    public record PlatformSettingResponse(String platformLabel, String platformName, Instant disableFrom) {
        public PlatformSettingResponse(Setting setting) {
            this(setting.getPlatform().label(), setting.getPlatform().name(), setting.getDisableFrom());
        }
    }
}
