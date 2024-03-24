import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' as api;
import 'package:pancake_app/content_save_screen.dart';
import 'package:pancake_app/models/catalog.dart';
import 'package:pancake_app/models/content.dart';
import 'package:pancake_app/widgets/catalog_section.dart';
import 'package:url_launcher/url_launcher.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  Catalog catalog = const Catalog(title: '시청할 컨텐츠', contents: []);

  @override
  void initState() {
    super.initState();
    _onLoad();
  }

  Future<void> _onLoad() async {
    final response = await api.get<Map<String, dynamic>>('watches');

    setState(() {
      catalog = Catalog.fromJson(response);
    });
  }

  Future<void> _watch(Content content) async {
    final opened = await launchUrl(
      api.url('watch/${content.id}/${content.shortcutOption.platformName}'),
    );

    if (!opened) {
      return;
    }

    await api.patch('contents/${content.id}/watched');
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
        title: const Text('Flutter Demo Home Page'),
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
          child: CatalogSection(
            onSelected: _watch,
            catalog: catalog,
          ),
        ),
      ),
    );
  }
}
