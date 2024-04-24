part of 'api.dart';

const _header = {'Content-Type': 'application/json'};

typedef HttpRequest = Future<http.Response> Function();

Future<ApiResult<T>> post<T>(String path,
    {required Map<String, Object?> body}) async {
  return _process(
    () => http.post(url(path), headers: _header, body: jsonEncode(body)),
  );
}

Future<ApiResult<T>> get<T>(String path) async {
  return _process(() => http.get(url(path), headers: _header));
}

Future<ApiResult<T>> put<T>(String path, {Map<String, Object?>? body}) async {
  return _process(
      () => http.put(url(path), headers: _header, body: jsonEncode(body)));
}

// TODO: body
Future<ApiResult<T>> patch<T>(String path, {String? body}) async {
  return _process(() => http.patch(url(path), headers: _header, body: body));
}

Future<ApiResult<T>> _process<T>(HttpRequest request) async {
  final response = await request();

  return _mapToApiResult(response);
}

ApiResult<T> _mapToApiResult<T>(http.Response response) {
  return ApiResult(
    success: response.success,
    body: _parse(response),
  );
}

T? _parse<T>(http.Response response) {
  if (response.hasBody) {
    return _parseToJson(response);
  }

  return null;
}

T _parseToJson<T>(http.Response response) {
  final json = jsonDecode(utf8.decode(response.bodyBytes));

  return json is List ? json.cast<Map<String, dynamic>>() : json;
}

extension on http.Response {
  bool get success => (statusCode ~/ 100) == 2;
  bool get hasBody => bodyBytes.isNotEmpty;
}

class ApiResult<T> {
  final bool success;
  final T? body;

  ApiResult({
    required this.success,
    required this.body,
  });
}
