import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' show api;

class ContentSaveSection extends StatefulWidget {
  const ContentSaveSection({super.key});

  @override
  State<ContentSaveSection> createState() => _ContentSaveSectionState();
}

class _ContentSaveSectionState extends State<ContentSaveSection> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  String? _title;
  String? _description;
  String? _imageUrl;

  Future<void> _onContentSaved() async {
    _validateFields();
    await _save();
    _goToPrevious();
  }

  void _validateFields() {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    _formKey.currentState!.save();
  }

  Future<void> _save() async {
    await api.post(
      'contents',
      body: {
        'title': _title,
        'description': _description,
        'imageUrl': _imageUrl,
      },
    );
  }

  void _goToPrevious() {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('저장 되었습니다.')),
    );
    Navigator.of(context).pop();
  }

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: Column(
        children: [
          _ContentInputField(
            icon: const Icon(Icons.text_format),
            labelText: '타이틀',
            hintText: '등록할 컨텐츠의 타이틀',
            onSaved: (value) => _title = value,
          ),
          _ContentInputField(
            icon: const Icon(Icons.text_format),
            labelText: '설명',
            hintText: '등록할 컨텐츠의 설명',
            onSaved: (value) => _description = value,
          ),
          _ContentInputField(
            icon: const Icon(Icons.link),
            labelText: '썸네일 URL',
            hintText: '등록할 컨텐츠의 썸네일 URL',
            onSaved: (value) => _imageUrl = value,
          ),
          const SizedBox(height: 30),
          ElevatedButton(
            onPressed: _onContentSaved,
            child: const Text('Submit'),
          ),
        ],
      ),
    );
  }
}

class _ContentInputField extends StatelessWidget {
  const _ContentInputField({
    required this.icon,
    required this.labelText,
    required this.hintText,
    required this.onSaved,
  });

  final Icon icon;
  final String labelText;
  final String hintText;
  final FormFieldSetter<String?> onSaved;

  String? _requiredValue(String? value) {
    if (value == null || value.isEmpty) {
      return '등록하기';
    }

    return null;
  }

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      decoration: InputDecoration(
        icon: icon,
        labelText: labelText,
        hintText: hintText,
      ),
      validator: _requiredValue,
      onSaved: onSaved,
    );
  }
}
