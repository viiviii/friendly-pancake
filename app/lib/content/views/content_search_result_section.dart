import 'package:flutter/material.dart';

import '../view_models/content_search_view_model.dart';

typedef ContentSelected<SearchContent> = void Function(SearchContent content);

class ContentSearchResultSection extends StatelessWidget {
  const ContentSearchResultSection({
    super.key,
    required this.onItemAdded,
    required this.result,
  });

  final ContentSelected<SearchContent> onItemAdded;
  final SearchResult result;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        ListView.builder(
          shrinkWrap: true,
          itemCount: result.data.length,
          itemBuilder: (_, i) {
            return _SearchItemView(
              onAdded: onItemAdded,
              content: result.data[i],
            );
          },
        ),
        if (result.searchResultMessage != null)
          _SearchResultMessage(result.searchResultMessage!)
      ],
    );
  }
}

class _SearchItemView extends StatelessWidget {
  const _SearchItemView({
    required this.onAdded,
    required this.content,
  });

  final ContentSelected<SearchContent> onAdded;
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
            onPressed: () => onAdded(content),
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
