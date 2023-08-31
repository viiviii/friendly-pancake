import 'package:flutter_test/flutter_test.dart';
import 'package:pancake_app/api/api.dart' as api;
import 'package:pancake_app/api/api.dart';

class Url {
  final Uri _url;

  Url(Domain domain)
      : _url = Uri.parse(domain.fromEnvironment()).resolve('api/');

  Uri call() => _url;
}

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

  test('url(String path) ', () {
    expect('${api.url('contents')}', 'http://localhost:8080/api/contents');
    expect('${api.url('users')}', 'http://localhost:8080/api/users');
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
