package com.pancake.api.content.domain;

import java.util.List;
import java.util.Optional;

public interface ContentRepository {

    Content save(Content content);

    List<Content> findAll();

    Optional<Content> findById(long id);
}
