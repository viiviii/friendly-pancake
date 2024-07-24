import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' show api;
import 'package:pancake_app/content/bookmark/bookmark_screen.dart';
import 'package:pancake_app/content/search/content_search_screen.dart';
import 'package:pancake_app/content/search/view_models/content_search_view_model.dart';
import 'package:pancake_app/widgets/overflow_navigation_bar.dart';

class ContentScreen extends StatefulWidget {
  const ContentScreen({super.key});

  @override
  State<ContentScreen> createState() => _ContentScreenState();
}

class _ContentScreenState extends State<ContentScreen> {
  late final List<_Menu> _menu = [
    _Menu(
      ContentSearchScreen(onSelected: _onSearchContentSelected),
      icon: const Icon(Icons.search),
      label: const Text('검색'),
    ),
    const _Menu(
      BookmarkScreen(),
      icon: Icon(Icons.bookmark),
      label: Text('북마크'),
    ),
  ];

  int _currentIndex = 0;

  Future<void> _onMenuSelected(int index) async {
    setState(() => _currentIndex = index);
    _clearSavedMessages();
  }

  Future<void> _onSearchContentSelected(SearchContent searchContent) async {
    await _addToBookmark(searchContent);
    _alertSavedMessage();
  }

  void _alertSavedMessage() {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('저장 되었습니다.')),
    );
  }

  void _clearSavedMessages() {
    ScaffoldMessenger.of(context).clearSnackBars();
  }

  Future<void> _addToBookmark(SearchContent searchContent) async {
    await api.post('bookmarks', body: {
      'contentSource': 'TMDB', // TODO: 하드코딩
      'contentId': searchContent.id,
      'contentType': 'movie', // TODO: 하드코딩, tv도 생길 예정임
      'title': searchContent.title,
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('컨텐츠'),
      ),
      body: Padding(
        padding: const EdgeInsets.only(top: 15),
        child: OverflowNavigationBar(
          onDestinationSelected: _onMenuSelected,
          destinations: _menu,
          selectedIndex: _currentIndex,
          body: Padding(
            padding: const EdgeInsets.all(30),
            child: _menu[_currentIndex].screen,
          ),
        ),
      ),
    );
  }
}

class _Menu extends NavigationRailDestination {
  const _Menu(this.screen, {required super.icon, required super.label});

  final Widget screen;
}
