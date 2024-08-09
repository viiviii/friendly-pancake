package com.pancake.api.content;

import com.pancake.api.content.application.ContentMetadata;

public interface ContentProvider {

    String provideType();

    ContentMetadata getBy(String contentId);
}
