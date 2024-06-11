import 'package:flutter/material.dart';

import 'view_models/content_search_view_model.dart';
import 'views/content_search_bar_section.dart';
import 'views/content_search_result_section.dart';

class ContentScreen extends StatefulWidget {
  const ContentScreen({super.key});

  @override
  State<ContentScreen> createState() => _ContentScreenState();
}

class _ContentScreenState extends State<ContentScreen> {
  final SearchViewModel _viewModel = SearchViewModel();
  Future<SearchResult>? _searchResult;

  void onSearched(String query) {
    setState(() {
      _searchResult = _viewModel.searchBy(query);
    });
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
            if (_searchResult != null)
              ContentSearchResultSection(search: _searchResult!),
          ],
        ),
      ),
    );
  }
}
