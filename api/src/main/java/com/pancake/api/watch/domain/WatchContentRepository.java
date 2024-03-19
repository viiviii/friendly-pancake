package com.pancake.api.watch.domain;

import java.util.List;
import java.util.Optional;

public interface WatchContentRepository {

    List<WatchContent> findAll();

    Optional<WatchContent> findById(long id);
}
