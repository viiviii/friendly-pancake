import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' show api;
import 'package:pancake_app/widgets/my_future_builder.dart';

import 'view_models/content_search_view_model.dart';
import 'views/content_search_bar_section.dart';
import 'views/content_search_result_section.dart';

class ContentSearchScreen extends StatefulWidget {
  const ContentSearchScreen({super.key, required this.onSelected});

  final ValueSetter<SearchContent> onSelected;

  @override
  State<ContentSearchScreen> createState() => _ContentSearchScreenState();
}

class _ContentSearchScreenState extends State<ContentSearchScreen> {
  final ContentSearchViewModel _viewModel = ContentSearchViewModel(api);
  Future<ContentSearchResult>? _searchResult;

  void _onSearched(String query) {
    setState(() {
      _searchResult = _viewModel.searchBy(query);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 80.0),
        child: ListView(
          children: [
            ContentSearchBarSection(onSubmitted: _onSearched),
            const SizedBox(height: 30),
            MyFutureBuilder<ContentSearchResult>(
              future: _searchResult,
              builder: (_, data) {
                return ContentSearchResultSection(
                  onItemAdded: widget.onSelected,
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
