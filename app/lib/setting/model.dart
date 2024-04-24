class Platform {
  final String label;
  final String name;
  DateTime? disableFrom;

  Platform({
    required this.label,
    required this.name,
    required this.disableFrom,
  });

  factory Platform.fromJson(Map<String, dynamic> json) {
    return Platform(
      label: json['platformLabel'] as String,
      name: json['platformName'] as String,
      disableFrom: DateTime.tryParse(json['disableFrom'] ?? '')?.toLocal(),
    );
  }

  @override
  String toString() {
    return 'Platform{label: $label, name: $name, disableFrom: $disableFrom}';
  }
}
