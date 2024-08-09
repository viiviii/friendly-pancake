package com.pancake.api.bookmark;

import com.pancake.api.bookmark.application.BookmarkService;
import com.pancake.api.bookmark.domain.BookmarkRepository;
import com.pancake.api.content.GetContent;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import static com.pancake.api.bookmark.Builders.aBookmarkSaveCommand;
import static com.pancake.api.content.Builders.aMetadata;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
class BookmarkServiceTest {

    private final GetContent getContent = mock(GetContent.class);
    private final BookmarkRepository bookmarkRepository = mock(BookmarkRepository.class);
    private final BookmarkService bookmarkService = new BookmarkService(getContent, bookmarkRepository);

    @Test
    void 메타데이터가_존재하지_않으면_예외가_발생한다() {
        //given
        var bookmark = aBookmarkSaveCommand()
                .contentId("8392")
                .contentType("movie")
                .build();

        given(getContent.queryBy("8392", "movie")).willReturn(null);

        //when
        ThrowingCallable actual = () -> bookmarkService.save(bookmark);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장한_제목과_검색결과의_제목이_다른_경우_예외가_발생한다() {
        //given
        var bookmark = aBookmarkSaveCommand()
                .title("저장할 제목")
                .contentId("8392")
                .contentType("movie")
                .build();

        given(getContent.queryBy("8392", "movie"))
                .willReturn(aMetadata().title("다른 제목").build());


        //when
        ThrowingCallable actual = () -> bookmarkService.save(bookmark);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}
