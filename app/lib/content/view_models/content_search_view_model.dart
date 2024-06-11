import 'package:pancake_app/api/api.dart' as api;
import 'package:pancake_app/content/models/content_search_model.dart';

class SearchViewModel {
  SearchResult? _current = const SearchResult.empty();

  Future<SearchResult>? searchBy(String query) {
    if (query.isEmpty || query == _current?.query) {
      return null;
    }

    return _searchBy(query);
  }

  Future<SearchResult> _searchBy(String query) async {
    final response = await _fetch(query);
    final result = SearchResult(query: query, response: response);
    _current = result;

    return result;
  }

  Future<SearchResponse> _fetch(String query) async {
    final response = await api.get('search/contents?query=$query');

    return SearchResponse(response.body!);
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
  @override
  final String title;
  @override
  final String originalTitle;
  final String description;
  final String imageUrl;
  final String releaseDate;

  SearchContent(SearchResponseItem item)
      : title = item.title,
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
    if (data.isEmpty) {
      return '"$query"에 대한 검색 결과가 없습니다.';
    }
    if (hasMore) {
      return '💡 이외 더 많은 검색 결과가 있습니다. 더 자세한 검색어를 사용해 보세요!';
    }

    return null;
  }
}

mixin DisplayTitle {
  String get title;
  String get originalTitle;

  String get displayTitle {
    if (title == originalTitle) {
      return title;
    }

    return '$title ($originalTitle)';
  }
}
