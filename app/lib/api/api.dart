import 'http.dart';
import 'url.dart';

export 'http.dart' show ApiResult, Api;

final api = Api(baseUrl: Url.value, client: createClient());
