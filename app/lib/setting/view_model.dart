import 'package:flutter/material.dart';
import 'package:pancake_app/api/api.dart' as api;

import 'model.dart';

class EnablePlatform extends InheritedWidget {
  const EnablePlatform({
    super.key,
    required Platform platform,
    required super.child,
  }) : _platform = platform;

  final Platform _platform;

  static EnablePlatform of(BuildContext context) {
    final result = context.dependOnInheritedWidgetOfExactType<EnablePlatform>();
    assert(result != null, 'No Platform found in context');
    return result!;
  }

  String get label => _platform.label;

  DateTime? get disableFrom => _platform.disableFrom;

  bool get enabled => disableFrom?.isAfter(DateTime.now()) ?? true;

  String get enableMessage => enabled ? '👀' : '💤';

  // TODO: intl 라이브러리 추가
  String get disableMessage {
    final disable = disableFrom;
    if (!enabled || disable == null) {
      return '';
    }
    return '${disable.year}년 ${disable.month}월 ${disable.day}일부터 비활성화';
  }

  DateTimeRange get selectableRange {
    return DateTimeRange(
      start: DateTime.now(),
      end: DateTime.now().add(const Duration(days: 365)),
    );
  }

  Future<void> enableSwitch(bool enable) async {
    _platform.disableFrom = enable ? null : DateTime.now();
    await _update();
  }

  Future<void> pick(DateTime? picked) async {
    if (picked == null) {
      return;
    }
    _platform.disableFrom = picked;
    await _update();
  }

  Future<void> _update() async {
    await api.put(
      'settings/platforms/${_platform.name}',
      body: _platform.toJson(),
    );
  }

  @override
  bool updateShouldNotify(EnablePlatform oldWidget) =>
      disableFrom != oldWidget.disableFrom;
}
