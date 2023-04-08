package com.pancake.api.content.infra;

import com.pancake.api.content.domain.Content;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ContentRepository extends Repository<Content, Long> {

    Content save(Content content);

    List<Content> findAll();

    List<Content> findByWatchedFalse();
}
