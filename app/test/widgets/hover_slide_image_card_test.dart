import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:pancake_app/widgets/hover_slide_image_card.dart';

import '../utils.dart';

void main() {
  const visible = 1.0;
  const notVisible = 0.0;

  late ImageProvider testImage;

  setUpAll(() async {
    testImage = TestImageProvider(
      await createTestImage(width: 10, height: 10),
    );
  });

  Widget buildTest({Widget? overlayWidget}) {
    return MaterialApp(
      home: Center(
        child: Material(
          child: HoverSlideImageCard(
            onTap: () {},
            image: testImage,
            overlayWidget: overlayWidget ?? const Text('따분한 바다 생활이 싫어'),
          ),
        ),
      ),
    );
  }

  testWidgets('카드에 마우스가 호버되면 이미지가 흐려지면서 오버레이 위젯이 보여진다', (tester) async {
    //given
    await tester.pumpWidget(buildTest(overlayWidget: const Text('이웃집 토토로')));
    final mouse = await tester.createMouse();

    //when hovered
    await mouse.moveTo(tester.getCenter(find.testingWidget()));
    await tester.pumpAndSettle();

    //then
    expect(tester.getOpacityOfImage(), inOpenClosedRange(notVisible, 0.3));
    expect(tester.getOpacityOfText('이웃집 토토로'), equals(visible));

    //when not hovered
    await mouse.moveTo(Offset.zero);
    await tester.pumpAndSettle();

    //then
    expect(tester.getOpacityOfImage(), equals(visible));
    expect(tester.getOpacityOfText('이웃집 토토로'), equals(notVisible));
  });

  testWidgets('🐛 카드에 마우스가 아주 빠르게 지나가도 오버레이 위젯은 보여지지 않는다', (tester) async {
    //given
    await tester.pumpWidget(buildTest(overlayWidget: const Text('이웃집 토토로')));

    //when
    final mouse = await tester.createMouse();
    await mouse.moveTo(tester.getCenter(find.testingWidget()));
    await mouse.moveTo(Offset.zero);
    await tester.pumpAndSettle();

    //then
    expect(tester.getOpacityOfText('이웃집 토토로'), equals(notVisible));
  });

  /// 👀 https://github.com/viiviii/friendly-pancake-app/issues/2
  testWidgets('🐛 카드 최하단에 마우스가 위치해도 이벤트가 1번만 발생한다', (tester) async {
    //given
    await tester.pumpWidget(buildTest());

    //when
    final mouse = await tester.createMouse();
    await mouse.moveTo(tester.getBottomRight(find.testingWidget()));
    await mouse.moveBy(const Offset(-1, -1));

    //then
    await tester.pumpAndSettle().onError((error, stackTrace) {
      fail(
        '${error.runtimeType}: $error\n'
        '최하단 경계에 마우스 호버 시 슬라이드 애니메이션으로 인해 무한루프가 발생하면 '
        'pumpAndSettle()에서 timed out 에러가 발생한다.\n'
        '$stackTrace',
      );
    });
  });
}

extension _Finder on CommonFinders {
  Finder testingWidget() => byType(HoverSlideImageCard);
}

extension _Tester on WidgetTester {
  double getOpacityOfImage() {
    return widget<Image>(find.byType(Image)).opacity!.value;
  }

  double getOpacityOfText(String description) {
    return widget<FadeTransition>(find.ancestor(
      of: find.text(description),
      matching: find.byType(FadeTransition),
    )).opacity.value;
  }
}
