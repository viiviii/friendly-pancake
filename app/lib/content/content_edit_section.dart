import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:pancake_app/api/api.dart' as api;
import 'package:pancake_app/widgets/my_future_builder.dart';
import 'package:pancake_app/widgets/my_simple_dialog.dart';
import 'package:url_launcher/url_launcher.dart';

import 'model.dart';

class ContentEditSection extends StatefulWidget {
  const ContentEditSection({super.key, required this.content});

  final ContentMetadata content;

  @override
  State<ContentEditSection> createState() => _ContentEditSectionState();
}

class _ContentEditSectionState extends State<ContentEditSection> {
  final TextEditingController _controller = TextEditingController();
  late Future<List<ContentStreaming>> _playbacks;

  bool _isPreviewError = false;

  set _previewImageUrl(String imageUrl) {
    _controller.text = imageUrl;
    _isPreviewError = false;
  }

  String get _previewImageUrl => _controller.value.text;

  bool get _imageChanged => _previewImageUrl != widget.content.imageUrl;

  @override
  void initState() {
    super.initState();
    _previewImageUrl = widget.content.imageUrl;
    _playbacks = _getPlaybacks();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  Future<void> _onPaste() async {
    final paste = await Clipboard.getData(Clipboard.kTextPlain);
    _updatePreviewImage(paste?.text ?? '');
  }

  Future<void> _onSave() async {
    if (_isPreviewError) {
      _showSnackBar('로드에 실패한 이미지는 저장할 수 없음');
      return;
    }
    await _updateImageUrl();
    _showSnackBar('저장 완료');
    _close();
  }

  Future<void> _onAdd() async {
    await showDialog<void>(
      context: context,
      builder: (_) {
        return MySimpleDialog(
          title: const Text('스트리밍 정보 추가'),
          child: _StreamingAddFormSection(
            onSaved: _addPlaybackUrl,
          ),
        );
      },
    );
  }

  void _updatePreviewImage(String updateImageUrl) {
    if (updateImageUrl.isEmpty) {
      return;
    }
    if (updateImageUrl == _previewImageUrl) {
      return;
    }
    setState(() => _previewImageUrl = updateImageUrl);
  }

  Future<void> _addPlaybackUrl(String? playbackUrl) async {
    if (playbackUrl == null) {
      return;
    }

    final result = await _savePlayback(playbackUrl);
    if (!result.success) {
      _showSnackBar('저장 실패');
      return;
    }

    _showSnackBar('저장 완료');
    _close();
    setState(() {
      _playbacks = _getPlaybacks();
    });
  }

  Future<List<ContentStreaming>> _getPlaybacks() async {
    final response = await api.get<List<Map<String, dynamic>>>(
      'contents/${widget.content.id}/playbacks',
    );
    return response.body!.map(ContentStreaming.fromJson).toList();
  }

  Future<void> _updateImageUrl() async {
    await api.patch(
      'contents/${widget.content.id}/image',
      body: _previewImageUrl,
    );
  }

  Future<api.ApiResult> _savePlayback(String playbackUrl) async {
    return await api.post(
      'contents/${widget.content.id}/playbacks',
      body: {
        'url': playbackUrl, // TODO
      },
    );
  }

  void _showSnackBar(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message)),
    );
  }

  void _close() {
    Navigator.pop(context);
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        _ImagePreview(
          imageUrl: _previewImageUrl,
          errorBuilder: (_, error, __) {
            _isPreviewError = true;
            return ErrorMessage(message: '$error');
          },
        ),
        const SizedBox(width: 20),
        Flexible(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _Title(
                titleName: '이미지',
                buttonName: '저장',
                onPressed: _imageChanged ? _onSave : null,
              ),
              _ImageUrlField(
                controller: _controller,
                onPaste: _onPaste,
              ),
              const SizedBox(height: 70),
              _Title(
                titleName: '볼 수 있는 곳',
                buttonName: '추가',
                onPressed: _onAdd,
              ),
              MyFutureBuilder(
                future: _playbacks,
                builder: (_, data) {
                  return _StreamingListView(data);
                },
              ), // TODO
            ],
          ),
        ),
      ],
    );
  }
}

class _ImageUrlField extends StatelessWidget {
  const _ImageUrlField({required this.controller, required this.onPaste});

  final TextEditingController controller;
  final VoidCallback onPaste;

  @override
  Widget build(BuildContext context) {
    return TextField(
      controller: controller,
      readOnly: true,
      decoration: InputDecoration(
        filled: true,
        hintText: 'https://image.tmdb.org/...jpg',
        suffixIcon: IconButton(
          icon: const Icon(Icons.paste),
          onPressed: onPaste,
        ),
      ),
    );
  }
}

class _StreamingListView extends StatelessWidget {
  const _StreamingListView(this.streamingList);

  final List<ContentStreaming> streamingList;

  @override
  Widget build(BuildContext context) {
    final availableOn = <Widget>[
      for (final e in streamingList)
        _ReadOnlyField(
          e.url,
          onPressed: () => launchUrl(Uri.parse(e.url)),
        ),
    ];
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: availableOn.isNotEmpty
          ? availableOn
          : [const _ReadOnlyField('등록된 정보 없음')],
    );
  }
}

class _StreamingAddFormSection extends StatelessWidget {
  _StreamingAddFormSection({required this.onSaved});

  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final FormFieldSetter<String> onSaved;

  void _onSave() {
    if (!_formKey.currentState!.validate()) {
      return;
    }
    _formKey.currentState?.save();
  }

  String? _validateUrl(String? value) {
    if (value == null || value.isEmpty) {
      return '재생 주소는 필수 값입니다.';
    }
    return null;
  }

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          TextFormField(
            decoration: const InputDecoration(
              filled: true,
              hintText: 'https://www.netflix.com/watch/1',
            ),
            validator: _validateUrl,
            onSaved: onSaved,
          ),
          const SizedBox(height: 30),
          TextButton(
            onPressed: _onSave,
            child: const Text('저장'),
          )
        ],
      ),
    );
  }
}

class _ImagePreview extends StatelessWidget {
  const _ImagePreview({required this.imageUrl, required this.errorBuilder});

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

class _Title extends StatelessWidget {
  const _Title({
    required this.titleName,
    required this.buttonName,
    this.onPressed,
  });

  final String titleName;
  final String buttonName;
  final VoidCallback? onPressed;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Text(
          titleName,
          style: Theme.of(context).textTheme.titleMedium,
        ),
        TextButton(
          onPressed: onPressed,
          child: Text(buttonName),
        ),
      ],
    );
  }
}

class _ReadOnlyField extends StatelessWidget {
  const _ReadOnlyField(this.value, {this.onPressed});

  final String value;
  final VoidCallback? onPressed;

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      initialValue: value,
      readOnly: true,
      enabled: onPressed != null,
      maxLines: 1,
      minLines: 1,
      decoration: InputDecoration(
        filled: true,
        suffix: IconButton(
          icon: const Icon(Icons.arrow_outward),
          onPressed: onPressed,
        ),
      ),
    );
  }
}
