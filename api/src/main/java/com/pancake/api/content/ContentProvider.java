package com.pancake.api.content;

import com.pancake.api.content.application.ContentMetadata;

public interface ContentProvider {

    ContentType provideType();

    ContentMetadata getBy(String contentId);
}
