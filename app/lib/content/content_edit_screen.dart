import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' as api;
import 'package:pancake_app/widgets/hover_slide_image_card.dart';

import 'model.dart';

typedef ContentSelected<Content> = void Function(Content);

class ContentEditScreen extends StatelessWidget {
  const ContentEditScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Padding(
        padding: EdgeInsets.symmetric(vertical: 20, horizontal: 60),
        child: _ContentListViewSection(),
      ),
    );
  }
}

class _ContentListViewSection extends StatefulWidget {
  const _ContentListViewSection({Key? key}) : super(key: key);

  @override
  State<_ContentListViewSection> createState() =>
      _ContentListViewSectionState();
}

class _ContentListViewSectionState extends State<_ContentListViewSection> {
  late Future<List<ContentMetadata>> _future;

  @override
  void initState() {
    super.initState();
    _future = _loadContents();
  }

  Future<List<ContentMetadata>> _loadContents() async {
    final response = await api.get<List<Map<String, dynamic>>>('contents');
    return response.map(ContentMetadata.fromJson).toList();
  }

  Future<void> _showEditContentSection(ContentMetadata content) async {
    await showDialog<void>(
      context: context,
      builder: (_) => _ContentEditSection(content: content),
    );
  }

  @override
  Widget build(_) {
    return _FutureBuilder<List<ContentMetadata>>(
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
            final content = contents[index];
            return Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                HoverSlideImageCard(
                  onTap: () => _showEditContentSection(content),
                  image: NetworkImage(content.imageUrl),
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
          },
        );
      },
    );
  }
}

class _ContentEditSection extends StatefulWidget {
  const _ContentEditSection({required this.content});

  final ContentMetadata content;

  @override
  State<_ContentEditSection> createState() => _ContentEditSectionState();
}

class _ContentEditSectionState extends State<_ContentEditSection> {
  final FocusNode _focusNode = FocusNode();
  final TextEditingController _controller = TextEditingController();

  late Future<List<ContentStreaming>> _future;
  late String _previewImageUrl;

  bool _isPreviewError = false;

  String get _editImageUrl => _controller.value.text;
  bool get _isImageUpdated => _previewImageUrl != widget.content.imageUrl;

  @override
  void initState() {
    super.initState();
    _controller.text = widget.content.imageUrl;
    _previewImageUrl = widget.content.imageUrl;
    _future = _loadContentStreamingList();
  }

  @override
  void dispose() {
    _focusNode.dispose();
    _controller.dispose();
    super.dispose();
  }

  Future<List<ContentStreaming>> _loadContentStreamingList() async {
    final response = await api.get<List<Map<String, dynamic>>>(
      'contents/${widget.content.id}/playbacks',
    );
    return response.map(ContentStreaming.fromJson).toList();
  }

  Future<void> _savePreviewImageUrl() async {
    if (_isPreviewError) {
      _showSnackBar('저장 실패: 이미지가 올바르지 않음');
      return;
    }

    await _update(_previewImageUrl);
    _showSnackBar('저장 완료');
    _closeEditContentSection();
  }

  Future<void> _update(String imageUrl) async {
    await api.patch(
      'contents/${widget.content.id}/image',
      body: imageUrl,
    );
  }

  void _showSnackBar(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message)),
    );
  }

  void _closeEditContentSection() {
    Navigator.pop(context);
  }

  void _previewWithEditImage() {
    if (_previewImageUrl == _editImageUrl) {
      return;
    }

    setState(() {
      _previewImageUrl = _editImageUrl;
      _isPreviewError = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    final titleStyle = Theme.of(context).textTheme.titleMedium;

    return Scaffold(
      body: SimpleDialog(
        title: Text(widget.content.title),
        children: [
          SimpleDialogOption(
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _ImagePreview(
                  imageUrl: _previewImageUrl,
                  errorBuilder: (_, error, __) {
                    _isPreviewError = true;
                    return _ErrorMessageSection(message: '$error');
                  },
                ),
                const SizedBox(width: 20),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    mainAxisSize: MainAxisSize.max,
                    children: [
                      Text('이미지 주소', style: titleStyle),
                      Row(
                        children: [
                          Flexible(
                            child: TextField(
                              focusNode: _focusNode,
                              controller: _controller,
                              decoration: InputDecoration(
                                suffix: IconButton(
                                  icon: const Icon(Icons.clear),
                                  onPressed: _controller.clear,
                                ),
                                hintText: 'https://image.tmdb.org/...jpg',
                                filled: true,
                              ),
                              onSubmitted: (_) => _previewWithEditImage,
                              onTapOutside: (_) {
                                _focusNode.unfocus();
                                _previewWithEditImage();
                              },
                            ),
                          ),
                          TextButton(
                            onPressed:
                                _isImageUpdated ? _savePreviewImageUrl : null,
                            child: const Text('저장'),
                          ),
                        ],
                      ),
                      const SizedBox(height: 70),
                      Text('볼 수 있는 곳', style: titleStyle),
                      _FutureBuilder<List<ContentStreaming>>(
                        future: _future,
                        builder: (_, streamingList) {
                          return _ContentStreamingListView(streamingList);
                        },
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 15),
          TextButton(
            onPressed: _closeEditContentSection,
            child: const Text('닫기'),
          ),
        ],
      ),
    );
  }
}

class _ContentStreamingListView extends StatelessWidget {
  const _ContentStreamingListView(this.streamingList);

  final List<ContentStreaming> streamingList;

  @override
  Widget build(BuildContext context) {
    final textStyle = Theme.of(context).textTheme.bodyMedium;

    List<Widget> availableOn = List.unmodifiable(
      [Text('등록된 정보 없음', style: textStyle)],
    );

    if (streamingList.isNotEmpty) {
      availableOn = [
        for (final e in streamingList) Text(e.url, style: textStyle)
      ];
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: availableOn,
    );
  }
}

class _ImagePreview extends StatelessWidget {
  const _ImagePreview({
    required this.imageUrl,
    required this.errorBuilder,
  });

  final String imageUrl;
  final ImageErrorWidgetBuilder errorBuilder;

  @override
  Widget build(BuildContext context) {
    return Image.network(
      imageUrl,
      width: 300,
      fit: BoxFit.cover,
      errorBuilder: errorBuilder,
    );
  }
}

typedef _WidgetBuilder<T> = Widget Function(BuildContext context, T data);

class _FutureBuilder<T> extends StatelessWidget {
  const _FutureBuilder({required this.future, required this.builder});

  final Future<T> future;
  final _WidgetBuilder<T> builder;

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<T>(
      future: future,
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return builder(context, snapshot.data as T);
        } else if (snapshot.hasError) {
          return _ErrorMessageSection(message: '${snapshot.error}');
        } else {
          return const _ProgressIndicatorSection();
        }
      },
    );
  }
}

class _ErrorMessageSection<T> extends StatelessWidget {
  const _ErrorMessageSection({required this.message});

  final String message;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(
            Icons.error_outline,
            color: Colors.red,
            size: 60,
          ),
          Padding(
            padding: const EdgeInsets.only(top: 16),
            child: Text(
              message,
              maxLines: 5,
              softWrap: true,
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }
}

class _ProgressIndicatorSection extends StatelessWidget {
  const _ProgressIndicatorSection();

  @override
  Widget build(BuildContext context) {
    return const Center(
      child: SizedBox(
        width: 60,
        height: 60,
        child: CircularProgressIndicator(),
      ),
    );
  }
}
