part of 'api.dart';

const _dartDefineKey = 'API_HOST'; // --dart-define=<foo=bar>
const _local = 'localhost:8080';
const _host = String.fromEnvironment(_dartDefineKey, defaultValue: _local);

Uri url(String path) => Uri.http(_host, 'api/$path');
