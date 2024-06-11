class SearchResponse {
  final bool hasNext;
  final List<SearchResponseItem> contents;

  SearchResponse(dynamic json)
      : hasNext = json['hasNext'] as bool,
        contents = json['contents']
            .map<SearchResponseItem>(SearchResponseItem.new)
            .toList();

  @override
  String toString() {
    return 'SearchResponse{hasNext: $hasNext, contents: $contents}';
  }
}

class SearchResponseItem {
  final String title;
  final String originalTitle;
  final String description;
  final String imageUrl;
  final String releaseDate;

  SearchResponseItem(dynamic json)
      : title = json['title'] as String,
        originalTitle = json['originalTitle'] as String,
        description = json['description'] as String,
        imageUrl = json['imageUrl'] as String,
        releaseDate = json['releaseDate'] ?? '';

  @override
  String toString() {
    return 'SearchResponseItem{title: $title, originalTitle: $originalTitle, '
        'description: $description, imageUrl: $imageUrl, releaseDate: $releaseDate}';
  }
}
