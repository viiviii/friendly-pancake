import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' as api;
import 'package:pancake_app/setting/enable_platform_section.dart';
import 'package:pancake_app/widgets/my_future_builder.dart';

import 'model.dart';

class SettingScreen extends StatefulWidget {
  const SettingScreen({Key? key}) : super(key: key);

  @override
  State<SettingScreen> createState() => _SettingScreenState();
}

class _SettingScreenState extends State<SettingScreen> {
  late Future<List<Platform>> _settings;

  @override
  void initState() {
    super.initState();
    _settings = _getSettings();
  }

  void _onSettingChanged() {
    setState(() {
      _settings = _getSettings();
    });
  }

  Future<List<Platform>> _getSettings() async {
    final res = await api.get<List<Map<String, dynamic>>>('settings/platforms');
    return res.body!.map(Platform.fromJson).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('설정'),
      ),
      body: MyFutureBuilder<List<Platform>>(
        future: _settings,
        builder: (_, data) {
          return EnablePlatformSection(
            data: data,
            onSettingChanged: _onSettingChanged,
          );
        },
      ),
    );
  }
}
