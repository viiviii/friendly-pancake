import 'dart:convert';

import 'package:http/http.dart' as http;

const _header = {'Content-Type': 'application/json'};

http.Client createClient() => http.Client();

class ApiResult<T> {
  final bool success;
  final T? body;

  ApiResult({
    required this.success,
    required this.body,
  });
}

class Api {
  final Uri _baseUrl;
  final http.Client _client;

  Api({required String baseUrl, required http.Client client})
      : _baseUrl = Uri.parse(baseUrl).resolve('api/'),
        _client = client;

  Uri url(String path) => _baseUrl.resolve(path);

  Future<ApiResult<T>> get<T>(String path) async {
    return _process(_client.get, path);
  }

  Future<ApiResult<T>> post<T>(String path, {Object? body}) async {
    return _process(_client.post, (path, body));
  }

  Future<ApiResult<T>> put<T>(String path, {Object? body}) async {
    return _process(_client.put, (path, body));
  }

  Future<ApiResult<T>> patch<T>(String path, {Object? body}) async {
    return _process(_client.patch, (path, body));
  }

  Future<ApiResult<T>> _process<T>(Function handler, Object params) async {
    final response = await _perform(handler, params);

    return _toResult(response);
  }

  Future<http.Response> _perform(Function request, Object params) async {
    return switch ((request, params)) {
      (RequestWithBody _, (String path, Object? body)) => request(
          url(path),
          headers: _header,
          body: encode(body),
        ),
      (RequestWithoutBody _, String path) => request(
          url(path),
          headers: _header,
        ),
      _ => throw AssertionError(),
    };
  }

  ApiResult<T> _toResult<T>(http.Response response) {
    return ApiResult(success: response.success, body: decode(response));
  }
}

String encode(Object? body) {
  return jsonEncode(body);
}

T? decode<T>(http.Response response) {
  if (!response.hasBody) {
    return null;
  }

  final json = jsonDecode(utf8.decode(response.bodyBytes));

  return switch (json) {
    List _ => json.cast<Map<String, dynamic>>(),
    _ => json,
  };
}

typedef RequestWithoutBody = Future<http.Response> Function(
  Uri url, {
  Map<String, String>? headers,
});

typedef RequestWithBody = Future<http.Response> Function(
  Uri url, {
  Map<String, String>? headers,
  Object? body,
  Encoding? encoding,
});

extension on http.Response {
  bool get success => (statusCode ~/ 100) == 2;
  bool get hasBody => bodyBytes.isNotEmpty;
}
