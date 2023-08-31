part of 'api.dart';

final _domain = Domain();

Uri url(String path) =>
    Uri.parse(_domain.fromEnvironment()).resolve('api/$path');

class Domain {
  static const _dartDefineKey = 'API_HOST'; // --dart-define=<foo=bar>

  final String _defaultValue = 'http://localhost:8080';

  String fromEnvironment() {
    return String.fromEnvironment(_dartDefineKey, defaultValue: _defaultValue);
  }
}
