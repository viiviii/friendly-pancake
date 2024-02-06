package com.pancake.api.content.application;

import com.pancake.api.content.domain.Watch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddWatchCommand {

    private String url;

    public Watch toEntity() {
        return new Watch(toURL());
    }

    private URL toURL() {
        // TODO
        try {
            return new URI(getUrl()).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
