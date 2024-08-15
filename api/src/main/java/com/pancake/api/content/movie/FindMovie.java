package com.pancake.api.content.movie;

import com.pancake.api.content.application.ContentMetadata;

public interface FindMovie {

    ContentMetadata findById(String id);
}
