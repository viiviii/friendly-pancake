import 'package:pancake_app/models/content.dart';

class Catalog {
  final String title;
  final List<Content> contents;

  Catalog({
    required this.title,
    required this.contents,
  });

  factory Catalog.fromJson(Map<String, dynamic> json) {
    return Catalog(
      title: json['title'] as String,
      contents:
          json['contents'].map<Content>((e) => Content.fromJson(e)).toList(),
    );
  }

  @override
  String toString() {
    return 'Catalog{title: $title, contents: $contents}';
  }
}
