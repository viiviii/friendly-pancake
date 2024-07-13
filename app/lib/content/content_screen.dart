import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' show api;

import '../widgets/my_future_builder.dart';
import 'view_models/content_search_view_model.dart';
import 'views/content_search_bar_section.dart';
import 'views/content_search_result_section.dart';

class ContentScreen extends StatefulWidget {
  const ContentScreen({super.key});

  @override
  State<ContentScreen> createState() => _ContentScreenState();
}

class _ContentScreenState extends State<ContentScreen> {
  final SearchViewModel _viewModel = SearchViewModel(api);
  Future<SearchResult>? _searchResult;

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
            MyFutureBuilder<SearchResult>(
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
