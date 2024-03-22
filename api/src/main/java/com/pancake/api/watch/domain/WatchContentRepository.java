package com.pancake.api.watch.domain;

import java.util.List;

public interface WatchContentRepository {

    List<WatchContent> findAll();
}
