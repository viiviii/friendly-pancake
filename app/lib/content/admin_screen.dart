import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' as api;
import 'package:pancake_app/content/content_list_view_section.dart';
import 'package:pancake_app/widgets/my_future_builder.dart';
import 'package:pancake_app/widgets/my_simple_dialog.dart';

import 'content_edit_section.dart';
import 'content_save_section.dart';
import 'model.dart';

class AdminScreen extends StatefulWidget {
  const AdminScreen({Key? key}) : super(key: key);

  @override
  State<AdminScreen> createState() => _AdminScreenState();
}

class _AdminScreenState extends State<AdminScreen> {
  late Future<List<ContentMetadata>> _contents;

  @override
  void initState() {
    super.initState();
    _contents = _getContents();
  }

  Future<void> _onAdd() async {
    await showDialog<void>(
      context: context,
      builder: (_) => const MySimpleDialog(
        title: Text('컨텐츠 추가'),
        child: ContentSaveSection(),
      ),
    );
    setState(() {
      _contents = _getContents();
    });
  }

  Future<void> _onSelected(ContentMetadata content) async {
    await showDialog<void>(
      context: context,
      builder: (_) => MySimpleDialog(
        title: SelectableText(content.title),
        child: ContentEditSection(content: content),
      ),
    );
    setState(() {
      _contents = _getContents();
    });
  }

  Future<List<ContentMetadata>> _getContents() async {
    final response = await api.get<List<Map<String, dynamic>>>('contents');
    return response.body!.map(ContentMetadata.fromJson).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('컨텐츠 관리'),
        actions: [
          Padding(
            padding: const EdgeInsets.only(right: 20),
            child: ElevatedButton(
              onPressed: _onAdd,
              child: const Text('추가'),
            ),
          ),
        ],
      ),
      body: MyFutureBuilder<List<ContentMetadata>>(
        future: _contents,
        builder: (_, data) {
          return ContentListViewSection(
            contents: data,
            onTap: (content) => _onSelected(content),
          );
        },
      ),
    );
  }
}
