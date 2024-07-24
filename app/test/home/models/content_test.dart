import 'package:flutter_test/flutter_test.dart';
import 'package:pancake_app/home/models/content.dart';

void main() {
  test('바로 시청할 옵션으로 첫번째 옵션을 반환한다', () {
    //given
    var content = _contentWith([
      _option('디즈니플러스'),
      _option('넷플릭스'),
      _option('티빙'),
    ]);

    //when
    var actual = content.shortcutOption;

    //then
    expect(actual, equals(_option('디즈니플러스')));
  });
}

Content _contentWith(List<Option> options) {
  return Content(
    id: 1,
    title: '제목',
    description: '설명',
    imageUrl: '이미지',
    watched: false,
    options: options,
  );
}

Option _option(String platform) {
  return Option(platformName: platform, platformLabel: platform);
}
