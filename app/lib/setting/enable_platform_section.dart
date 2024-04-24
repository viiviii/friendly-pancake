import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' as api;

import 'model.dart';

class EnablePlatformSection extends StatelessWidget {
  const EnablePlatformSection({
    super.key,
    required this.data,
    required this.onSettingChanged,
  });

  final List<PlatformSetting> data;
  final VoidCallback onSettingChanged;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(vertical: 20, horizontal: 60),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Expanded(
              flex: 1,
              child: Text(
                '시청 플랫폼',
                style: Theme.of(context).textTheme.headlineSmall,
              ),
            ),
            Expanded(
              flex: 2,
              child: ListView.builder(
                shrinkWrap: true,
                itemCount: data.length,
                itemBuilder: (_, index) {
                  return _SettingTile(
                    onChanged: onSettingChanged,
                    setting: data[index],
                  );
                },
              ),
            )
          ],
        ),
      ),
    );
  }
}

class _SettingTile extends StatefulWidget {
  const _SettingTile({required this.setting, required this.onChanged});

  final PlatformSetting setting;
  final VoidCallback onChanged;

  @override
  State<_SettingTile> createState() => _SettingTileState();
}

class _SettingTileState extends State<_SettingTile> {
  late bool _enabled;
  DateTime? _picked;

  @override
  void initState() {
    super.initState();
    _enabled = widget.setting.disableFrom?.isAfter(DateTime.now()) ?? true;
  }

  Future<void> _onSwitch(bool? value) async {
    setState(() {
      _enabled = value!;
      _picked = _enabled ? null : DateTime.now();
    });
    await _changeSetting();
    widget.onChanged();
  }

  Future<void> _showDatePicker() async {
    _picked = await showDatePicker(
      context: context,
      firstDate: DateTime.now(),
      lastDate: DateTime.now().add(const Duration(days: 365)),
      initialDate: widget.setting.disableFrom?.toLocal() ??
          DateTime.now(), // TODO: 버전 올리면 optional
      currentDate: widget.setting.disableFrom?.toLocal(),
      helpText: '비활성화 날짜 선택',
    );
    if (_picked == null) {
      return;
    }
    await _changeSetting();
    widget.onChanged();
  }

  Future<void> _changeSetting() async {
    final platform = widget.setting.platformName;
    final disableFrom = _picked?.toUtc().toIso8601String();
    await api.put('settings/platforms/$platform', body: {
      'disableFrom': disableFrom,
    });
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      enabled: _enabled,
      selected: _enabled,
      onTap: _showDatePicker,
      leading: Text(_enabled ? '👀' : '💤'), // TODO: 이모지 색깔 보이게(버전 올려야됨)
      title: Text(widget.setting.platformLabel),
      subtitle: Text(widget.setting.disableFrom?.toLocal().toIso8601String() ??
          ''), // TODO: 포맷 추가
      trailing: Switch(
        onChanged: _onSwitch,
        value: _enabled,
      ),
    );
  }
}
