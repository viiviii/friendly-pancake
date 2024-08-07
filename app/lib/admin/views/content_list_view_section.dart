import 'package:flutter/material.dart';
import 'package:pancake_app/widgets/hover_slide_image_card.dart';

import '../models/metadata.dart';

class ContentListViewSection extends StatelessWidget {
  const ContentListViewSection({
    super.key,
    required this.contents,
    required this.onTap,
  });
  final List<ContentMetadata> contents;
  final ValueSetter<ContentMetadata> onTap;

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
            // TODO: childAspectRatio - 원래는 이미지 비율과 같아야하지만 title 때문에 임의의 상수 계산해서 넣음
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

  final ValueSetter<ContentMetadata> onTap;
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
          errorBuilder: (_, __, ___) {
            return const ColoredBox(
              color: Colors.redAccent,
              child: Icon(
                Icons.error_outline,
                color: Colors.white,
                size: 80,
              ),
            );
          },
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
