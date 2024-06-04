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
