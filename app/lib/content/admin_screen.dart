import 'package:flutter/material.dart';

import 'content_edit_screen.dart';

class AdminScreen extends StatelessWidget {
  const AdminScreen({Key? key}) : super(key: key);

  Future<void> _goToContentSave(BuildContext context) async {
    await Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const ContentSaveScreen()),
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
                onTap: () => _goToContentSave(context),
                title: '컨텐츠 추가',
              ),
              _MenuCard(onTap: () {}, title: '컨텐츠 수정'),
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
