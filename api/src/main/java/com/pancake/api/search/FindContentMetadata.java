package com.pancake.api.search;

import com.pancake.api.content.application.ContentMetadata;
import org.springframework.data.domain.Page;

public interface FindContentMetadata {

    Page<ContentMetadata> findAllByTitle(String title);
}
