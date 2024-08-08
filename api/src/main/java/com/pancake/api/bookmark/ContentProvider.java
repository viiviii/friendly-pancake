package com.pancake.api.bookmark;

import com.pancake.api.content.application.ContentMetadata;

public interface ContentProvider {

    String provideType();

    ContentMetadata getBy(String contentId);
}
