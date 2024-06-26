import 'package:flutter/material.dart';
import 'package:pancake_app/admin/admin_screen.dart';
import 'package:pancake_app/api/api.dart' show api;
import 'package:pancake_app/content/content_screen.dart';
import 'package:pancake_app/home/models/catalog.dart';
import 'package:pancake_app/setting/setting_screen.dart';
import 'package:url_launcher/url_launcher.dart';

import 'models/content.dart';
import 'views/catalog_section.dart';

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
      catalog = Catalog.fromJson(response.body!);
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

  Future<void> _goToContent() async {
    await Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const ContentScreen()),
    );

    _onLoad();
  }

  Future<void> _goToAdmin() async {
    await Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const AdminScreen()),
    );

    _onLoad();
  }

  Future<void> _goToSetting() async {
    await Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const SettingScreen()),
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
              onPressed: _goToContent,
              child: const Text('컨텐츠'),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(right: 20),
            child: ElevatedButton(
              onPressed: _goToSetting,
              child: const Text('설정'),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(right: 20),
            child: ElevatedButton(
              onPressed: _goToAdmin,
              child: const Text('어드민'),
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
