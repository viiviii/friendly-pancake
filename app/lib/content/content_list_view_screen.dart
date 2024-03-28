import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' as api;
import 'package:pancake_app/widgets/hover_slide_image_card.dart';
import 'package:pancake_app/widgets/my_future_builder.dart';

import 'model.dart';

typedef ContentSelected<ContentMetadata> = void Function(
    ContentMetadata content);

class ContentListViewScreen extends StatefulWidget {
  const ContentListViewScreen({super.key, required this.onTap});

  final ContentSelected onTap;

  @override
  State<ContentListViewScreen> createState() => _ContentListViewScreenState();
}

class _ContentListViewScreenState extends State<ContentListViewScreen> {
  late Future<List<ContentMetadata>> _future;

  @override
  void initState() {
    super.initState();
    _future = _loadContents();
  }

  Future<List<ContentMetadata>> _loadContents() async {
    final response = await api.get<List<Map<String, dynamic>>>('contents');
    return response.body!.map(ContentMetadata.fromJson).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(vertical: 20, horizontal: 60),
        child: MyFutureBuilder<List<ContentMetadata>>(
          future: _future,
          builder: (_, contents) {
            return GridView.builder(
              itemCount: contents.length,
              gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
                maxCrossAxisExtent: 200,
                mainAxisSpacing: 15,
                crossAxisSpacing: 15,
                childAspectRatio: 0.56,
              ),
              itemBuilder: (context, index) {
                return _ContentCard(
                  widget: widget,
                  content: contents[index],
                );
              },
            );
          },
        ),
      ),
    );
  }
}

class _ContentCard extends StatelessWidget {
  const _ContentCard({required this.widget, required this.content});

  final ContentListViewScreen widget;
  final ContentMetadata content;

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        HoverSlideImageCard(
          onTap: () => widget.onTap(content),
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
