part of 'api.dart';

final _url = Url(Domain());

Uri url(String path) => _url.path(path);

class Domain {
  static const dartDefineKey = 'API_URL'; // --dart-define=<foo=bar>

  static const String _default = 'http://localhost:8080';

  String fromEnvironment() {
    // ⚠️ web 환경에서 에러 주의
    // - Uncaught Unsupported operation: fromEnvironment can only be used as a const constructor
    return const String.fromEnvironment(dartDefineKey, defaultValue: _default);
  }
}

class Url {
  final Uri _url;

  Url(Domain domain)
      : _url = Uri.parse(domain.fromEnvironment()).resolve('api/');

  Uri call() => _url;

  Uri path(String path) => _url.resolve(path);
}
