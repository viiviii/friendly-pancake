import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' show api;
import 'package:pancake_app/widgets/my_future_builder.dart';

class BookmarkScreen extends StatefulWidget {
  const BookmarkScreen({super.key});

  @override
  State<BookmarkScreen> createState() => _BookmarkScreenState();
}

class _BookmarkScreenState extends State<BookmarkScreen> {
  late Future<List<Bookmark>> _bookmarks;

  @override
  void initState() {
    super.initState();
    _bookmarks = _getBookmarks();
  }

  Future<List<Bookmark>> _getBookmarks() async {
    final response = await api.get<List<Map<String, dynamic>>>('bookmarks');

    return response.body!.map(Bookmark.new).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(vertical: 20, horizontal: 200),
        child: MyFutureBuilder<List<Bookmark>>(
          future: _bookmarks,
          builder: (_, data) {
            return ListView.builder(
              itemCount: data.length,
              itemBuilder: (_, i) {
                final item = data[i];
                return ListTile(
                  title: Text(item.recordTitle),
                  trailing: Text(item.contentType),
                );
              },
            );
          },
        ),
      ),
    );
  }
}

class Bookmark {
  final int id;
  final String contentId;
  final String contentType;
  final String recordTitle;

  Bookmark(dynamic json)
      : id = json['id'] as int,
        contentId = json['contentId'] as String,
        contentType = json['contentType'] as String,
        recordTitle = json['recordTitle'] as String;
}
