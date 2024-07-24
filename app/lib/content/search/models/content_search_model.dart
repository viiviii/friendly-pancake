class ContentSearchResponse {
  final bool hasNext;
  final List<ContentSearchResponseItem> contents;

  ContentSearchResponse(dynamic json)
      : hasNext = json['hasNext'] as bool,
        contents = json['contents']
            .map<ContentSearchResponseItem>(ContentSearchResponseItem.new)
            .toList();

  @override
  String toString() {
    return 'ContentSearchResponse{hasNext: $hasNext, contents: $contents}';
  }
}

class ContentSearchResponseItem {
  final String id;
  final String title;
  final String originalTitle;
  final String description;
  final String imageUrl;
  final String releaseDate;

  ContentSearchResponseItem(dynamic json)
      : id = json['id'] as String,
        title = json['title'] as String,
        originalTitle = json['originalTitle'] as String,
        description = json['description'] as String,
        imageUrl = json['imageUrl'] as String,
        releaseDate = json['releaseDate'] ?? '';

  @override
  String toString() {
    return 'ContentSearchResponseItem{id: $id, title: $title, originalTitle: $originalTitle, '
        'description: $description, imageUrl: $imageUrl, releaseDate: $releaseDate}';
  }
}
