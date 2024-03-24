class Content {
  final int id;
  final String title;
  final String description;
  final String imageUrl;
  final bool watched;
  final List<Option> options;

  const Content({
    required this.id,
    required this.title,
    required this.description,
    required this.imageUrl,
    required this.watched,
    required this.options,
  });

  factory Content.fromJson(Map<String, dynamic> json) {
    return Content(
      id: json['id'] as int,
      title: json['title'] as String,
      description: json['description'] as String,
      imageUrl: json['imageUrl'] as String,
      watched: json['watched'] as bool,
      options: json['options'].map<Option>((e) => Option.fromJson(e)).toList(),
    );
  }

  Option get shortcutOption => options.first;

  @override
  String toString() {
    return 'Content{id: $id, title: $title, description: $description, imageUrl: $imageUrl, watched: $watched, options: $options}';
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is Content && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}

class Option {
  final String platformName;
  final String platformLabel;

  const Option({
    required this.platformName,
    required this.platformLabel,
  });

  factory Option.fromJson(Map<String, dynamic> json) {
    return Option(
      platformName: json['platform'] as String,
      platformLabel: json['platform'] as String, // TODO: api에서 뺴먹었음
    );
  }

  @override
  String toString() {
    return 'Option{platformName: $platformName, platformLabel: $platformLabel}';
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is Option &&
          runtimeType == other.runtimeType &&
          platformName == other.platformName;

  @override
  int get hashCode => platformName.hashCode;
}
