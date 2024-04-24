import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:pancake_app/setting/model.dart';
import 'package:pancake_app/setting/view_model.dart';

const minutes1 = Duration(minutes: 1);

void main() {
  group('활성화 여부', () {
    test('비활성화 날짜가 없으면 [true]이다', () {
      //given
      var platform = enablePlatformWith(null);

      //when
      var actual = platform.enabled;

      //then
      expect(actual, isTrue);
    });

    test('비활성화 날짜가 현재 시간 이후면 [true]이다', () {
      //given
      var nowAfter = DateTime.now().add(minutes1);
      var platform = enablePlatformWith(nowAfter);

      //when
      var actual = platform.enabled;

      //then
      expect(actual, isTrue);
    });

    test('비활성화 날짜가 현재 시간 이전이면 [false]이다', () {
      //given
      var nowBefore = DateTime.now().subtract(minutes1);
      var platform = enablePlatformWith(nowBefore);

      //when
      var actual = platform.enabled;

      //then
      expect(actual, isFalse);
    });
  });

  group('비활성화 상태 메시지', () {
    test('현재 활성화 상태이고 비활성화 날짜가 있으면 포매팅하여 반환한다', () {
      //given
      var platform = enablePlatformWith(
        DateTime.parse('2099-01-20 20:18:04Z'),
      );

      //when
      var actual = platform.disableMessage;

      //then
      expect(actual, equals('2099년 1월 20일부터 비활성화'));
      expect(platform.enabled, isTrue);
    });

    test('현재 활성화 상태이고 비활성화 날짜가 없으면 빈 문자열을 반환한다', () {
      //given
      var platform = enablePlatformWith(null);

      //when
      var actual = platform.disableMessage;

      //then
      expect(actual, isEmpty);
      expect(platform.enabled, isTrue);
    });

    test('현재 비활성화 상태이면 빈 문자열을 반환한다', () {
      //given
      var platform = enablePlatformWith(DateTime.parse('2000-01-20 20:18:04Z'));

      //when
      var actual = platform.disableMessage;

      //then
      expect(actual, isEmpty);
      expect(platform.enabled, isFalse);
    });
  });

  test('선택 가능한 비활성화 날짜 범위는 오늘로부터 1년 간이다', () {
    //given
    var platform = enablePlatformWith(DateTime.parse('2000-01-20 20:18:04Z'));
    var now = DateTime.now();

    //when
    var actual = platform.selectableRange;

    //then
    expect(DateUtils.isSameDay(actual.start, now), isTrue);
    expect(actual.end.difference(actual.start).inDays, equals(365));
  });
}

EnablePlatform enablePlatformWith(DateTime? disableFrom) {
  return EnablePlatform(
    platform: platformWith(disableFrom),
    child: const SizedBox.shrink(),
  );
}

Platform platformWith(DateTime? disableFrom) {
  return Platform(label: 'label', name: 'name', disableFrom: disableFrom);
}
