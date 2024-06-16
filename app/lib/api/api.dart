import 'http.dart';
import 'url.dart';

export 'http.dart' show ApiResult, Api;

final api = Api(baseUrl: Url.value, client: createClient());

Uri url(String path) => api.url(path);

Future<ApiResult<T>> get<T>(String path) => api.get(path);

Future<ApiResult<T>> post<T>(String path, {Object? body}) async {
  return api.post(path, body: body);
}

Future<ApiResult<T>> put<T>(String path, {Object? body}) async {
  return api.put(path, body: body);
}

Future<ApiResult<T>> patch<T>(String path, {Object? body}) async {
  return api.patch(path, body: body);
}
