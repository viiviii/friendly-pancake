import 'dart:async';

import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' as api;
import 'package:pancake_app/content_save_screen.dart';
import 'package:url_launcher/url_launcher.dart';

import 'widgets/content.dart';

typedef ContentSelected<int> = void Function(int contentId);

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorSchemeSeed: const Color(0xffBB2649),
        useMaterial3: true,
        brightness: Brightness.dark,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  List<Content> _watchedContents = [];

  // TODO
  String _unwatchedContentsTitle = '';
  List<Content> _unwatchedContents = [];

  @override
  void initState() {
    super.initState();
    _onLoad();
  }

  Future<void> _onLoad() async {
    final response = await api.get<Map<String, dynamic>>('watches');

    final Catalog catalog = Catalog.fromJson(response);

    setState(() {
      // todo: 서버에서 페이징
      _watchedContents =
          catalog.contents.where((e) => e.watched).take(5).toList();
      _unwatchedContentsTitle = catalog.title;
      _unwatchedContents = catalog.contents.where((e) => !e.watched).toList();
    });
  }

  Future<void> _watch(int id) async {
    final opened = await launchUrl(api.url('contents/$id'));

    if (!opened) {
      return;
    }

    await api.patch('contents/$id/watched');
    _onLoad();
  }

  Future<void> _moveToContentSave() async {
    await Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const ContentSaveScreen()),
    );

    _onLoad();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        actions: [
          Padding(
            padding: const EdgeInsets.only(right: 20),
            child: ElevatedButton(
              onPressed: _moveToContentSave,
              child: const Text('등록'),
            ),
          ),
        ],
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.symmetric(vertical: 20, horizontal: 60),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Padding(
                padding: const EdgeInsets.only(bottom: 10),
                child: Text(
                  '최근에 봤어요',
                  style: Theme.of(context).textTheme.headlineSmall,
                ),
              ),
              _ContentGridView(
                onContentTap: _watch,
                contents: _watchedContents,
              ),
              Padding(
                padding: const EdgeInsets.only(bottom: 10),
                child: Text(
                  _unwatchedContentsTitle,
                  style: Theme.of(context).textTheme.headlineSmall,
                ),
              ),
              _ContentGridView(
                onContentTap: _watch,
                contents: _unwatchedContents,
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _ContentGridView extends StatelessWidget {
  const _ContentGridView({
    Key? key,
    required this.onContentTap,
    required this.contents,
  }) : super(key: key);

  final ContentSelected<int> onContentTap;
  final List<Content> contents;

  @override
  Widget build(BuildContext context) {
    return GridView.builder(
      itemCount: contents.length,
      physics: const NeverScrollableScrollPhysics(),
      shrinkWrap: true,
      gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
        maxCrossAxisExtent: 300,
        crossAxisSpacing: 15,
      ),
      itemBuilder: (_, index) {
        final content = contents[index];
        return Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            ContentCard(
              onTap: () => onContentTap(content.id),
              image: NetworkImage(content.imageUrl),
              description: content.description,
            ),
            Text(
              content.title,
              style: Theme.of(context).textTheme.titleSmall,
              maxLines: 2,
            ),
          ],
        );
      },
    );
  }
}

class Catalog {
  final String title;
  final List<Content> contents;

  Catalog._(this.title, this.contents);

  factory Catalog.fromJson(Map<String, dynamic> json) {
    return Catalog._(
        json['title'] as String,
        json['contents']
            .map<Content>((json) => Content.fromJson(json))
            .toList());
  }
}

class Content {
  final int id;
  final String title;
  final String description;
  final String imageUrl;
  final bool watched;

  Content._(this.id, this.title, this.description, this.imageUrl, this.watched);

  factory Content.fromJson(Map<String, dynamic> json) {
    return Content._(
      json['id'] as int,
      json['title'] as String,
      json['description'] as String,
      json['imageUrl'] as String,
      json['watched'] as bool,
    );
  }
}
