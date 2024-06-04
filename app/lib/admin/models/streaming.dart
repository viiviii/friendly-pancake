class ContentStreaming {
  final String platform;
  final String url;

  const ContentStreaming({
    required this.platform,
    required this.url,
  });

  factory ContentStreaming.fromJson(Map<String, dynamic> json) {
    return ContentStreaming(
      platform: json['platform'] as String,
      url: json['url'] as String,
    );
  }

  @override
  String toString() {
    return 'ContentStreaming{platform: $platform, url: $url}';
  }
}
