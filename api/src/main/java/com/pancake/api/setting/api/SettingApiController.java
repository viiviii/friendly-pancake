package com.pancake.api.setting.api;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.setting.application.SetEnablePlatform;
import com.pancake.api.setting.domain.DisableDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingApiController {

    private final SetEnablePlatform setEnablePlatform;


    @PutMapping("platforms/{platform}")
    public ResponseEntity<Void> enablePlatform(@PathVariable Platform platform, @RequestBody EnableRequest request) {
        setEnablePlatform.command(platform, new DisableDateTime(request.disabledAt()));

        return status(NO_CONTENT).build();
    }

    public record EnableRequest(Instant disabledAt) {
    }
}
