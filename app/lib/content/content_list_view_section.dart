import 'package:flutter/material.dart';
import 'package:pancake_app/widgets/hover_slide_image_card.dart';

import 'model.dart';

typedef ContentSelected<ContentMetadata> = void Function(
    ContentMetadata content);

class ContentListViewSection extends StatelessWidget {
  const ContentListViewSection({
    super.key,
    required this.contents,
    required this.onTap,
  });
  final List<ContentMetadata> contents;
  final ContentSelected onTap;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(vertical: 20, horizontal: 60),
        child: GridView.builder(
          itemCount: contents.length,
          gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
            maxCrossAxisExtent: 200,
            mainAxisSpacing: 15,
            crossAxisSpacing: 15,
            childAspectRatio: 0.56,
          ),
          itemBuilder: (context, index) {
            return _ContentCard(
              onTap: onTap,
              content: contents[index],
            );
          },
        ),
      ),
    );
  }
}

class _ContentCard extends StatelessWidget {
  const _ContentCard({required this.onTap, required this.content});

  final ContentSelected onTap;
  final ContentMetadata content;

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        HoverSlideImageCard(
          onTap: () => onTap(content),
          image: NetworkImage(content.imageUrl), // TODO: widget or image
        ),
        Flexible(
          child: Text(
            content.title,
            style: Theme.of(context).textTheme.titleSmall,
            overflow: TextOverflow.ellipsis,
            maxLines: 2,
          ),
        ),
      ],
    );
  }
}
