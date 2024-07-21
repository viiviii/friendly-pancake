import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' show api;
import 'package:pancake_app/content/bookmark/bookmark_screen.dart';
import 'package:pancake_app/content/search/content_search_screen.dart';
import 'package:pancake_app/content/search/view_models/content_search_view_model.dart';

class ContentScreen extends StatefulWidget {
  const ContentScreen({super.key});

  @override
  State<ContentScreen> createState() => _ContentScreenState();
}

class _ContentScreenState extends State<ContentScreen> {
  late final List<Destination> _menu = [
    Destination(
      screen: ContentSearchScreen(onSelected: _addToBookmark),
      icon: const Icon(Icons.search),
      label: '검색',
    ),
    const Destination(
      screen: BookmarkScreen(),
      icon: Icon(Icons.bookmark),
      label: '북마크',
    ),
  ];

  int _currentIndex = 0;

  Future<void> _onMenuSelected(int index) async {
    setState(() => _currentIndex = index);
  }

  Future<void> _addToBookmark(SearchContent searchContent) async {
    await _postBookmark(searchContent);
    alertSavedMessage();
  }

  Future<void> _postBookmark(SearchContent searchContent) async {
    await api.post('bookmarks', body: {
      'contentSource': 'TMDB', // TODO: 하드코딩
      'contentId': searchContent.id,
      'contentType': 'movie', // TODO: 하드코딩, tv도 생길 예정임
      'title': searchContent.title,
    });
  }

  void alertSavedMessage() {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('저장 되었습니다.')),
    );
  }

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;
    print('screenWidth=$screenWidth');
    return Scaffold(
      appBar: AppBar(
        title: const Text('컨텐츠'),
      ),
      body: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          if (screenWidth > 700)
            _RailNavigation(
              onDestinationSelected: _onMenuSelected,
              destinations: _menu
                  .map((e) => NavigationRailDestination(
                      icon: e.icon, label: Text(e.label)))
                  .toList(),
              selectedIndex: _currentIndex,
            ),
          Flexible(
            child: Padding(
              padding: const EdgeInsets.symmetric(
                vertical: 30,
                horizontal: 100,
              ),
              child: _menu[_currentIndex].screen,
            ),
          ),
        ],
      ),
      bottomNavigationBar: screenWidth <= 700
          ? NavigationBar(
              // labelBehavior: labelBehavior,
              selectedIndex: _currentIndex,
              onDestinationSelected: _onMenuSelected,
              destinations: _menu
                  .map((e) =>
                      NavigationDestination(icon: e.icon, label: e.label))
                  .toList(),
            )
          : null,
    );
  }
}

class _RailNavigation extends StatelessWidget {
  const _RailNavigation({
    required this.onDestinationSelected,
    required this.destinations,
    required this.selectedIndex,
  });

  final ValueChanged<int> onDestinationSelected;
  final List<NavigationRailDestination> destinations;
  final int selectedIndex;

  @override
  Widget build(BuildContext context) {
    return NavigationRail(
      selectedIndex: selectedIndex,
      onDestinationSelected: onDestinationSelected,
      destinations: destinations,
      extended: true,
      minExtendedWidth: 150,
    );
  }
}

class Destination {
  const Destination({
    required this.screen,
    required this.icon,
    required this.label,
  });

  final Widget screen;
  final Widget icon;
  final String label;
}
