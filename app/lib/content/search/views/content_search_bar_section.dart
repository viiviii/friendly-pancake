import 'package:flutter/material.dart';

class ContentSearchBarSection extends StatefulWidget {
  const ContentSearchBarSection({super.key, required this.onSubmitted});

  final ValueChanged<String> onSubmitted;

  @override
  State<ContentSearchBarSection> createState() =>
      _ContentSearchBarSectionState();
}

class _ContentSearchBarSectionState extends State<ContentSearchBarSection> {
  final TextEditingController _controller = TextEditingController();

  String get _query => _controller.value.text;

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SearchBar(
      controller: _controller,
      hintText: '원하는 영화를 검색하세요.',
      leading: IconButton(
        icon: const Icon(Icons.search),
        onPressed: () => widget.onSubmitted(_query),
      ),
      onSubmitted: widget.onSubmitted,
    );
  }
}
