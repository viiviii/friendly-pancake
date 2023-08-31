import 'package:flutter_test/flutter_test.dart';
import 'package:pancake_app/api/api.dart' as api;
import 'package:pancake_app/api/api.dart';

void main() {
  test('Domain defaultValue', () {
    final domain = Domain();
    expect(domain.fromEnvironment(), 'http://localhost:8080');
  });

  test('url()', () {
    expect('${api.url('contents')}', 'http://localhost:8080/api/contents');
    expect('${api.url('users')}', 'http://localhost:8080/api/users');
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
