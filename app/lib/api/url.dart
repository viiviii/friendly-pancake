part of 'api.dart';

final _url = Url(Domain());

Uri url(String path) => _url.path(path);

class Domain {
  static const _dartDefineKey = 'API_HOST'; // --dart-define=<foo=bar>

  final String _defaultValue = 'http://localhost:8080';

  String fromEnvironment() {
    return String.fromEnvironment(_dartDefineKey, defaultValue: _defaultValue);
  }
}

class Url {
  final Uri _url;

  Url(Domain domain)
      : _url = Uri.parse(domain.fromEnvironment()).resolve('api/');

  Uri call() => _url;

  Uri path(String path) => _url.resolve(path);
}
