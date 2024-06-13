import 'package:flutter_test/flutter_test.dart';
import 'package:pancake_app/content/view_models/content_search_view_model.dart';

const minutes1 = Duration(minutes: 1);

void main() {
  group('검색 결과 메시지', () {
    test('결과가 없는 경우 메시지를 반환한다', () {
      expect(
        _SearchResultMessage(data: [])(),
        contains('검색 결과가 없습니다'),
      );
    });

    test('조회되지 않은 더 많은 결과가 있는 경우 메시지를 반환한다', () {
      expect(
        _SearchResultMessage(hasMore: true, data: [1, 2, 3])(),
        contains('더 많은 검색 결과가 있습니다'),
      );
    });

    test('모든 결과가 조회된 경우 메시지를 반환하지 않는다', () {
      expect(
        _SearchResultMessage(hasMore: false, data: [1, 2, 3])(),
        isNull,
      );
    });
  });

  group('화면에 보여질 컨텐츠 제목', () {
    test('오리지널 제목과 제목이 같은 경우 제목만 보여진다', () {
      expect(
        _DisplayTitle(title: '범죄도시 4', originalTitle: '범죄도시 4')(),
        equals('범죄도시 4'),
      );
    });

    test('오리지널 제목과 제목이 다른 경우 함께 보여진다', () {
      expect(
        _DisplayTitle(title: '아이언맨', originalTitle: 'Iron Man')(),
        equals('아이언맨 (Iron Man)'),
      );
    });
  });
}

class _SearchResultMessage<T> with SearchResultMessage<T> {
  @override
  final String query;
  @override
  final bool hasMore;
  @override
  final List<T> data;

  _SearchResultMessage({
    this.query = '검색어',
    this.hasMore = false,
    this.data = const [],
  });

  String? call() => searchResultMessage;
}

class _DisplayTitle with DisplayTitle {
  @override
  final String title;
  @override
  final String originalTitle;

  _DisplayTitle({required this.title, this.originalTitle = ''});

  String call() => displayTitle;
}
