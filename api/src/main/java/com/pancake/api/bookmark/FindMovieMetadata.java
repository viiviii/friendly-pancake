package com.pancake.api.bookmark;

import com.pancake.api.content.application.ContentMetadata;

public interface FindMovieMetadata {

    ContentMetadata findById(String id);
}
