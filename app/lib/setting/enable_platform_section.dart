import 'package:flutter/material.dart';

import 'model.dart';
import 'view_model.dart';

class EnablePlatformSection extends StatelessWidget {
  const EnablePlatformSection({
    super.key,
    required this.data,
    required this.onSettingChanged,
  });

  final List<Platform> data;
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
                  return EnablePlatform(
                    platform: data[index],
                    child: _SettingTile(
                      onChanged: onSettingChanged,
                    ),
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

class _SettingTile extends StatelessWidget {
  const _SettingTile({required this.onChanged});

  final VoidCallback onChanged;

  Future<void> _onSwitch(bool? value, EnablePlatform platform) async {
    await platform.enableSwitch(value!);
    onChanged();
  }

  Future<void> _onPickDisableDate(
      BuildContext context, EnablePlatform platform) async {
    final disableFrom = await _showDisableDatePicker(
      context,
      range: platform.selectableRange,
      disableFrom: platform.disableFrom,
    );

    await platform.pick(disableFrom);
    onChanged();
  }

  Future<DateTime?> _showDisableDatePicker(BuildContext context,
      {required DateTimeRange range, DateTime? disableFrom}) async {
    return showDatePicker(
      context: context,
      firstDate: range.start,
      lastDate: range.end,
      currentDate: disableFrom,
      helpText: '비활성화 날짜 선택',
    );
  }

  @override
  Widget build(BuildContext context) {
    final platform = EnablePlatform.of(context);
    return Card(
      child: ListTile(
        enabled: platform.enabled,
        selected: platform.enabled,
        onTap: () => _onPickDisableDate(context, platform),
        minVerticalPadding: 16,
        leading: Text(
          platform.enableEmoji,
          style: Theme.of(context).textTheme.titleLarge,
        ),
        title: Text(platform.label),
        subtitle: platform.disableDateText,
        trailing: Switch(
          onChanged: (enabled) => _onSwitch(enabled, platform),
          value: platform.enabled,
        ),
      ),
    );
  }
}
