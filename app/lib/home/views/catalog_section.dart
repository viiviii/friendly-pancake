import 'package:flutter/material.dart';
import 'package:pancake_app/home/models/catalog.dart';
import 'package:pancake_app/widgets/hover_slide_image_card.dart';

import '../models/content.dart';

typedef ContentSelected<Content> = void Function(Content selected);

class CatalogSection extends StatelessWidget {
  const CatalogSection(
      {super.key, required this.onSelected, required this.catalog});

  final ContentSelected<Content> onSelected;
  final Catalog catalog;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        const _WatchlistTitle('최근에 봤어요'), // TODO: 하드코딩 제거
        _Watchlist(
          onSelected: onSelected,
          contents: catalog.watchedContents,
        ),
        _WatchlistTitle(catalog.title),
        _Watchlist(
          onSelected: onSelected,
          contents: catalog.unwatchedContents,
        ),
      ],
    );
  }
}

class _WatchlistTitle extends StatelessWidget {
  const _WatchlistTitle(this.title);

  final String title;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: Text(
        title,
        style: Theme.of(context).textTheme.headlineSmall,
      ),
    );
  }
}

class _Watchlist extends StatelessWidget {
  const _Watchlist({required this.onSelected, required this.contents});

  final ContentSelected<Content> onSelected;
  final List<Content> contents;

  @override
  Widget build(BuildContext context) {
    return GridView.builder(
      itemCount: contents.length,
      physics: const NeverScrollableScrollPhysics(),
      shrinkWrap: true,
      gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
        maxCrossAxisExtent: 300,
        crossAxisSpacing: 15,
        childAspectRatio: 0.56,
      ),
      itemBuilder: (_, index) {
        return _ContentCard(
          contents[index],
          onSelected: onSelected,
        );
      },
    );
  }
}

class _ContentCard extends StatelessWidget {
  const _ContentCard(this.content, {required this.onSelected});

  final ContentSelected<Content> onSelected;
  final Content content;

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        HoverSlideImageCard(
          image: NetworkImage(content.imageUrl),
          onTap: () => onSelected(content),
          overlayWidget: Padding(
            padding: const EdgeInsets.all(15.0),
            child: _WatchShortcutText(content.shortcutOption),
          ),
          errorBuilder: (_, __, ___) {
            return const Center(child: Text('이미지를 로드할 수 없음'));
          },
        ),
        _ContentTitle(content.title),
      ],
    );
  }
}

class _ContentTitle extends StatelessWidget {
  const _ContentTitle(this.title);

  final String title;

  @override
  Widget build(BuildContext context) {
    return Text(
      title,
      style: Theme.of(context).textTheme.titleSmall,
      maxLines: 2,
    );
  }
}

class _WatchShortcutText extends StatelessWidget {
  const _WatchShortcutText(this.option);

  final Option option;

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;
    return IgnorePointer(
      child: Column(
        children: [
          Text(
            '${option.platformLabel}에서',
            style: textTheme.bodyLarge,
          ),
          Text(
            '지금 볼래요',
            style: textTheme.titleLarge,
          ),
        ],
      ),
    );
  }
}
