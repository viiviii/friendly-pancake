import 'package:flutter_test/flutter_test.dart';
import 'package:pancake_app/setting/model.dart';

const minutes1 = Duration(minutes: 1);

void main() {
  group('fromJson', () {
    test('비활성화 날짜가 없어도 예외가 발생하지 않는다', () {
      //given
      var json = jsonWith(disableFrom: null);

      //when
      var actual = Platform.fromJson(json);

      //then
      expect(actual.disableFrom, isNull);
    });

    test('비활성화 날짜가 있으면 현지 날짜로 변경한다', () {
      //given
      var json = jsonWith(disableFrom: '2000-12-31 00:00:00Z');

      //when
      var actual = Platform.fromJson(json);

      //then
      expect(actual.disableFrom?.isUtc, isFalse);
    });
  });

  group('toJson', () {
    test('비활성화 날짜가 있는 경우 UTC ISO8601 포맷으로 반환한다', () {
      //given
      var platform = Platform.fromJson(
        jsonWith(disableFrom: '2000-12-31 00:00:00Z'),
      );

      //when
      var actual = platform.toJson();

      //then
      expect(actual, equals({'disableFrom': '2000-12-31T00:00:00.000Z'}));
    });
  });
}

Map<String, String?> jsonWith({required String? disableFrom}) {
  return {
    'platformLabel': 'label',
    'platformName': 'name',
    'disableFrom': disableFrom,
  };
}
