class ContentMetadata {
  final int id;
  final String title;
  final String description;
  final String imageUrl;

  const ContentMetadata({
    required this.id,
    required this.title,
    required this.description,
    required this.imageUrl,
  });

  factory ContentMetadata.fromJson(Map<String, dynamic> json) {
    return ContentMetadata(
      id: json['id'] as int,
      title: json['title'] as String,
      description: json['description'] as String,
      imageUrl: json['imageUrl'] as String,
    );
  }

  @override
  String toString() {
    return 'ContentMetadata{id: $id, title: $title, description: $description, imageUrl: $imageUrl}';
  }
}

class ContentStreaming {
  final String platform;
  final String url;

  const ContentStreaming({
    required this.platform,
    required this.url,
  });

  factory ContentStreaming.fromJson(Map<String, dynamic> json) {
    return ContentStreaming(
      platform: json['platform'] as String,
      url: json['url'] as String,
    );
  }

  @override
  String toString() {
    return 'ContentStreaming{platform: $platform, url: $url}';
  }
}
