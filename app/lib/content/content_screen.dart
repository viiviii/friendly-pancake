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
      label: const Text('검색'),
    ),
    const Destination(
      screen: BookmarkScreen(),
      icon: Icon(Icons.bookmark),
      label: Text('북마크'),
    ),
  ];

  int _selectedIndex = 0;

  Future<void> _onMenuSelected(int index) async {
    setState(() => _selectedIndex = index);
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
    return Scaffold(
      appBar: AppBar(
        title: const Text('컨텐츠'),
      ),
      body: Row(
        children: [
          _RailNavigation(
            onDestinationSelected: _onMenuSelected,
            destinations: _menu
                .map((e) =>
                    NavigationRailDestination(icon: e.icon, label: e.label))
                .toList(),
            selectedIndex: _selectedIndex,
          ),
          Expanded(
            child: _menu[_selectedIndex].screen,
          ),
        ],
      ),
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
      minExtendedWidth: 100,
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
  final Widget label;
}
