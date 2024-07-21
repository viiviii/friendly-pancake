import 'package:flutter/material.dart';
import 'package:pancake_app/content/bookmark/bookmark_screen.dart';
import 'package:pancake_app/content/search/content_search_screen.dart';

class ContentScreen extends StatefulWidget {
  const ContentScreen({super.key});

  static const List<Destination> menu = [
    Destination(
      screen: ContentSearchScreen(),
      icon: Icon(Icons.search),
      label: Text('검색'),
    ),
    Destination(
      screen: BookmarkScreen(),
      icon: Icon(Icons.bookmark),
      label: Text('북마크'),
    ),
  ];

  @override
  State<ContentScreen> createState() => _ContentScreenState();
}

class _ContentScreenState extends State<ContentScreen> {
  int _selectedIndex = 0;

  Future<void> _onMenuSelected(int index) async {
    setState(() => _selectedIndex = index);
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
            destinations: ContentScreen.menu
                .map((e) =>
                    NavigationRailDestination(icon: e.icon, label: e.label))
                .toList(),
            selectedIndex: _selectedIndex,
          ),
          Expanded(
            child: ContentScreen.menu[_selectedIndex].screen,
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
