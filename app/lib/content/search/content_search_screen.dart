import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' show api;
import 'package:pancake_app/widgets/my_future_builder.dart';

import 'view_models/content_search_view_model.dart';
import 'views/content_search_bar_section.dart';
import 'views/content_search_result_section.dart';

class ContentSearchScreen extends StatefulWidget {
  const ContentSearchScreen({super.key});

  @override
  State<ContentSearchScreen> createState() => _ContentSearchScreenState();
}

class _ContentSearchScreenState extends State<ContentSearchScreen> {
  final ContentSearchViewModel _viewModel = ContentSearchViewModel(api);
  Future<ContentSearchResult>? _searchResult;

  void onSearched(String query) {
    setState(() {
      _searchResult = _viewModel.searchBy(query);
    });
  }

  void onAddToBookmark(SearchContent content) {
    _viewModel.addToBookmark(content);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('컨텐츠'),
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 80.0),
        child: ListView(
          children: [
            ContentSearchBarSection(onSubmitted: onSearched),
            const SizedBox(height: 30),
            MyFutureBuilder<ContentSearchResult>(
              future: _searchResult,
              builder: (_, data) {
                return ContentSearchResultSection(
                  onItemAdded: onAddToBookmark,
                  result: data,
                );
              },
            )
          ],
        ),
      ),
    );
  }
}
