class Url {
  static const String dartDefineKey = 'API_URL'; // --dart-define=<foo=bar>

  // ⚠️ web 환경에서 에러 주의
  // - Uncaught Unsupported operation: fromEnvironment can only be used as a const constructor
  static const String value = String.fromEnvironment(
    dartDefineKey,
    defaultValue: 'http://localhost:8080',
  );
}
