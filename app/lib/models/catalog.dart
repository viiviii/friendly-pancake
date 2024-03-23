import 'package:pancake_app/models/content.dart';

class Catalog {
  final String title;
  final List<Content> contents;

  const Catalog({
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

  List<Content> get watchedContents {
    // todo: 서버에서 주기
    return contents.where((e) => e.watched).take(5).toList();
  }

  List<Content> get unwatchedContents {
    return contents.where((e) => !e.watched).toList();
  }

  @override
  String toString() {
    return 'Catalog{title: $title, contents: $contents}';
  }
}
