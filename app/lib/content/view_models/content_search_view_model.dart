import 'package:pancake_app/api/api.dart' show Api;
import 'package:pancake_app/content/models/content_search_model.dart';

class SearchViewModel {
  SearchViewModel(this._api);

  final Api _api;
  SearchResult? _current = const SearchResult.empty();

  Future<SearchResult>? searchBy(String query) {
    if (query.isEmpty || query == _current?.query) {
      return null;
    }

    return _searchBy(query);
  }

  Future<void> addToBookmark(SearchContent searchContent) async {
    await _postBookmark(searchContent);
  }

  Future<SearchResult> _searchBy(String query) async {
    final response = await _getMovie(query);
    final result = SearchResult(query: query, response: response);
    _current = result;

    return result;
  }

  Future<SearchResponse> _getMovie(String query) async {
    final response = await _api.get('search/contents?query=$query');

    return SearchResponse(response.body!);
  }

  Future<void> _postBookmark(SearchContent searchContent) async {
    await _api.post('bookmarks', body: {
      'contentSource': 'TMDB', // TODO: 하드코딩
      'contentId': searchContent.id,
      'contentType': 'movie', // TODO: 하드코딩, tv도 생길 예정임
      'title': searchContent.title,
    });
  }
}

class SearchResult with SearchResultMessage<SearchContent> {
  @override
  final String query;
  @override
  final bool hasMore;
  @override
  final List<SearchContent> data;

  const SearchResult.empty()
      : query = '',
        hasMore = false,
        data = const [];

  SearchResult({required this.query, required SearchResponse response})
      : hasMore = response.hasNext,
        data = response.contents.map(SearchContent.new).toList();
}

class SearchContent with DisplayTitle {
  final String id;
  @override
  final String title;
  @override
  final String originalTitle;
  final String description;
  final String imageUrl;
  final String releaseDate;

  SearchContent(SearchResponseItem item)
      : id = item.id,
        title = item.title,
        originalTitle = item.originalTitle,
        description = item.description,
        imageUrl = item.imageUrl,
        releaseDate = item.releaseDate;
}

mixin SearchResultMessage<T> {
  String get query;
  bool get hasMore;
  List<T> get data;

  String? get searchResultMessage {
    return switch (this) {
      _ when data.isEmpty => '"$query"에 대한 검색 결과가 없습니다.',
      _ when hasMore => '💡 이외 더 많은 검색 결과가 있습니다. 더 자세한 검색어를 사용해 보세요!',
      _ => null,
    };
  }
}

mixin DisplayTitle {
  String get title;
  String get originalTitle;

  String get displayTitle {
    return switch (this) {
      _ when title == originalTitle => title,
      _ => '$title ($originalTitle)',
    };
  }
}
