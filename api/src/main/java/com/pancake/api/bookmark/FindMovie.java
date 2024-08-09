package com.pancake.api.bookmark;

import com.pancake.api.content.application.ContentMetadata;

public interface FindMovie {

    ContentMetadata findById(String id);
}
