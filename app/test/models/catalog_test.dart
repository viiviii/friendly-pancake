import 'package:flutter_test/flutter_test.dart';
import 'package:pancake_app/models/catalog.dart';
import 'package:pancake_app/models/content.dart';

void main() {
  group('시청한 컨텐츠를 최대 5개 반환한다', () {
    test('5개 이상일 때', () {
      //given
      var catalog = _catalogWith([
        _contentWith(true),
        _contentWith(true),
        _contentWith(true),
        _contentWith(true),
        _contentWith(true),
        _contentWith(false),
        _contentWith(true),
        _contentWith(true),
      ]);

      //when
      var actual = catalog.watchedContents;

      //then
      expect(actual.length, equals(5));
      expect(actual.every((e) => e.watched), isTrue);
    });

    test('5개 이하일 때', () {
      //given
      var catalog = _catalogWith([
        _contentWith(true),
        _contentWith(false),
      ]);

      //when
      var actual = catalog.watchedContents;

      //then
      expect(actual, hasLength(1));
      expect(actual.every((e) => e.watched), isTrue);
    });
  });

  test('시청하지 않은 컨텐츠들을 반환한다', () {
    //given
    var catalog = _catalogWith([
      _contentWith(false),
      _contentWith(true),
      _contentWith(false),
      _contentWith(false),
    ]);

    //when
    var actual = catalog.unwatchedContents;

    //then
    expect(actual, hasLength(3));
    expect(actual.every((e) => e.watched), isFalse);
  });
}

Catalog _catalogWith(List<Content> contents) {
  return Catalog(title: '카탈로그 제목', contents: contents);
}

Content _contentWith(bool watched) {
  return Content(
    id: 1,
    title: '컨텐츠 제목',
    description: '설명',
    imageUrl: '이미지',
    watched: watched,
    options: [const Option(platformName: '플랫폼', platformLabel: '플랫폼')],
  );
}
