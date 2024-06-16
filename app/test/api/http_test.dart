import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:http/testing.dart';
import 'package:pancake_app/api/http.dart';

void main() {
  group('decode', () {
    test('empty', () {
      expect(decode(response('')), null);
    });

    test('Map type', () {
      expect(
        decode(response('{"text": "bar", "value": 2, "status": false}')),
        {"text": "bar", "value": 2, "status": false},
      );
    });

    test('List<Map> type', () {
      expect(
        decode(response('[{"text": "bar", "value": 2, "status": false}]')),
        [
          {"text": "bar", "value": 2, "status": false}
        ],
      );
    });
  });

  group('encode', () {
    test('null', () {
      expect(encode(null), 'null');
    });

    test('Map type', () {
      expect(
        encode({'text': 'foo', 'value': 2, 'status': false, 'extra': null}),
        '{"text":"foo","value":2,"status":false,"extra":null}',
      );
    });

    test('List<Map> type', () {
      expect(
        encode([
          {'text': 'foo', 'value': 2, 'status': false, 'extra': null}
        ]),
        '[{"text":"foo","value":2,"status":false,"extra":null}]',
      );
    });
  });

  group('Api.class', () {
    test('`body`가 없는 get 요청', () async {
      //given
      var client = MockClient(withSuccess(path: '/name'));
      var api = Api(baseUrl: '', client: client);

      //when
      var actual = await api.get('/name');

      //then
      expect(actual.success, isTrue);
    });
  });

  test('`body`가 없는 post 요청', () async {
    //given
    var client = MockClient(withSuccess(path: '/name'));
    var api = Api(baseUrl: '', client: client);

    //when
    var actual = await api.post('/name');

    //then
    expect(actual.success, isTrue);
  });

  test('`body`가 있는 post 요청', () async {
    //given
    var client = MockClient(withSuccess(path: '/name'));
    var api = Api(baseUrl: '', client: client);

    //when
    var actual = await api.post('/name', body: 'John');

    //then
    expect(actual.success, isTrue);
  });
}

MockClientHandler withSuccess({String? path}) {
  return (request) async {
    return switch (request) {
      _ when request.url.path != path => response('', statusCode: 404),
      _ => response('', statusCode: 200),
    };
  };
}

http.Response response(String body, {int statusCode = 200}) {
  const header = {'content-type': 'application/json'};

  return http.Response(body, statusCode, headers: header);
}
