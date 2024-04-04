import 'package:flutter/material.dart';
import 'package:pancake_app/content/content_list_view_screen.dart';
import 'package:pancake_app/widgets/my_simple_dialog.dart';

import 'content_edit_screen.dart';
import 'content_save_screen.dart';

class AdminScreen extends StatelessWidget {
  const AdminScreen({Key? key}) : super(key: key);

  Future<void> _goToSaveContent(BuildContext context) async {
    await showDialog<void>(
      context: context,
      builder: (_) => const MySimpleDialog(
        title: Text('컨텐츠 추가'),
        child: ContentSaveScreen(),
      ),
    );
  }

  Future<void> _goToContentListView(BuildContext context) async {
    await Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => ContentListViewScreen(
          onTap: (content) => showDialog<void>(
            context: context,
            builder: (_) => MySimpleDialog(
              title: Text(content.title),
              child: ContentEditScreen(content: content),
            ),
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 60),
        child: Center(
          child: GridView.count(
            crossAxisCount: 3,
            crossAxisSpacing: 15,
            mainAxisSpacing: 15,
            shrinkWrap: true,
            children: [
              _MenuCard(
                onTap: () => _goToSaveContent(context),
                title: '컨텐츠 추가',
              ),
              _MenuCard(
                onTap: () => _goToContentListView(context),
                title: '컨텐츠 수정',
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _MenuCard extends StatelessWidget {
  final GestureTapCallback onTap;
  final String title;

  const _MenuCard({required this.onTap, required this.title});

  @override
  Widget build(BuildContext context) {
    final textStyle = Theme.of(context).textTheme.headlineLarge;
    return Card(
      child: InkWell(
        onTap: onTap,
        child: Center(
          child: Text(title, style: textStyle),
        ),
      ),
    );
  }
}
