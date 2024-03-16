package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.WatchContent;
import com.pancake.api.watch.domain.WatchOption;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetContentsToWatchResult {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private boolean watched;

    private List<OptionResult> options;

    public GetContentsToWatchResult(WatchContent watchContent) {
        this.id = watchContent.getId();
        this.title = watchContent.getTitle();
        this.description = watchContent.getDescription();
        this.imageUrl = watchContent.getImageUrl();
        this.watched = watchContent.isWatched();
        this.options = toOptions(watchContent.getOptions());
    }

    private static List<OptionResult> toOptions(List<WatchOption> watchingOptions) {
        return watchingOptions.stream().map(OptionResult::new).toList();
    }

    @Data
    @NoArgsConstructor
    public static class OptionResult {
        private String platformName;
        private String platformLabel;

        public OptionResult(WatchOption option) {
            this.platformName = option.getPlatform().name();
            this.platformLabel = option.getPlatform().label();
        }
    }
}
