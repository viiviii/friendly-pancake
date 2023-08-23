part of 'api.dart';

const _header = {'Content-Type': 'application/json'};

typedef HttpRequest = Future<http.Response> Function();

Future<T> post<T>(String path, {required Map<String, Object?> body}) async {
  return _process(
    () => http.post(url(path), headers: _header, body: jsonEncode(body)),
  );
}

Future<T> get<T>(String path) async {
  return _process(() => http.get(url(path), headers: _header));
}

Future<T> patch<T>(String path) async {
  return _process(() => http.patch(url(path), headers: _header));
}

Future<T> _process<T>(HttpRequest request) async {
  final response = await request();
  assert(response.success, '지금은 귀찮아'); // TODO

  return _parseToJson<T>(response);
}

T _parseToJson<T>(http.Response response) {
  final json = jsonDecode(utf8.decode(response.bodyBytes));

  return json is List ? json.cast<Map<String, dynamic>>() : json;
}

extension on http.Response {
  bool get success => (statusCode ~/ 100) == 2;
}
