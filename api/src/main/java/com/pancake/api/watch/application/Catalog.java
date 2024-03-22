package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.WatchContent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class Catalog {

    private String title;
    
    private List<WatchContent> contents;

    public Catalog(List<WatchContent> contents) {
        this.title = "추천해요"; // TODO: 하드코딩 제거
        this.contents = contents;
    }
}
