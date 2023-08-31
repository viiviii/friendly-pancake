import 'package:flutter_test/flutter_test.dart';
import 'package:pancake_app/api/api.dart';

class StubDomain implements Domain {
  final String _domain;

  StubDomain(this._domain);

  @override
  String fromEnvironment() => _domain;
}

void main() {
  test('Domain defaultValue', () {
    final domain = Domain();
    expect(domain.fromEnvironment(), 'http://localhost:8080');
  });

  test('url path http', () {
    //given
    final url = Url(StubDomain('http://localhost:8080'));

    //when
    final actual = url.path('posts');

    //then
    expect(actual, Uri.http('localhost:8080', 'api/posts'));
  });

  test('url path https', () {
    //given
    final url = Url(StubDomain('https://my.example.site:443'));

    //when
    final actual = url.path('posts');

    //then
    expect(actual, Uri.https('my.example.site:443', 'api/posts'));
  });

  test('path()를 여러 번 호출해도 /api/ 뒤의 마지막 {path}만 변경 된다', () {
    //given
    final url = Url(StubDomain('http://localhost:8080'));

    //then
    expect('${url.path('posts')}', 'http://localhost:8080/api/posts');
    expect('${url.path('users')}', 'http://localhost:8080/api/users');
  });

  test('url() with http', () {
    //given
    final url = Url(StubDomain('http://www.localhost:81'));

    //when
    final actual = url();

    //then
    expect(actual, Uri.http('www.localhost:81', 'api/'));
  });

  test('url() with https', () {
    //given
    final url = Url(StubDomain('https://my.example.site:8443'));

    //when
    final actual = url();

    //then
    expect(actual, Uri.https('my.example.site:8443', 'api/'));
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
