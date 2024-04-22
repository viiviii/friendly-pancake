package com.pancake.api.watch.domain;

import com.pancake.api.content.domain.Platform;

import java.time.Instant;
import java.util.List;

public interface FindEnabledPlatforms {

    List<Platform> findEnabledPlatformsAt(Instant instant);
}
