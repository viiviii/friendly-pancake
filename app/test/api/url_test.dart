import 'package:flutter_test/flutter_test.dart';
import 'package:pancake_app/api/api.dart';

void main() {
  test('url()', () {
    expect(url('posts').path, '/api/posts');
  });

  group('Domain', () {
    test('dartDefineKey', () {
      expect(Domain.dartDefineKey, 'API_URL');
    });

    test('defaultValue', () {
      expect(Domain().fromEnvironment(), 'http://localhost:8080');
    });
  });

  group('Url', () {
    group('url()', () {
      test('when domain is http', () {
        //given
        final url = Url(StubDomain('http://localhost:80'));

        //when
        final actual = url();

        //then
        expect(actual, Uri.http('localhost:80', 'api/'));
      });

      test('when domain is https', () {
        //given
        final url = Url(StubDomain('https://my.example.site:443'));

        //when
        final actual = url();

        //then
        expect(actual, Uri.https('my.example.site:443', 'api/'));
      });
    });

    group('path()', () {
      test('when domain is http', () {
        //given
        final url = Url(StubDomain('http://localhost:80'));

        //when
        final actual = url.path('posts');

        //then
        expect(actual, Uri.http('localhost:80', 'api/posts'));
      });

      test('when domain is https', () {
        //given
        final url = Url(StubDomain('https://my.example.site:443'));

        //when
        final actual = url.path('posts');

        //then
        expect(actual, Uri.https('my.example.site:443', 'api/posts'));
      });

      test('여러 번 호출해도 /api/를 제외한 {path}만 변경 된다', () {
        //given
        final url = Url(StubDomain('http://localhost:8080'));

        //then
        expect(url.path('posts').path, '/api/posts');
        expect(url.path('users').path, '/api/users');
      });
    });
  });

  group('Uri 학습 테스트', () {
    test('properties', () {
      //given
      final uri = Uri.http('localhost:8080', 'api');

      //then
      expect(uri.scheme, 'http');
      expect(uri.host, 'localhost');
      expect(uri.port, 8080);
      expect(uri.path, '/api');
      expect(uri.origin, 'http://localhost:8080');
    });

    test('toString()', () {
      //given
      final uri = Uri.http('localhost:8080', 'api');

      //then
      expect('$uri', 'http://localhost:8080/api');
    });

    test('resolve()', () {
      //given
      final uri = Uri.http('localhost:8080', 'api');

      //when
      final actual = uri.resolve('contents');

      //then
      expect('$actual', 'http://localhost:8080/contents');
    });

    test('resolve() for path with trailing /', () {
      //given
      final uri = Uri.http('localhost:8080', 'api/');

      //when
      final actual = uri.resolve('contents');

      //then
      expect('$actual', 'http://localhost:8080/api/contents');
    });
  });
}

class StubDomain implements Domain {
  final String _domain;

  StubDomain(this._domain);

  @override
  String fromEnvironment() => _domain;
}
