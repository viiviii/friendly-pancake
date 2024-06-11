import 'package:flutter/material.dart';
import 'package:pancake_app/widgets/my_future_builder.dart';

import '../view_models/content_search_view_model.dart';

class ContentSearchResultSection extends StatelessWidget {
  const ContentSearchResultSection({super.key, required this.search});

  final Future<SearchResult> search;

  @override
  Widget build(BuildContext context) {
    return MyFutureBuilder<SearchResult>(
      future: search,
      builder: (_, data) {
        return Column(
          children: [
            _SearchResultListView(data),
            if (data.searchResultMessage != null)
              _SearchResultMessage(data.searchResultMessage!)
          ],
        );
      },
    );
  }
}

class _SearchResultListView extends StatelessWidget {
  const _SearchResultListView(this.search);

  final SearchResult search;

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      shrinkWrap: true,
      itemCount: search.data.length,
      itemBuilder: (_, i) {
        return _SearchItemView(content: search.data[i]);
      },
    );
  }
}

class _SearchItemView extends StatelessWidget {
  const _SearchItemView({required this.content});

  final SearchContent content;

  @override
  Widget build(BuildContext context) {
    const dividerPadding = SizedBox(width: 16);
    return Padding(
      padding: const EdgeInsets.only(bottom: 12.0),
      child: Row(
        children: [
          Image.network(
            content.imageUrl,
            height: 170,
          ),
          dividerPadding,
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  content.displayTitle,
                  style: Theme.of(context).textTheme.titleMedium,
                ),
                Text(
                  content.releaseDate,
                  style: Theme.of(context).textTheme.labelLarge,
                ),
                const SizedBox(height: 18),
                Text(
                  content.description,
                  style: Theme.of(context).textTheme.bodyMedium,
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                ),
              ],
            ),
          ),
          dividerPadding,
          IconButton(
            onPressed: () {}, // TODO: 컨텐츠 등록하기 기능 연결
            icon: const Icon(Icons.add),
          ),
          dividerPadding,
        ],
      ),
    );
  }
}

class _SearchResultMessage extends StatelessWidget {
  const _SearchResultMessage(this.message);

  final String message;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 30),
      child: Card.outlined(
        child: ListTile(
          title: Center(child: Text(message)),
        ),
      ),
    );
  }
}
