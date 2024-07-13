package com.pancake.api.search;

import com.pancake.api.content.application.ContentMetadata;
import org.springframework.data.domain.Page;

public interface FindContentMetadata {

    ContentMetadata findById(String id);

    Page<ContentMetadata> findAllByTitle(String title);
}
