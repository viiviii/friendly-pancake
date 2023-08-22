part of 'api.dart';

const _header = {'Content-Type': 'application/json'};

Future<http.Response> post(String resource,
    {required Map<String, Object?> body}) {
  return http.post(url(resource), headers: _header, body: jsonEncode(body));
}

Future<http.Response> get(String resource) {
  return http.get(url(resource), headers: _header);
}

Future<http.Response> patch(String resource) {
  return http.patch(url(resource), headers: _header);
}
